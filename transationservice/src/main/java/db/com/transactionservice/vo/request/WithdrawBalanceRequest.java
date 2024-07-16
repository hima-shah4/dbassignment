package db.com.transactionservice.vo.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WithdrawBalanceRequest {
	@Min(value = 1, message = "ID must be greater than or equal to 1")
	private long accountnumber;
	
	@Min(value = 0, message = "ID must be greater than or equal to 0")
	private double withdrawalAmount;
}
