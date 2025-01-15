package com.konrad.smartfinance.exception;

public class DebitTransactionException extends Exception {

    public static final String NOT_FOUND = "Transaction not found";

    public DebitTransactionException(String message) {
        super(message);
    }
}
