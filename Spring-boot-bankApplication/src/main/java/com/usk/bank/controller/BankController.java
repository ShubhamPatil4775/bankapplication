package com.usk.bank.controller;

import com.usk.bank.dto.*;
import com.usk.bank.service.BankAccountService;
import com.usk.bank.serviceImpl.BankAccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/bank")
public class BankController {
    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> registerAccount(@RequestBody AccountRegistrationRequest request){
        return new ResponseEntity<>(bankAccountService.registerAccount(request), HttpStatus.CREATED);
    }

    @PostMapping("/debit")
    public ResponseEntity<DebitCreditResponse> debitAccount(@RequestBody TransactionRequest request){
        return new ResponseEntity<>(bankAccountService.debitAccount(request.getAccountNo(),request.getAmount()),HttpStatus.OK);
    }

    @PostMapping("/credit")
    public ResponseEntity<DebitCreditResponse> creditAccount(@RequestBody TransactionRequest request){
        return new ResponseEntity<>(bankAccountService.creditAccount(request.getAccountNo(),request.getAmount()),HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<AccountResponse> creditAccount(@RequestBody FundTransferRequest request){
        return new ResponseEntity<>(bankAccountService.transferFunds(request),HttpStatus.OK);
    }

    @GetMapping("/statement/{accountNo}")
    public ResponseEntity<List<TransactionStatementResponse>> getAccountStatement(@PathVariable String accountNo,@RequestParam int month, @RequestParam int year){
        TransactionStatementRequest transactionStatementRequest = new TransactionStatementRequest(accountNo,month,year);
        List<TransactionStatementResponse> transactionStatementResponses = bankAccountService.getAccountStatement(transactionStatementRequest);
        return new ResponseEntity<>(transactionStatementResponses,HttpStatus.OK);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountDetails(@PathVariable String accountNumber){
        return new ResponseEntity<>(bankAccountService.getAccountDetails(accountNumber),HttpStatus.OK);
    }

}
