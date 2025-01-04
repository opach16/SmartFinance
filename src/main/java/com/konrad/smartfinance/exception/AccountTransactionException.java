package com.konrad.smartfinance.exception;

public class AccountTransactionException extends Exception {

    public static final String NOT_FOUND = "Transaction not found";

    public AccountTransactionException(String message) {
        super(message);
    }
}
