package db.com.accountservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
	 private String name;
	 private String username;
	 private String password;
	 private String email;
	 private String phonenumber;
}
