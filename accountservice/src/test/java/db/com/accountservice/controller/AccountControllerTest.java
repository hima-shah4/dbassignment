package db.com.accountservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import db.com.accountservice.service.AccountService;
import db.com.accountservice.vo.request.BalanceInquiryRequest;
import db.com.accountservice.vo.request.CreateAccountRequest;
import db.com.accountservice.vo.request.CreditBalanceRequest;
import db.com.accountservice.vo.request.UserDetailsRequest;
import db.com.accountservice.vo.request.WithdrawBalanceRequest;
import db.com.accountservice.vo.response.BalanceInquiryResponse;
import db.com.accountservice.vo.response.CreateAccountResponse;
import db.com.accountservice.vo.response.CreditBalanceResponse;
import db.com.accountservice.vo.response.UserDetailsResponse;
import db.com.accountservice.vo.response.WithdrawBalanceResponse;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AccountController(accountService)).build();
    }

    @Test
    void testCreateAccount() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setUserId(1L);
        request.setAmount(100.0);

        CreateAccountResponse response = new CreateAccountResponse();
        response.setAccountNumber(1L);
        response.setUser_id(1L);
        response.setBalance(100.0);

        when(accountService.createAccount(any(CreateAccountRequest.class))).thenReturn(response);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1, \"amount\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.amount").value(100.0));
    }

    @Test
    void testGetUserDetails() throws Exception {
        UserDetailsResponse response = new UserDetailsResponse();
        response.setEmail("test@example.com");
        response.setPhonenumber("1234567890");

        when(accountService.getUserDetailsForAccount(any(UserDetailsRequest.class))).thenReturn(response);

        mockMvc.perform(get("/accounts/1/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phonenumber").value("1234567890"));
    }

    @Test
    void testGetAccountBalance() throws Exception {
        BalanceInquiryResponse response = new BalanceInquiryResponse();
        response.setAccountNumber(1L);
        response.setAmount(100.0);

        when(accountService.getBalance(any(BalanceInquiryRequest.class))).thenReturn(response);

        mockMvc.perform(get("/accounts/1/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(1L))
                .andExpect(jsonPath("$.amount").value(100.0));
    }

    @Test
    void testWithdrawAmount() throws Exception {
        WithdrawBalanceRequest request = new WithdrawBalanceRequest();
        request.setAccountnumber(1L);
        request.setWithdrawalAmount(50.0);

        WithdrawBalanceResponse response = new WithdrawBalanceResponse();
        response.setAccountNumber(1L);
        response.setStatus(true);
        response.setNewAmount(50.0);

        when(accountService.withdrawBalance(any(WithdrawBalanceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/accounts/balance/debit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":1, \"amount\":50.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(1L))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.amount").value(50.0));
    }

    @Test
    void testCreditBalance() throws Exception {
        CreditBalanceRequest request = new CreditBalanceRequest();
        request.setAccountNumber(1L);
        request.setCreditAmount(50.0);

        CreditBalanceResponse response = new CreditBalanceResponse();
        response.setAccountNumber(1L);
        response.setNewAmount(150.0);
        response.setStatus(true);

        when(accountService.creditBalance(any(CreditBalanceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/accounts/balance/credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":1, \"creditAmount\":50.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(1L))
                .andExpect(jsonPath("$.newAmount").value(150.0))
                .andExpect(jsonPath("$.status").value(true));
    }
}
