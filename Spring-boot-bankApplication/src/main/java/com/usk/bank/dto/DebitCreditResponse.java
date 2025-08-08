package com.usk.bank.dto;

public class DebitCreditResponse {
    private boolean success;
    private String message;
    private String accountNo;
    private double newBalance;
    private String transactionId;

    public DebitCreditResponse() {
    }

    public DebitCreditResponse(boolean success, String message, String accountNo, double newBalance, String transactionId) {
        this.success = success;
        this.message = message;
        this.accountNo = accountNo;
        this.newBalance = newBalance;
        this.transactionId = transactionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(double newBalance) {
        this.newBalance = newBalance;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
