package db.com.transactionservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TransactionResponse {
	private boolean status;
	private String message;
}
