package com.usk.bank.dto;

public class TransactionRequest {
    private String accountNo;
    private double amount;

    public TransactionRequest() {
    }

    public TransactionRequest(String accountNo, double amount) {
        this.accountNo= accountNo;
        this.amount = amount;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
