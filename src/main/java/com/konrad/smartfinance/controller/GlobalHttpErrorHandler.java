package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.exception.*;
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

    @ExceptionHandler(CryptoTransactionException.class)
    public ResponseEntity<Object> handlerCryptoTransactionException(CryptoTransactionException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<Object> handlerAccountException(AccountException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DebitTransactionException.class)
    public ResponseEntity<Object> handlerAccountTransactionException(DebitTransactionException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AssetException.class)
    public ResponseEntity<Object> handlerAssetException(AssetException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
