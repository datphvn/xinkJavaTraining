package com.library.domain.service;

import com.library.domain.exception.LibraryException;
import com.library.domain.model.Book;
import com.library.domain.model.BookStatus;

/**
 * Business rule that validates book availability consistency.
 * Ensures that available copies match the book's status.
 */
public class BookAvailabilityRule implements BusinessRule<Book> {

    @Override
    public boolean applies(Book book) {
        return book != null && book.getStatus() == BookStatus.AVAILABLE;
    }

    @Override
    public void execute(Book book) throws LibraryException {
        if (book.getAvailableCopies() <= 0) {
            throw new LibraryException("INCONSISTENT_STATE",
                String.format("Book '%s' shows as available but has no available copies",
                    book.getTitle()));
        }

        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new LibraryException("INCONSISTENT_STATE",
                String.format("Available copies (%d) cannot exceed total copies (%d) for book '%s'",
                    book.getAvailableCopies(), book.getTotalCopies(), book.getTitle()));
        }
    }

    @Override
    public String getDescription() {
        return "Validate book availability consistency";
    }

    @Override
    public int getPriority() {
        return 50; // Lower priority
    }

    @Override
    public String getName() {
        return "BookAvailabilityRule";
    }
}
