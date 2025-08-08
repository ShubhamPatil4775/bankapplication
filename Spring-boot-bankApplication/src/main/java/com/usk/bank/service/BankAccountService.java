package com.usk.bank.service;

import com.usk.bank.dto.*;

import java.util.List;

public interface BankAccountService {
    AccountResponse registerAccount(AccountRegistrationRequest request);
    DebitCreditResponse debitAccount(String accountNo,double amount);
    DebitCreditResponse creditAccount(String accountNo,double amount);
    AccountResponse transferFunds(FundTransferRequest request);
    List<TransactionStatementResponse> getAccountStatement(TransactionStatementRequest request);
    AccountResponse getAccountDetails(String accountNo);
}
