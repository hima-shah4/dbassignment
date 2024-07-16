package db.com.accountservice.service;

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

public interface AccountService {

	public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) ;
	public CreateUserResponse createUser(CreateUserRequest createUserRequest);
	public BalanceInquiryResponse getBalance(BalanceInquiryRequest balanceInquiryRequest);
	public WithdrawBalanceResponse withdrawBalance(WithdrawBalanceRequest withdrawBalanceRequest);
	public CreditBalanceResponse creditBalance(CreditBalanceRequest creditBalanceRequest);
	public UserDetailsResponse getUserDetailsForAccount(UserDetailsRequest userDetailsRequest);
}
