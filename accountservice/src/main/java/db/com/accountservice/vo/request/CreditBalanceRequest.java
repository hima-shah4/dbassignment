package db.com.accountservice.vo.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreditBalanceRequest {
	@Min(value = 1, message = "ID must be greater than or equal to 1")
	private long accountNumber;
	
	@Min(value = 0, message = "Amount must be greater than or equal to 0")
	private double creditAmount;
}