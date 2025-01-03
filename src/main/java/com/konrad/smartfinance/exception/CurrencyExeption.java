package com.konrad.smartfinance.exception;

public class CurrencyExeption extends Exception {

    public static final String CURRENCY_NOT_FOUND = "Currency not found";
    public static final String ALREADY_EXISTS = " currency already exists";
    public CurrencyExeption(String message) {
        super(message);
    }
}
