package com.konrad.smartfinance.exception;

public class CryptocurrencyException extends Exception {

    public static final String NOT_FOUND = "Cryptocurrency not found";

    public CryptocurrencyException(String message) {
        super(message);
    }
}
