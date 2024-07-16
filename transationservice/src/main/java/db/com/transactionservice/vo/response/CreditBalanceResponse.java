package db.com.transactionservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditBalanceResponse {
	private long accountNumber;
	private boolean status;
	private double newAmount;
}
