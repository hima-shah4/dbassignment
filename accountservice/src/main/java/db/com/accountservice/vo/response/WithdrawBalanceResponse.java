package db.com.accountservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WithdrawBalanceResponse {
	private long accountNumber;
	private boolean status;
	private double newAmount;
}
