package com.usk.bank.dto;

public class AccountRegistrationRequest {
    private String accountHolderName;
    private String email;
    private Long mobileNo;

    public AccountRegistrationRequest() {
    }

    public AccountRegistrationRequest(String accountHolderName, String email, Long mobileNo) {
        this.accountHolderName = accountHolderName;
        this.email = email;
        this.mobileNo = mobileNo;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Long mobileNo) {
        this.mobileNo = mobileNo;
    }
}
