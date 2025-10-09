package com.library.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private final String memberId;
    private final String name;
    private final List<Book> borrowedBooks = new ArrayList<>();

    public Member(String memberId, String name) {
        if (memberId == null || memberId.isBlank())
            throw new IllegalArgumentException("Member ID cannot be empty");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Member name cannot be empty");

        this.memberId = memberId;
        this.name = name;
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }

    public void borrowBook(Book book) {
        if (!book.isAvailable())
            throw new IllegalStateException("Book is not available");
        book.borrow();
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        if (borrowedBooks.remove(book))
            book.returnBook();
        else
            throw new IllegalStateException("This member didn’t borrow this book");
    }

    public List<Book> getBorrowedBooks() { return borrowedBooks; }

    @Override
    public String toString() {
        return String.format("Member[id=%s, name=%s]", memberId, name);
    }
}
