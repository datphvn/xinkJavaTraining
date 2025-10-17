package com.library.domain.exception;

/**
 * Exception thrown when attempting to borrow a book that is not available.
 */
public class BookNotAvailableException extends LibraryException {
    public BookNotAvailableException(String bookTitle) {
        super("BOOK_NOT_AVAILABLE", "Book '" + bookTitle + "' is not available for borrowing");
    }

    public BookNotAvailableException(String bookTitle, Throwable cause) {
        super("BOOK_NOT_AVAILABLE", "Book '" + bookTitle + "' is not available for borrowing", cause);
    }
}
