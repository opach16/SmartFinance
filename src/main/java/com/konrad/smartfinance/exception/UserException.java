package com.konrad.smartfinance.exception;

public class UserException extends Exception {

    public static final String USER_NOT_FOUND = "User Not Found";

    public UserException(String message) {
        super(message);
    }
}
