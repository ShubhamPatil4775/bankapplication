package com.usk.bank.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private double amount;
    private Date transactionDate;
    private String relatedAccount;
    private String transactionId;

    public BankTransaction() {
    }

    public BankTransaction(String accountNumber,TransactionType type, double amount,Date transactionDate,String transactionId) {
        this.amount = amount;
        this.type = type;
        this.accountNumber = accountNumber;
        this.transactionDate= transactionDate;
        this.transactionId = transactionId;
    }

    public BankTransaction(String accountNumber, TransactionType type, double amount, Date transactionDate, String relatedAccount, String transactionId) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.relatedAccount = relatedAccount;
        this.transactionId = transactionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getRelatedAccount() {
        return relatedAccount;
    }

    public void setRelatedAccount(String relatedAccount) {
        this.relatedAccount = relatedAccount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "BankTransaction{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                ", relatedAccount='" + relatedAccount + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
