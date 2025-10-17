package com.library.infrastructure.persistence;

import com.library.domain.model.Book;
import com.library.domain.repository.BookRepository;
import com.library.domain.valueobject.ISBN;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryBookRepository extends InMemoryRepository<Book, java.util.UUID> implements BookRepository {

    @Override
    public Optional<Book> findByIsbn(ISBN isbn) {
        return findAll().stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return findAll().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthor(String authorName) {
        return findAll().stream()
                .filter(b -> b.getAuthors().stream()
                        .anyMatch(a -> a.getName().toLowerCase().contains(authorName.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByCategory(String categoryName) {
        return findAll().stream()
                .filter(b -> b.getCategories().stream()
                        .anyMatch(c -> c.getName().toLowerCase().contains(categoryName.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAllAvailable() {
        return findAll().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public long countAvailableCopies() {
        return findAll().stream()
                .mapToLong(b -> b.getAvailableCopies() != null ? b.getAvailableCopies() : 0)
                .sum();
    }
}
