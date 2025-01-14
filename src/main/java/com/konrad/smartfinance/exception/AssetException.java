package com.konrad.smartfinance.exception;

public class AssetException extends Exception {

    public static final String NOT_FOUND = "Asset not found";

    public AssetException(String message) {
        super(message);
    }
}
