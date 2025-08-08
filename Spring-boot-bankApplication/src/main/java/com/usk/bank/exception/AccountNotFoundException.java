package com.usk.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String msg){
        super(msg);
    }
}
