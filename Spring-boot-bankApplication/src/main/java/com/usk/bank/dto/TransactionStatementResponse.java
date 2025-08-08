package com.usk.bank.dto;

import com.usk.bank.entity.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class TransactionStatementResponse {
    private String transactionId;
    private String accountNo;
    private TransactionType type;
    private double amount;
    private Date transactionDate;
    private String relatedAccount;

    public TransactionStatementResponse() {
    }

    public TransactionStatementResponse(String transactionId, String accountNo, TransactionType type, double amount, Date transactionDate, String relatedAccount) {
        this.transactionId = transactionId;
        this.accountNo = accountNo;
        this.type = type;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.relatedAccount = relatedAccount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
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
}

