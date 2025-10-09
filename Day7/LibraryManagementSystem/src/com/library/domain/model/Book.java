package com.library.domain.model;

import java.util.Objects;

public class Book {
    private String isbn;
    private String title;
    private Author author;
    private Category category;
    private boolean available = true;

    public Book(String isbn, String title, Author author, Category category) {
        if (isbn == null || isbn.isBlank())
            throw new IllegalArgumentException("ISBN cannot be empty");
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title cannot be empty");

        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public Author getAuthor() { return author; }
    public Category getCategory() { return category; }
    public boolean isAvailable() { return available; }

    public void borrow() {
        if (!available) throw new IllegalStateException("Book already borrowed");
        available = false;
    }

    public void returnBook() { available = true; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() { return Objects.hash(isbn); }

    @Override
    public String toString() {
        return String.format("%s (%s) by %s", title, isbn, author.getName());
    }
}
