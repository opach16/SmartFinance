package com.konrad.smartfinance.exception;

public class CryptoTransactionException extends Exception {

    public static final String NOT_FOUND = "Transaction not found";

    public CryptoTransactionException(String message) {
        super(message);
    }
}
