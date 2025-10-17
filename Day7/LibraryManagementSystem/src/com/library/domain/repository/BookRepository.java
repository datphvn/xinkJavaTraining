package com.library.domain.repository;

import com.library.domain.model.Book;
import com.library.domain.valueobject.ISBN;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Book entities.
 * Provides specialized query methods for books.
 */
public interface BookRepository extends Repository<Book, UUID> {
    /**
     * Finds a book by ISBN.
     * @param isbn The ISBN to search for
     * @return The book, or empty if not found
     */
    Optional<Book> findByIsbn(ISBN isbn);

    /**
     * Finds books by title (case-insensitive, partial match).
     * @param title The title to search for
     * @return A list of matching books
     */
    List<Book> findByTitle(String title);

    /**
     * Finds books by author name.
     * @param authorName The author name to search for
     * @return A list of books by that author
     */
    List<Book> findByAuthor(String authorName);

    /**
     * Finds books by category name.
     * @param categoryName The category name to search for
     * @return A list of books in that category
     */
    List<Book> findByCategory(String categoryName);

    /**
     * Finds all available books.
     * @return A list of available books
     */
    List<Book> findAllAvailable();

    /**
     * Counts the total number of available copies.
     * @return The total number of available copies
     */
    long countAvailableCopies();
}
