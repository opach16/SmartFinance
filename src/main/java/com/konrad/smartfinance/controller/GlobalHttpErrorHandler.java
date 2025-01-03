package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.exception.CryptocurrencyException;
import com.konrad.smartfinance.exception.CurrencyExeption;
import com.konrad.smartfinance.exception.CurrencyTransactionException;
import com.konrad.smartfinance.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Object> handlerUserException(UserException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyExeption.class)
    public ResponseEntity<Object> handlerCurrencyException(CurrencyExeption exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyTransactionException.class)
    public ResponseEntity<Object> handlerCurrencyTransactionException(CurrencyTransactionException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CryptocurrencyException.class)
    public ResponseEntity<Object> handlerCryptocurrencyException(CryptocurrencyException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
