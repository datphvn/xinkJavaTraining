package com.library.test;

import com.library.domain.model.*;

public class DomainTest {
    public static void main(String[] args) {
        Author author = new Author("J.K. Rowling");
        Book book = new Book("978-0545010221", "Harry Potter", author, Category.FICTION);
        Member member = new Member("M001", "Alice");

        member.borrowBook(book);
        System.out.println(book.isAvailable()); // false
        member.returnBook(book);
        System.out.println(book.isAvailable()); // true
    }
}
