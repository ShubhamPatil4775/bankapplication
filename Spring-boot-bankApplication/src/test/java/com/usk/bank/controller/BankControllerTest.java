package com.usk.bank.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.usk.bank.dto.*;
import com.usk.bank.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class BankControllerTest {
    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankController bankController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterAccount_Success() {
        AccountRegistrationRequest request = new AccountRegistrationRequest();
          request.setAccountHolderName("shubham patil");
          request.setEmail("shubham@gmail.com");
          request.setMobileNo(1010101010L);

        AccountResponse expectedResponse = new AccountResponse();
        expectedResponse.setAccountNumber("1011011010");
        expectedResponse.setAccountHolderName("shubham patil");
        expectedResponse.setBalance(10000.00);
        expectedResponse.setEmail("shubham@gmail.com");
        expectedResponse.setMobileNo(1010101010L);

        when(bankAccountService.registerAccount(any(AccountRegistrationRequest.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<AccountResponse> response = bankController.registerAccount(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1011011010", response.getBody().getAccountNumber());
        assertEquals("shubham patil", response.getBody().getAccountHolderName());
        assertEquals("shubham@gmail.com", response.getBody().getEmail());
        assertEquals(1010101010L, response.getBody().getMobileNo());
        assertEquals(10000.00, response.getBody().getBalance());

        verify(bankAccountService, times(1)).registerAccount(request);

    }

    @Test
    void testDebitAccount_Success() {
        TransactionRequest request = new TransactionRequest();
        request.setAccountNo("1011011010");
        request.setAmount(2000.0);

        DebitCreditResponse expectedResponse = new DebitCreditResponse();
        expectedResponse.setSuccess(true);
        expectedResponse.setMessage("Amount debited successfully");
        expectedResponse.setAccountNo("1011011010");
        expectedResponse.setNewBalance(8000.0);
        expectedResponse.setTransactionId("101010110");

        when(bankAccountService.debitAccount("1011011010", 2000.0)).thenReturn(expectedResponse);

        ResponseEntity<DebitCreditResponse> response = bankController.debitAccount(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(true,response.getBody().isSuccess());
        assertEquals("Amount debited successfully", response.getBody().getMessage());
        assertEquals("1011011010", response.getBody().getAccountNo());
        assertEquals(8000.0, response.getBody().getNewBalance());
        assertEquals("101010110",response.getBody().getTransactionId());

        verify(bankAccountService, times(1)).debitAccount("1011011010", 2000.0);
    }

    @Test
    void testCreditAccount_Success() {
        TransactionRequest request = new TransactionRequest();
        request.setAccountNo("1011011010");
        request.setAmount(2000.0);

        DebitCreditResponse expectedResponse = new DebitCreditResponse();
        expectedResponse.setSuccess(true);
        expectedResponse.setMessage("Amount credited successfully");
        expectedResponse.setAccountNo("1011011010");
        expectedResponse.setNewBalance(10000.0);
        expectedResponse.setTransactionId("101010110");

        when(bankAccountService.creditAccount("1011011010", 2000.0)).thenReturn(expectedResponse);

        ResponseEntity<DebitCreditResponse> response = bankController.creditAccount(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(true,response.getBody().isSuccess());
        assertEquals("Amount credited successfully", response.getBody().getMessage());
        assertEquals("1011011010", response.getBody().getAccountNo());
        assertEquals(10000.0, response.getBody().getNewBalance());
        assertEquals("101010110",response.getBody().getTransactionId());


        verify(bankAccountService, times(1)).creditAccount("1011011010", 2000.0);
    }

//    @Test
//    void testTransferFunds_Success() {
//        FundTransferRequest request = new FundTransferRequest();
//        request.setFromAccountNo("1010101010");
//        request.setToAccountNo("2020202020");
//        request.setAmount(1000.0);
//
//        AccountResponse expectedResponse = new AccountResponse();
//        expectedResponse.setAccountNumber("1010101010");
//        expectedResponse.setAccountHolderName("shubham patil");
//        expectedResponse.setBalance(10000.00);
//        expectedResponse.setEmail("shubham@gmail.com");
//        expectedResponse.setMobileNo(1010101010L);
//
//        when(bankAccountService.transferFunds(request)).thenReturn(expectedResponse);
//
//        ResponseEntity<?> response = bankController.creditAccount(request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("transferred the fund", response.getBody());
////        assertNotNull(response.getBody());
////        assertEquals("1010101010", response.getBody().getAccountNumber());
////        assertEquals("shubham patil", response.getBody().getAccountHolderName());
////        assertEquals("shubham@gmail.com", response.getBody().getEmail());
////        assertEquals(1010101010L, response.getBody().getMobileNo());
////        assertEquals(10000.00, response.getBody().getBalance());
//
//        verify(bankAccountService, times(1)).transferFunds(request);
//    }

    @Test
    void testGetAccountDetails_Success() {
        String accountNumber = "ACC789";

        AccountResponse expectedResponse = new AccountResponse();
        expectedResponse.setAccountNumber("1010101010");
        expectedResponse.setAccountHolderName("shubham patil");
        expectedResponse.setBalance(10000.00);
        expectedResponse.setEmail("shubham@gmail.com");
        expectedResponse.setMobileNo(1010101010L);

        when(bankAccountService.getAccountDetails(accountNumber)).thenReturn(expectedResponse);

        ResponseEntity<AccountResponse> response = bankController.getAccountDetails(accountNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1010101010", response.getBody().getAccountNumber());
        assertEquals("shubham patil", response.getBody().getAccountHolderName());
        assertEquals("shubham@gmail.com", response.getBody().getEmail());
        assertEquals(1010101010L, response.getBody().getMobileNo());
        assertEquals(10000.00, response.getBody().getBalance());

        verify(bankAccountService, times(1)).getAccountDetails(accountNumber);
    }
}