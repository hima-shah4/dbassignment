package db.com.transactionservice.vo.request;

import lombok.Data;

@Data
public class TransactionRequest {
	private long accountFrom;
	private long accountTo;
	private double amount;
	//private String currency;
}
