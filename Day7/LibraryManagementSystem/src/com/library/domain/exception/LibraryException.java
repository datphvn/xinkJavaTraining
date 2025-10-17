package com.library.domain.exception;

/**
 * Base exception class for all library-related exceptions.
 * Pure OOP implementation without any framework dependencies.
 */
public class LibraryException extends RuntimeException {
    private final String errorCode;

    /**
     * Constructor with error code and message.
     * @param errorCode The error code
     * @param message The error message
     */
    public LibraryException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructor with error code, message, and cause.
     * @param errorCode The error code
     * @param message The error message
     * @param cause The cause of the exception
     */
    public LibraryException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Constructor with message only (for backward compatibility).
     * @param message The error message
     */
    public LibraryException(String message) {
        super(message);
        this.errorCode = "LIBRARY_ERROR";
    }

    /**
     * Constructor with message and cause (for backward compatibility).
     * @param message The error message
     * @param cause The cause of the exception
     */
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "LIBRARY_ERROR";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
