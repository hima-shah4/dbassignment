package db.com.transactionservice.vo.response;

import lombok.Data;

@Data
public class DebitBalanceResponse {
	private long accountNumber;
	private boolean status;
	private double newAmount;
}
