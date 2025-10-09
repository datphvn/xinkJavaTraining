package com.library.domain.exception;

public class LibraryException extends Exception {
    private final String errorCode;

    public LibraryException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public LibraryException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
