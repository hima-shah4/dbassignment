package db.com.transactionservice.vo.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsRequest {
	@Min(value = 1, message = "ID must be greater than or equal to 1")
	private long accountNumber;
}
