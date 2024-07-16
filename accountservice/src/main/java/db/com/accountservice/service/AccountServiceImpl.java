package db.com.accountservice.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import db.com.accountservice.entity.Account;
import db.com.accountservice.entity.User;
import db.com.accountservice.repository.AccountRepository;
import db.com.accountservice.repository.UserRepository;
import db.com.accountservice.vo.error.AccountNotFoundException;
import db.com.accountservice.vo.error.UserNotFoundException;
import db.com.accountservice.vo.request.BalanceInquiryRequest;
import db.com.accountservice.vo.request.CreateAccountRequest;
import db.com.accountservice.vo.request.CreateUserRequest;
import db.com.accountservice.vo.request.CreditBalanceRequest;
import db.com.accountservice.vo.request.UserDetailsRequest;
import db.com.accountservice.vo.request.WithdrawBalanceRequest;
import db.com.accountservice.vo.response.BalanceInquiryResponse;
import db.com.accountservice.vo.response.CreateAccountResponse;
import db.com.accountservice.vo.response.CreateUserResponse;
import db.com.accountservice.vo.response.CreditBalanceResponse;
import db.com.accountservice.vo.response.UserDetailsResponse;
import db.com.accountservice.vo.response.WithdrawBalanceResponse;

@Service
public class AccountServiceImpl implements AccountService{
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {
		logger.debug("createAccountRequest: {}" ,createAccountRequest);
		Account account = accountRepository.save(Account.builder()
				.user_id(createAccountRequest.getUserId())
				.amount(createAccountRequest.getAmount())
				.build());
		CreateAccountResponse createAccountResponse =
				new CreateAccountResponse(account.getId(), account.getUser_id(), account.getAmount());
		logger.info("Account created...");
		logger.debug("createAccountResponse: {}" ,createAccountResponse);
		return createAccountResponse;
	}
	
	@Override
	public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
		logger.debug("createUserRequest: {}" ,createUserRequest);
		User user = userRepository.save(
						User.builder().name(createUserRequest.getName())
						.username(createUserRequest.getUsername())
						.email(createUserRequest.getEmail())
						.password(createUserRequest.getPassword())
						.phonenumber(createUserRequest.getPhonenumber())
						.build());
		CreateUserResponse createUserResponse =  new CreateUserResponse(user.getId(), user.getName(),user.getUsername(),user.getPassword(),
				user.getEmail(),user.getPhonenumber());;
		logger.info("User Created...");
		logger.debug("createUserResponse: {}",createUserResponse);
		return createUserResponse;
	}
	
	@Override
	@Transactional
	public WithdrawBalanceResponse withdrawBalance(WithdrawBalanceRequest  withdrawBalanceRequest) {
		logger.debug("withdrawBalanceRequest: {}",withdrawBalanceRequest);
		Account account = getAccount(withdrawBalanceRequest.getAccountnumber());
		double withdrawalAmount = withdrawBalanceRequest.getWithdrawalAmount();
		WithdrawBalanceResponse withdrawBalanceResponse = withdrawBalance(account, withdrawalAmount );
		logger.debug("withdrawBalanceResponse: {}",withdrawBalanceResponse);
		return withdrawBalanceResponse;

	}

	@Transactional
	protected boolean isWithdrable (double  accBalance, double withdrawBalance) {
		return (accBalance > withdrawBalance); 
	}
	
	private WithdrawBalanceResponse getWithdrawBalanceResponse(boolean status, double amount, long accountNumber) {
		return new WithdrawBalanceResponse(accountNumber, status, amount);
		
	}
	@Transactional
	protected  WithdrawBalanceResponse withdrawBalance(Account account, double withdrawalAmount) {
		boolean blnRes =false; 
		double newAmount =account.getAmount();
		if(isWithdrable(newAmount, withdrawalAmount)) {
			newAmount-=withdrawalAmount;
			account.setAmount(newAmount);
			accountRepository.save(account);
			blnRes = true;
		}else {
			logger.warn("Not enough balance for withdrawal");
		}
		return getWithdrawBalanceResponse(blnRes,newAmount,account.getId() );
	}

	@Override
	public BalanceInquiryResponse getBalance(BalanceInquiryRequest balanceInquiryRequest) {
		logger.debug("balanceInquiryRequest: {}",balanceInquiryRequest);
		Account account = getAccount(balanceInquiryRequest.getAccountNumber());
		BalanceInquiryResponse balanceInquiryResponse = new BalanceInquiryResponse();
		balanceInquiryResponse.setAccountNumber(account.getId());
		balanceInquiryResponse.setAmount(account.getAmount());
		logger.info("balance retrieved...");
		logger.debug("balanceInquiryResponse: {}",balanceInquiryResponse);
		return balanceInquiryResponse;
		
	}

	@Override
	@Transactional
	public CreditBalanceResponse creditBalance(CreditBalanceRequest creditBalanceRequest) {
		logger.debug("creditBalanceRequest: {}",creditBalanceRequest);
		Account account = getAccount(creditBalanceRequest.getAccountNumber());
		CreditBalanceResponse creditBalanceResponse = new CreditBalanceResponse();
		account.setAmount(account.getAmount()+creditBalanceRequest.getCreditAmount());
		accountRepository.save(account);
		creditBalanceResponse.setAccountNumber(creditBalanceRequest.getAccountNumber());
		creditBalanceResponse.setNewAmount(account.getAmount());
		creditBalanceResponse.setStatus(true);
		logger.info("Balance credited to the account...");
		logger.debug("creditBalanceResponse: {}",creditBalanceResponse);
		return creditBalanceResponse;
	}
	
	@Transactional
    protected Account getAccount(long accountNumber) {
    	Optional<Account> targetAccount = accountRepository.findById(accountNumber);
    	Account account=null;
    	if(targetAccount.isPresent()) {
    	   account = targetAccount.get();
    	}else {
    		logger.error("Account Not Found");
    		throw new AccountNotFoundException("Account# "+accountNumber +" Not Found");
    	}
    	return account;
    }
	
    public UserDetailsResponse getUserDetailsForAccount(UserDetailsRequest userDetailsRequest) {
    	Account account = getAccount(userDetailsRequest.getAccountNumber());
    	UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
    	Optional<User> user = userRepository.findById(account.getUser_id());
    	if(user.isPresent()) {
    		userDetailsResponse.setEmail(user.get().getEmail());
    		userDetailsResponse.setPhonenumber(user.get().getPhonenumber());
    	}else {
    		logger.error("User Not Found");
    		throw new UserNotFoundException("User# " +account.getUser_id() +" Not Found"); 
    	}
    	return userDetailsResponse;
    }

	
	

}
