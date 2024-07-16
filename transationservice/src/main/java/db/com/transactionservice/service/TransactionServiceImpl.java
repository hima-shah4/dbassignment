package db.com.transactionservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import db.com.transactionservice.client.AccountServiceClient;
import db.com.transactionservice.entity.Transaction;
import db.com.transactionservice.repository.TransactionRepository;
import db.com.transactionservice.vo.error.TransactionFailureException;
import db.com.transactionservice.vo.error.UserDetailsNotFound;
import db.com.transactionservice.vo.request.CreditBalanceRequest;
import db.com.transactionservice.vo.request.TransactionRequest;
import db.com.transactionservice.vo.request.WithdrawBalanceRequest;
import db.com.transactionservice.vo.response.CreditBalanceResponse;
import db.com.transactionservice.vo.response.TransactionHistoryResponse;
import db.com.transactionservice.vo.response.TransactionRecord;
import db.com.transactionservice.vo.response.TransactionResponse;
import db.com.transactionservice.vo.response.UserDetailsResponse;
import db.com.transactionservice.vo.response.WithdrawBalanceResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
@AllArgsConstructor

public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private AccountServiceClient accountServiceClient ;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Value("${mq.name}")
	private String queueDestination;
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
	
	private final String SEPERATOR=",";
	
	@Override
	@Transactional 
	public TransactionResponse transact(TransactionRequest transactionRequest) {
		logger.debug("transactionRequest: {}",transactionRequest);
		boolean blnRes = false;
		String responseMessage ="";
		double txnAmount = transactionRequest.getAmount();
		long toAccount = transactionRequest.getAccountTo();
		long fromAccount = transactionRequest.getAccountFrom();
		if(withrawBalance(fromAccount, txnAmount)) {
		    try {
				blnRes = creditBalance(toAccount,txnAmount);
				if(blnRes) {
					responseMessage ="Transaction is successful";
				}else {
					responseMessage = "Credit to the account Failed";
					logger.warn("Credit to the account failed");
					//rollback withdrawal
					creditBalance(fromAccount, txnAmount);
				}
			}catch(Exception e) {
				logger.error("Error while crediting to the account");
				//rollback withdrwal
				creditBalance(fromAccount, txnAmount);
			}
		}else {
			logger.warn("Withdrwal from the account failed");
			responseMessage ="Withdrawal from the account Failed";
		}
	    TransactionResponse transactionResponse =  new TransactionResponse(blnRes, responseMessage);
	    logger.debug("transactionResponse: {}",transactionResponse);
	    return transactionResponse;

	}

	@Transactional
	public boolean creditBalance(long accountTo, double amount) {
		boolean blnResult = false;
		CreditBalanceRequest creditBalanceRequest = new CreditBalanceRequest(accountTo, amount);
		ResponseEntity<CreditBalanceResponse> creditBalanceResponse;
		try {
			creditBalanceResponse = accountServiceClient.creditBalance(creditBalanceRequest);
			if(creditBalanceResponse.getStatusCode()==HttpStatus.OK && creditBalanceResponse.getBody()!=null &&
					creditBalanceResponse.getBody().isStatus()) {
				blnResult = true;
				saveTransaction(accountTo,amount,"Cr");
				sendEmail("Account Credit Receipt","Your account has been Credited with "+amount, accountTo);
			}
		}catch(Exception e) {
			new TransactionFailureException("credit to the account failed");
			
		}
		return blnResult;
	}
	
	@Transactional
	public boolean withrawBalance(long accountFrom, double amount){
		boolean blnResult = false;
		WithdrawBalanceRequest withdrawBalanceRequest = new WithdrawBalanceRequest(accountFrom, amount);
		ResponseEntity<WithdrawBalanceResponse> withdrawBalanceResponse = 
				accountServiceClient.withdrawBalance(withdrawBalanceRequest);
		if(withdrawBalanceResponse.getStatusCode()==HttpStatus.OK && withdrawBalanceResponse.getBody()!=null
			&& withdrawBalanceResponse.getBody().isStatus()) {
			blnResult =true;
			saveTransaction(accountFrom,amount,"Db");
//			auditService.enterTransactionAuditData(accountFrom, amount, "Db");
		}
		sendEmail("Account Debit Receipt","Your account has been debited with "+amount, accountFrom);
		return blnResult;
	}

	@Transactional
    public void saveTransaction(long account, double amount, String transactionType) {
        transactionRepository.save(Transaction.builder().account(account).amount(amount).transactionType(transactionType)
        		.build());
    }
	
	@Async
	public void sendEmail(String subject, String message, long accountNumber) {
		 String emailTo =  getUserEmailFromAccount(accountNumber);
		// as broker is not supporting custom object , sending string
			String msgString = new StringBuilder().append(emailTo).append(SEPERATOR)
								.append(subject).append(SEPERATOR)
								.append(message).toString();
	        jmsTemplate.convertAndSend(queueDestination, msgString);
	}

	private String getUserEmailFromAccount(long accountNumber) {
		ResponseEntity<UserDetailsResponse> userDetails = accountServiceClient.getUserDetails(accountNumber);
		if(userDetails ==null ||userDetails.getStatusCode() !=HttpStatus.OK ||userDetails.getBody()==null
				|| ObjectUtils.isEmpty(userDetails.getBody().getEmail())) {
			throw new UserDetailsNotFound("User Details Not Found");
		}else {
			return userDetails.getBody().getEmail();
		}
	}
	
	@Override
	public TransactionHistoryResponse getTransactionHistory(long account, LocalDateTime fromDate, LocalDateTime toDate) {
		List<Transaction> transactions = transactionRepository.findByAccountAndCreatedOnBetween(account, fromDate, toDate);
		
		if(transactions.isEmpty()) {
			return new TransactionHistoryResponse(new ArrayList<>(),"No Records Found.");
		}else {
			List<TransactionRecord> transactionRecordList = transactions.stream()
					.map(transaction ->convertToDto(transaction)).collect(Collectors.toList());
			return new TransactionHistoryResponse(transactionRecordList,
					"Transactions retrieved successfully.");
		}	
	}
	
	 private TransactionRecord convertToDto(Transaction transaction) {
		 	ModelMapper modelMapper = new ModelMapper();
	        return modelMapper.map(transaction, TransactionRecord.class);
	 }
}
