package db.com.transactionservice.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import db.com.transactionservice.vo.request.CreditBalanceRequest;
import db.com.transactionservice.vo.request.WithdrawBalanceRequest;
import db.com.transactionservice.vo.response.CreditBalanceResponse;
import db.com.transactionservice.vo.response.UserDetailsResponse;
import db.com.transactionservice.vo.response.WithdrawBalanceResponse;

@HttpExchange("/accounts")
public interface AccountServiceClient {

	@PostExchange("/balance/debit")
	public ResponseEntity<WithdrawBalanceResponse> withdrawBalance(@RequestBody WithdrawBalanceRequest withdrawBalanceRequest);
	
	@PostExchange("/balance/credit")
	public ResponseEntity<CreditBalanceResponse> creditBalance(@RequestBody CreditBalanceRequest creditBalanceRequest);
	
	@GetExchange("/{accountNumber}/user")
	public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable long accountNumber);
	
}
