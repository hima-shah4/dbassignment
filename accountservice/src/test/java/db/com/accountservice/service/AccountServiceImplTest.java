package db.com.accountservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import db.com.accountservice.entity.Account;
import db.com.accountservice.entity.User;
import db.com.accountservice.repository.AccountRepository;
import db.com.accountservice.repository.UserRepository;
import db.com.accountservice.vo.error.AccountNotFoundException;
import db.com.accountservice.vo.request.BalanceInquiryRequest;
import db.com.accountservice.vo.request.CreateAccountRequest;
import db.com.accountservice.vo.request.CreateUserRequest;
import db.com.accountservice.vo.request.CreditBalanceRequest;
import db.com.accountservice.vo.request.WithdrawBalanceRequest;
import db.com.accountservice.vo.response.BalanceInquiryResponse;
import db.com.accountservice.vo.response.CreateAccountResponse;
import db.com.accountservice.vo.response.CreateUserResponse;
import db.com.accountservice.vo.response.CreditBalanceResponse;
import db.com.accountservice.vo.response.WithdrawBalanceResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setUserId(1L);
        request.setAmount(100.0);

        Account account = Account.builder()
                .id(1L)
                .user_id(1L)
                .amount(100.0)
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        CreateAccountResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals(1L, response.getAccountNumber());
        assertEquals(1L, response.getUser_id());
        assertEquals(100.0, response.getBalance());

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("abc abc");
        request.setUsername("abc");
        request.setEmail("abc@gmail.com");
        request.setPassword("password");
        request.setPhonenumber("1234567890");

        User user = User.builder()
                .id(1L)
                .name("abc abc")
                .username("abc")
                .email("abc@gmail.com")
                .password("password")
                .phonenumber("1234567890")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        CreateUserResponse response = accountService.createUser(request);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals("abc abc", response.getName());
        assertEquals("abc", response.getUsername());
        assertEquals("abc@gmail.com", response.getEmail());
        assertEquals("1234567890", response.getPhonenumber());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testWithdrawBalance() {
        WithdrawBalanceRequest request = new WithdrawBalanceRequest();
        request.setAccountnumber(1L);
        request.setWithdrawalAmount(50.0);

        Account account = Account.builder()
                .id(1L)
                .user_id(1L)
                .amount(100.0)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        WithdrawBalanceResponse response = accountService.withdrawBalance(request);

        assertNotNull(response);
        assertTrue(response.isStatus());
        assertEquals(50.0, response.getNewAmount());
        assertEquals(1L, response.getAccountNumber());

        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testWithdrawBalanceInsufficientFunds() {
        WithdrawBalanceRequest request = new WithdrawBalanceRequest();
        request.setAccountnumber(1L);
        request.setWithdrawalAmount(150.0);

        Account account = Account.builder()
                .id(1L)
                .user_id(1L)
                .amount(100.0)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        WithdrawBalanceResponse response = accountService.withdrawBalance(request);

        assertNotNull(response);
        assertFalse(response.isStatus());
        assertEquals(100.0, response.getNewAmount());
        assertEquals(1L, response.getAccountNumber());

        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testGetBalance() {
        BalanceInquiryRequest request = new BalanceInquiryRequest();
        request.setAccountNumber(1L);

        Account account = Account.builder()
                .id(1L)
                .user_id(1L)
                .amount(100.0)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        BalanceInquiryResponse response = accountService.getBalance(request);

        assertNotNull(response);
        assertEquals(1L, response.getAccountNumber());
        assertEquals(100.0, response.getAmount());

        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testCreditBalance() {
        CreditBalanceRequest request = new CreditBalanceRequest();
        request.setAccountNumber(1L);
        request.setCreditAmount(50.0);

        Account account = Account.builder()
                .id(1L)
                .user_id(1L)
                .amount(100.0)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        CreditBalanceResponse response = accountService.creditBalance(request);

        assertNotNull(response);
        assertTrue(response.isStatus());
        assertEquals(150.0, response.getNewAmount());
        assertEquals(1L, response.getAccountNumber());

        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testGetAccount_NotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccount(1L);
        });

        assertEquals("Account# 1 Not Found", exception.getMessage());

        verify(accountRepository, times(1)).findById(1L);
    }
}