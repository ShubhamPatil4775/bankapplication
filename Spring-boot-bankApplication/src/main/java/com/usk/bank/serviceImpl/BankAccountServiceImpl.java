package com.usk.bank.serviceImpl;

import com.usk.bank.dto.*;
import com.usk.bank.entity.BankAccount;
import com.usk.bank.entity.BankTransaction;
import com.usk.bank.entity.TransactionType;
import com.usk.bank.exception.AccountNotFoundException;
import com.usk.bank.exception.InsufficientFundsException;
import com.usk.bank.exception.InvalidTransactionException;
import com.usk.bank.repository.BankAccountRepository;
import com.usk.bank.repository.BankTransactionRepository;
import com.usk.bank.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    private static final double MIN_BALANCE= 10000.00;

    public String generateAccountNo(){
        String accountNo;
        Random random = new Random();
        do {
            accountNo= String.format("%010d",random.nextInt(1_000_000_000));
        }while (bankAccountRepository.existsByAccountNumber(accountNo));
            return accountNo;
    }
    public String generateTransactionId(){
        String transactionId;
        Random random = new Random();
        do{
            transactionId= String.valueOf(System.currentTimeMillis()+random.nextInt(99999));
        }while (bankTransactionRepository.existsByTransactionId(transactionId));
        return transactionId;
    }
    @Override
    public AccountResponse registerAccount(AccountRegistrationRequest request) {
        if (bankAccountRepository.existsByEmail(request.getEmail())){
            throw new InvalidTransactionException("Account with this email id already exists");
        }
        String newAccountNo= generateAccountNo();
        BankAccount bankAccount = new BankAccount(
                newAccountNo,
                request.getAccountHolderName(),
                request.getEmail(),
                request.getMobileNo(),
                MIN_BALANCE
        );
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        BankTransaction bankTransaction = new BankTransaction(
                savedBankAccount.getAccountNumber(),
                TransactionType.CREDIT,
                MIN_BALANCE,
                new Date(),
                generateTransactionId()
        );
        bankTransactionRepository.save(bankTransaction);

        return mapToAccountResponse(savedBankAccount);
    }

    @Override
    public DebitCreditResponse debitAccount(String accountNo, double amount) {
        if (amount <0){
            throw new InvalidTransactionException("Debit amount must be positive");
        }
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNo)
                        .orElseThrow(()->new AccountNotFoundException("Account not found with account NO"));
        if (account.getBalance() <amount){
            throw new InsufficientFundsException("Insufficient funds in accounts");
        }
        account.setBalance(account.getBalance() - amount);
        BankAccount account1 = bankAccountRepository.save(account);
        String transactionId= generateTransactionId();
        BankTransaction bankTransaction = new BankTransaction(
                accountNo,
                TransactionType.DEBIT,
                amount,
                new Date(),
                transactionId
        );
        bankTransactionRepository.save(bankTransaction);
        return new DebitCreditResponse(true,"Debit successful",account1.getAccountNumber(),account1.getBalance(),transactionId);
    }

    @Override
    public DebitCreditResponse creditAccount(String accountNo, double amount) {
        if (amount <=0){
            throw new InvalidTransactionException("Amount must be more than 0");
        }
        BankAccount account= bankAccountRepository.findByAccountNumber(accountNo)
                .orElseThrow(()->
                        new AccountNotFoundException("Account not found with this Account no"));
        account.setBalance(account.getBalance()+ amount);
        BankAccount updatedAccount =bankAccountRepository.save(account);
        String transactionId= generateTransactionId();

        BankTransaction bankTransaction = new BankTransaction(
                accountNo,
                TransactionType.CREDIT,
                amount,
                new Date(),
                transactionId
        );
        bankTransactionRepository.save(bankTransaction);
        return new DebitCreditResponse(true,"Credit successful",updatedAccount.getAccountNumber(),updatedAccount.getBalance(),transactionId);
    }

    @Override
    public AccountResponse transferFunds(FundTransferRequest request) {
        if (request.getAmount() <=0){
            throw new InvalidTransactionException("amount must be positive for tansfer");
        }
        BankAccount fromAccount = bankAccountRepository.findByAccountNumber(request.getFromAccountNo())
                .orElseThrow(()->
                        new AccountNotFoundException("Account not found"));
        BankAccount toAccount = bankAccountRepository.findByAccountNumber(request.getToAccountNo())
                .orElseThrow(()->
                        new AccountNotFoundException("Account not found"));
        if (fromAccount.getBalance() < request.getAmount()){
            throw new InsufficientFundsException("Insufficient funds in account to transfer");
        }
        fromAccount.setBalance(fromAccount.getBalance() - request.getAmount());
        toAccount.setBalance(toAccount.getBalance() + request.getAmount());
        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
        String transactionId= generateTransactionId();
        BankTransaction senderTxt = new BankTransaction(
                fromAccount.getAccountNumber(),
                TransactionType.TRANSFER_OUT,
                request.getAmount(),
                new Date(),
                toAccount.getAccountNumber(),
                transactionId
        );
        BankTransaction receiverTxt = new BankTransaction(
                toAccount.getAccountNumber(),
                TransactionType.TRANSFER_IN,
                request.getAmount(),
                new Date(),
                fromAccount.getAccountNumber(),
                transactionId
        );
        bankTransactionRepository.save(senderTxt);
        bankTransactionRepository.save(receiverTxt);
        return mapToAccountResponse(fromAccount);
    }

    @Override
    public List<TransactionStatementResponse> getAccountStatement(TransactionStatementRequest request) {
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(request.getAccountNo())
                .orElseThrow(()->
                        new AccountNotFoundException("Account not found"));
        YearMonth yearMonth= YearMonth.of(request.getYear(),request.getMonth());
        Date startDate = Date.from(yearMonth.atDay(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        Date endDate = Date.from(yearMonth.atEndOfMonth()
                .atTime(LocalTime.MAX)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        List<BankTransaction> transactions = bankTransactionRepository.findByAccountNumberAndTransactionDateBetweenOrderByTransactionDateDesc(
                request.getAccountNo(),startDate,endDate
        );

        return transactions.stream()
                .map(this::mapToTransactionStatementResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse getAccountDetails(String accountNo) {
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNo)
                .orElseThrow(()->
                        new AccountNotFoundException("Account not found"));
        return mapToAccountResponse(bankAccount);
    }

    public AccountResponse mapToAccountResponse(BankAccount account){
        return new AccountResponse(
                account.getAccountNumber(),
                account.getAccountHolderName(),
                account.getEmail(),
                account.getMobileNo(),
                account.getBalance()
        );
    }
    public TransactionStatementResponse mapToTransactionStatementResponse(BankTransaction transaction){
        return new TransactionStatementResponse(
                transaction.getTransactionId(),
                transaction.getAccountNumber(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getRelatedAccount()
        );
    }
}
