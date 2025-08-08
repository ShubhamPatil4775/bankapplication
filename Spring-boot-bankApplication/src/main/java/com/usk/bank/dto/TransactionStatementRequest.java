package com.usk.bank.dto;

public class TransactionStatementRequest {
    private String accountNo;
    private int month;
    private int year;

    public TransactionStatementRequest() {
    }

    public TransactionStatementRequest(String accountNo, int month, int year) {
        this.accountNo = accountNo;
        this.month = month;
        this.year = year;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
