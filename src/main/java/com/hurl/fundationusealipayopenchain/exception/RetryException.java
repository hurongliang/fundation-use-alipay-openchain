package com.hurl.fundationusealipayopenchain.exception;

public class RetryException extends RuntimeException {
    public RetryException(String message) {
        super(message);
    }

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public static RetryException create(String message) {
        return new RetryException(message);
    }

    public static RetryException create(String message, Throwable cause) {
        return new RetryException(message, cause);
    }
}