package db.com.accountservice.vo.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BalanceInquiryRequest {
	@Min(value = 1, message = "Account number must be greater than or equal to 1")
	private long accountNumber;
}
