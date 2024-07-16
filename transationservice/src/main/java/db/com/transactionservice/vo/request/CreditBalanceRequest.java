package db.com.transactionservice.vo.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class CreditBalanceRequest {
	@Min(value = 1, message = "ID must be greater than or equal to 1")
	private long accountNumber;
	
	@Min(value = 0, message = "Amount must be greater than or equal to 0")
	private double creditAmount;
}