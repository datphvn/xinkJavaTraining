package com.library.application.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Facade interface for the library application.
 * Provides a simplified interface for client applications.
 * Pure OOP implementation without any framework dependencies.
 */
public interface LibraryFacade {
    /**
     * Adds a new book to the library.
     */
    void addBook(String isbn, String title, String publisher, int year,
                String authorName, String categoryName, int copies) throws Exception;

    /**
     * Registers a new member.
     */
    void registerMember(String memberId, String name, String email,
                       String phoneNumber, LocalDate dateOfBirth) throws Exception;

    /**
     * Borrows a book for a member.
     */
    void borrowBook(String memberId, String isbn) throws Exception;

    /**
     * Returns a borrowed book.
     */
    void returnBook(String memberId, String isbn) throws Exception;

    /**
     * Searches for books with multiple criteria.
     */
    List<Map<String, Object>> searchBooks(String title, String author,
                                          String category, boolean onlyAvailable);

    /**
     * Gets all available books.
     */
    List<Map<String, Object>> getAvailableBooks();

    /**
     * Gets books borrowed by a member.
     */
    List<Map<String, Object>> getBorrowedBooks(String memberId) throws Exception;

    /**
     * Gets library statistics.
     */
    Map<String, Object> getLibraryStatistics();

    /**
     * Gets audit logs.
     */
    List<Map<String, Object>> getAuditLogs();
}
