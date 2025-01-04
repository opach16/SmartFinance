package com.konrad.smartfinance.exception;

public class AccountException extends Exception {

    public static final String NOT_FOUND = "Account not found";

    public AccountException(String message) {
        super(message);
    }
}
