package db.com.transactionservice.service;

public interface AuditDataService {

	public void enterTransactionAuditData(long account, double amount, String transactionType);
}
