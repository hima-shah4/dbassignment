package db.com.accountservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import db.com.accountservice.service.AccountService;
import db.com.accountservice.vo.request.BalanceInquiryRequest;
import db.com.accountservice.vo.request.CreateAccountRequest;
import db.com.accountservice.vo.request.CreateUserRequest;
import db.com.accountservice.vo.request.CreditBalanceRequest;
import db.com.accountservice.vo.request.UserDetailsRequest;
import db.com.accountservice.vo.request.WithdrawBalanceRequest;
import db.com.accountservice.vo.response.BalanceInquiryResponse;
import db.com.accountservice.vo.response.CreateAccountResponse;
import db.com.accountservice.vo.response.CreateUserResponse;
import db.com.accountservice.vo.response.CreditBalanceResponse;
import db.com.accountservice.vo.response.UserDetailsResponse;
import db.com.accountservice.vo.response.WithdrawBalanceResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
//	@GetMapping("/{accountNumber}")
//	public Account getAccount(@PathVariable long accountNumber) {
//		return accountService.get
//	}
	
	@PostMapping
	public ResponseEntity<CreateAccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
		return ResponseEntity.ok(accountService.createAccount(createAccountRequest));
	}
	
	@PostMapping("/users")
	public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest){
		return ResponseEntity.ok(accountService.createUser(createUserRequest));
	}
	
	@GetMapping("/{accountNumber}/user")
	public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable long accountNumber) {
		UserDetailsRequest userDetailsRequest = new UserDetailsRequest();
		userDetailsRequest.setAccountNumber(accountNumber);
		return ResponseEntity.ok(accountService.getUserDetailsForAccount(userDetailsRequest));
		
	}
	
	@GetMapping("/{accountNumber}/balance")
	public ResponseEntity<BalanceInquiryResponse> getAccountBalance(@PathVariable long accountNumber) {
		BalanceInquiryRequest balanceInquiryRequest = new BalanceInquiryRequest();
		balanceInquiryRequest.setAccountNumber(accountNumber);
		return ResponseEntity.ok(accountService.getBalance(balanceInquiryRequest));
	}
	
	@PostMapping("/balance/debit")
	public ResponseEntity<WithdrawBalanceResponse> withdrawAmount(@Valid @RequestBody WithdrawBalanceRequest withdrawBalanceRequest) {
		return ResponseEntity.ok(accountService.withdrawBalance(withdrawBalanceRequest));
	}
	
	@PostMapping("/balance/credit")
	public ResponseEntity<CreditBalanceResponse> creditBalance(@Valid @RequestBody CreditBalanceRequest creditBalanceRequest) {
		return ResponseEntity.ok(accountService.creditBalance(creditBalanceRequest));		
	}
	
}
