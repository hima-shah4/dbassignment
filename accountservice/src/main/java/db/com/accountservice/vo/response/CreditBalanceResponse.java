package db.com.accountservice.vo.response;

import lombok.Data;

@Data
public class CreditBalanceResponse {
	private long accountNumber;
	private boolean status;
	private double newAmount;
}
