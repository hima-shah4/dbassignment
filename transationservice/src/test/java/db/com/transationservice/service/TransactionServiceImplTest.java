package db.com.transationservice.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;

import db.com.transactionservice.client.AccountServiceClient;
import db.com.transactionservice.entity.Transaction;
import db.com.transactionservice.repository.TransactionRepository;
import db.com.transactionservice.service.TransactionServiceImpl;
import db.com.transactionservice.vo.request.CreditBalanceRequest;
import db.com.transactionservice.vo.request.TransactionRequest;
import db.com.transactionservice.vo.request.WithdrawBalanceRequest;
import db.com.transactionservice.vo.response.CreditBalanceResponse;
import db.com.transactionservice.vo.response.TransactionResponse;
import db.com.transactionservice.vo.response.UserDetailsResponse;
import db.com.transactionservice.vo.response.WithdrawBalanceResponse;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private AccountServiceClient accountServiceClient;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private JmsTemplate jmsTemplate;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl(accountServiceClient, transactionRepository, jmsTemplate,"msg-queu");
    }

    @Test
    void testTransact_Success() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(100.0);
        request.setAccountFrom(12345);
        request.setAccountTo(67890);

        when(accountServiceClient.withdrawBalance(any(WithdrawBalanceRequest.class)))
                .thenReturn(ResponseEntity.ok(WithdrawBalanceResponse.builder().status(true).build()));
        when(accountServiceClient.creditBalance(any(CreditBalanceRequest.class)))
                .thenReturn( ResponseEntity.ok(CreditBalanceResponse.builder().status(true).build()));

        TransactionResponse response = transactionService.transact(request);

        assertTrue(response.isStatus());
        assertEquals("Transaction is successful", response.getMessage());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(jmsTemplate, times(2)).convertAndSend(anyString(), anyString());
    }

    @Test
    void testTransact_WithdrawalFailure() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(100.0);
        request.setAccountFrom(1L);
        request.setAccountTo(2L);

        when(accountServiceClient.withdrawBalance(any(WithdrawBalanceRequest.class)))
                .thenReturn(ResponseEntity.ok(WithdrawBalanceResponse.builder().status(false).build()));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
        when(accountServiceClient.getUserDetails(any(Long.class))).thenReturn(ResponseEntity.ok(new UserDetailsResponse("abc","123")));
        Mockito.doNothing().when(jmsTemplate).convertAndSend(any(String.class), any(Object.class));
        TransactionResponse response = transactionService.transact(request);
        assertFalse(response.isStatus());
        assertEquals("Withdrawal from the account Failed", response.getMessage());
    }

    @Test
    void testTransact_CreditFailure() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(100.0);
        request.setAccountFrom(12345);
        request.setAccountTo(67890);

        when(accountServiceClient.withdrawBalance(any(WithdrawBalanceRequest.class)))
                .thenReturn(ResponseEntity.ok(WithdrawBalanceResponse.builder().status(true).build()));
        when(accountServiceClient.creditBalance(any(CreditBalanceRequest.class)))
                .thenReturn( ResponseEntity.ok(CreditBalanceResponse.builder().status(false).build()));

        TransactionResponse response = transactionService.transact(request);

        assertFalse(response.isStatus());
        assertEquals("Credit to the account Failed", response.getMessage());
//        verify(transactionRepository, times(1)).save(any(Transaction.class));
//        verify(jmsTemplate, times(2)).convertAndSend(anyString(), anyString());
    }

    @Test
    void testCreditBalance_Success() {
        when(accountServiceClient.creditBalance(any(CreditBalanceRequest.class)))
                .thenReturn( ResponseEntity.ok(CreditBalanceResponse.builder().status(true).build()));

        boolean result = transactionService.creditBalance(67890, 100.0);

        assertTrue(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), anyString());
    }

    @Test
    void testCreditBalance_Failure() {
        when(accountServiceClient.creditBalance(any(CreditBalanceRequest.class)))
                .thenReturn( ResponseEntity.ok(CreditBalanceResponse.builder().status(false).build()));

        boolean result = transactionService.creditBalance(1, 100.0);

        assertFalse(result);
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(jmsTemplate, never()).convertAndSend(anyString(), anyString());
    }

    @Test
    void testWithdrawBalance_Success() {
        when(accountServiceClient.withdrawBalance(any(WithdrawBalanceRequest.class)))
                .thenReturn(ResponseEntity.ok(WithdrawBalanceResponse.builder().status(true).build()));

        boolean result = transactionService.withrawBalance(12345, 100.0);

        assertTrue(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), anyString());
    }

    @Test
    void testWithdrawBalance_Failure() {
        when(accountServiceClient.withdrawBalance(any(WithdrawBalanceRequest.class)))
                .thenReturn(ResponseEntity.ok(WithdrawBalanceResponse.builder().status(false).build()));

        boolean result = transactionService.withrawBalance(12345, 100.0);

        assertFalse(result);
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(jmsTemplate, never()).convertAndSend(anyString(), anyString());
    }
}