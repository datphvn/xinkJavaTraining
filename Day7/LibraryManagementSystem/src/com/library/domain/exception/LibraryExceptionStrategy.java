package com.library.domain.exception;

import java.time.LocalDateTime;

public class LibraryExceptionStrategy {

    // ===== Base Exception =====
    public static abstract class LibraryException extends Exception {
        private final String errorCode;
        private final LocalDateTime timestamp;

        protected LibraryException(String errorCode, String message) {
            super(message);
            this.errorCode = errorCode;
            this.timestamp = LocalDateTime.now();
        }

        protected LibraryException(String errorCode, String message, Throwable cause) {
            super(message, cause);
            this.errorCode = errorCode;
            this.timestamp = LocalDateTime.now();
        }

        public String getErrorCode() { return errorCode; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    // ===== Specific Exceptions =====
    public static class BookNotFoundException extends LibraryException {
        public BookNotFoundException(String isbn) {
            super("BOOK_NOT_FOUND", "Book not found with ISBN: " + isbn);
        }
    }

    public static class MemberNotFoundException extends LibraryException {
        public MemberNotFoundException(String membershipId) {
            super("MEMBER_NOT_FOUND", "Member not found: " + membershipId);
        }
    }

    public static class BorrowingException extends LibraryException {
        public BorrowingException(String message) {
            super("BORROWING_ERROR", message);
        }

        public BorrowingException(String message, Throwable cause) {
            super("BORROWING_ERROR", message, cause);
        }
    }
}
