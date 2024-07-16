package db.com.transactionservice.vo.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserDetailsNotFound  extends RuntimeException{
	private static final long serialVersionUID = 1L;
    public UserDetailsNotFound(String message) {
    	super(message);
    }
}
