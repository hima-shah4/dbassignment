package db.com.transactionservice.vo.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransactionFailureException extends RuntimeException{
	private static final long serialVersionUID = 1L;
    public TransactionFailureException(String message) {
    	super(message);
    }
}
