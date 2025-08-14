package com.usk.bank.service;

import com.usk.bank.dto.*;
import com.usk.bank.entity.BankAccount;
import com.usk.bank.entity.BankTransaction;
import com.usk.bank.entity.TransactionType;
import com.usk.bank.exception.InvalidTransactionException;
import com.usk.bank.repository.BankAccountRepository;
import com.usk.bank.repository.BankTransactionRepository;
import com.usk.bank.serviceImpl.BankAccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankTransactionRepository bankTransactionRepository;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    @Test
    void testRegisterAccount_Success() {
        // Arrange
        AccountRegistrationRequest request = new AccountRegistrationRequest();
        request.setAccountHolderName("John Doe");
        request.setEmail("john@example.com");
        request.setMobileNo(9876543210L);

        when(bankAccountRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(bankAccountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(bankTransactionRepository.existsByTransactionId(anyString())).thenReturn(false);

        ArgumentCaptor<BankAccount> accountCaptor = ArgumentCaptor.forClass(BankAccount.class);
        ArgumentCaptor<BankTransaction> transactionCaptor = ArgumentCaptor.forClass(BankTransaction.class);

        BankAccount savedAccount = new BankAccount("1234567890", "John Doe", "john@example.com", 9876543210L, 10000.00);
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(savedAccount);

        // Act
        AccountResponse response = bankAccountService.registerAccount(request);

        // Assert
        assertNotNull(response);
        assertEquals("1234567890", response.getAccountNumber());
        verify(bankAccountRepository).save(accountCaptor.capture());
        verify(bankTransactionRepository).save(transactionCaptor.capture());

        BankAccount capturedAccount = accountCaptor.getValue();
        assertEquals("John Doe", capturedAccount.getAccountHolderName());
        assertEquals("john@example.com", capturedAccount.getEmail());
        assertEquals(10000.00, capturedAccount.getBalance());

        BankTransaction capturedTransaction = transactionCaptor.getValue();
        assertEquals(TransactionType.CREDIT, capturedTransaction.getType());
        assertEquals(10000.00, capturedTransaction.getAmount());
    }

    @Test
    void testRegisterAccount_EmailAlreadyExists() {
        // Arrange
        AccountRegistrationRequest request = new AccountRegistrationRequest();
        request.setEmail("existing@example.com");

        when(bankAccountRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidTransactionException.class, () -> bankAccountService.registerAccount(request));
        verify(bankAccountRepository, never()).save(any());
        verify(bankTransactionRepository, never()).save(any());
    }

    @Test
    void testDebitAccount_Success() {
        // Arrange
        String accountNo = "1234567890";
        double debitAmount = 5000.00;
        double initialBalance = 15000.00;

        BankAccount account = new BankAccount(accountNo, "John Doe", "john@example.com", 9876543210L, initialBalance);
        when(bankAccountRepository.findByAccountNumber(accountNo)).thenReturn(Optional.of(account));
        when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bankTransactionRepository.existsByTransactionId(anyString())).thenReturn(false);

        DebitCreditResponse response = bankAccountService.debitAccount(accountNo, debitAmount);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Debit successful", response.getMessage());
        assertEquals(accountNo, response.getAccountNo());
        assertEquals(initialBalance - debitAmount, response.getNewBalance(), 0.001);
        assertNotNull(response.getTransactionId());

        verify(bankAccountRepository).save(any(BankAccount.class));
        verify(bankTransactionRepository).save(any(BankTransaction.class));
    }

    @Test
    void testCreditAccount_Success() {
        // Arrange
        String accountNo = "1234567890";
        double creditAmount = 5000.00;
        double initialBalance = 15000.00;

        BankAccount account = new BankAccount(accountNo, "John Doe", "john@example.com", 9876543210L, initialBalance);
        when(bankAccountRepository.findByAccountNumber(accountNo)).thenReturn(Optional.of(account));
        when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bankTransactionRepository.existsByTransactionId(anyString())).thenReturn(false);

        DebitCreditResponse response = bankAccountService.creditAccount(accountNo, creditAmount);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Credit successful", response.getMessage());
        assertEquals(accountNo, response.getAccountNo());
        assertEquals(initialBalance + creditAmount, response.getNewBalance(), 0.001);
        assertNotNull(response.getTransactionId());

        verify(bankAccountRepository).save(any(BankAccount.class));
        verify(bankTransactionRepository).save(any(BankTransaction.class));
    }

    @Test
    void testTransferFunds_Success() {
        // Arrange
        String fromAccountNo = "1111111111";
        String toAccountNo = "2222222222";
        double transferAmount = 2000.00;

        BankAccount fromAccount = new BankAccount(fromAccountNo, "Alice", "alice@example.com", 9999999999L, 10000.00);
        BankAccount toAccount = new BankAccount(toAccountNo, "Bob", "bob@example.com", 8888888888L, 5000.00);

        FundTransferRequest request = new FundTransferRequest();
        request.setFromAccountNo(fromAccountNo);
        request.setToAccountNo(toAccountNo);
        request.setAmount(transferAmount);

        when(bankAccountRepository.findByAccountNumber(fromAccountNo)).thenReturn(Optional.of(fromAccount));
        when(bankAccountRepository.findByAccountNumber(toAccountNo)).thenReturn(Optional.of(toAccount));
        when(bankTransactionRepository.existsByTransactionId(anyString())).thenReturn(false);
        when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        AccountResponse response = bankAccountService.transferFunds(request);

        // Assert
        assertNotNull(response);
        assertEquals(fromAccountNo, response.getAccountNumber());
        assertEquals(fromAccount.getBalance(), 8000.00, 0.001); // 10000 - 2000

        verify(bankAccountRepository, times(2)).save(any(BankAccount.class));
        verify(bankTransactionRepository, times(2)).save(any(BankTransaction.class));
    }
}
