package com.konrad.smartfinance.exception;

public class CurrencyTransactionException extends Exception {

    public static final String NOT_FOUND = "Currency transaction not found";
    public CurrencyTransactionException(String message) {
        super(message);
    }
}
