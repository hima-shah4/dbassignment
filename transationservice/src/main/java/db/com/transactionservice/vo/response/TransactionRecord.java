package db.com.transactionservice.vo.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionRecord {
	private long transactionId;
	private long accountId;
	private double amount;
	private String operationtype;
	private LocalDate createdOn;
	private LocalDate modifiedOn;
}
