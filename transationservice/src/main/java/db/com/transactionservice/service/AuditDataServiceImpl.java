package db.com.transactionservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import db.com.transactionservice.entity.Transaction;
import db.com.transactionservice.repository.TransactionRepository;

public class AuditDataServiceImpl implements AuditDataService {

	@Autowired
	private TransactionRepository transactionRespository;
	
	@Override
	@Async
	public void enterTransactionAuditData(long account, double amount, String transactionType) {
		Transaction transaction = Transaction.builder().account(account).amount(amount)
					.transactionType(transactionType).build();
		transactionRespository.save(transaction);
//		return true;
	}	

}
