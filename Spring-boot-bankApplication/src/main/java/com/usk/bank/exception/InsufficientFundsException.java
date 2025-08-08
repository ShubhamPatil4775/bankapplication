package com.usk.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String msg){
        super(msg);
    }
}
