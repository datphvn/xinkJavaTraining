package com.library.infrastructure.repository;

import com.library.domain.model.Book;
import com.library.domain.model.BookStatus;
import com.library.domain.repository.BookRepository;
import com.library.domain.valueobject.ISBN;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * In-memory implementation of BookRepository.
 * Provides specialized query methods for books.
 * Pure OOP implementation without any framework dependencies.
 */
public class InMemoryBookRepository extends InMemoryRepository<Book, UUID> implements BookRepository {

    @Override
    public Optional<Book> findByIsbn(ISBN isbn) {
        if (isbn == null) {
            return Optional.empty();
        }
        return store.values().stream()
            .filter(book -> book.getIsbn().equals(isbn))
            .findFirst();
    }

    @Override
    public List<Book> findByTitle(String title) {
        if (title == null || title.isBlank()) {
            return List.of();
        }
        String searchTerm = title.toLowerCase();
        return store.values().stream()
            .filter(book -> book.getTitle().toLowerCase().contains(searchTerm))
            .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthor(String authorName) {
        if (authorName == null || authorName.isBlank()) {
            return List.of();
        }
        String searchTerm = authorName.toLowerCase();
        return store.values().stream()
            .filter(book -> book.getAuthors().stream()
                .anyMatch(author -> author.getName().toLowerCase().contains(searchTerm)))
            .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            return List.of();
        }
        String searchTerm = categoryName.toLowerCase();
        return store.values().stream()
            .filter(book -> book.getCategories().stream()
                .anyMatch(category -> category.getName().toLowerCase().contains(searchTerm)))
            .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAllAvailable() {
        return store.values().stream()
            .filter(book -> book.getStatus() == BookStatus.AVAILABLE && !book.isDeleted())
            .collect(Collectors.toList());
    }

    @Override
    public long countAvailableCopies() {
        return store.values().stream()
            .filter(book -> !book.isDeleted())
            .mapToLong(book -> book.getAvailableCopies() != null ? book.getAvailableCopies() : 0)
            .sum();
    }

    @Override
    protected UUID extractId(Book entity) {
        return entity.getId();
    }
}
