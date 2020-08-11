package com.blackwell.exception;

public class InvalidTokenResponseException extends Exception {
    private static final long serialVersionUID = 1670205893764987891L;

    public InvalidTokenResponseException(String message) {
        super(message);
    }

    public InvalidTokenResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
