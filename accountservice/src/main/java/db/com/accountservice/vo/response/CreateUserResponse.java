package db.com.accountservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponse {
	 private long userId;
	 private String name;
	 private String username;
	 private String password;
	 private String email;
	 private String phonenumber;
}
