package com.library.domain.exception;

public class BookNotFoundException extends LibraryException {
    public BookNotFoundException(String isbn) {
        super("BOOK_NOT_FOUND", "Book not found with ISBN: " + isbn);
    }
}
