package db.com.transactionservice.service;

import java.time.LocalDateTime;

import db.com.transactionservice.vo.request.TransactionRequest;
import db.com.transactionservice.vo.response.TransactionHistoryResponse;
import db.com.transactionservice.vo.response.TransactionResponse;

public interface TransactionService {
	public TransactionResponse transact(TransactionRequest transactionServiceRequest);
	public TransactionHistoryResponse getTransactionHistory(long account, LocalDateTime fromDate, LocalDateTime toDate);
//	public void sendEmail(EmailRequest message) ;
}
