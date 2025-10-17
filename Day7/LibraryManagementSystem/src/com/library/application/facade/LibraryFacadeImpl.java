package com.library.application.facade;

import com.library.domain.exception.LibraryException;
import com.library.domain.exception.ValidationException;
import com.library.domain.model.*;
import com.library.domain.service.LibraryService;
import com.library.domain.valueobject.ISBN;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

/**
 * Facade for the library application.
 * Provides a simplified interface for client applications.
 * Pure OOP implementation without any framework dependencies.
 */
public class LibraryFacadeImpl implements LibraryFacade {
    private final LibraryService libraryService;

    public LibraryFacadeImpl(LibraryService libraryService) {
        this.libraryService = Objects.requireNonNull(libraryService, "Library service cannot be null");
    }

    @Override
    public void addBook(String isbn, String title, String publisher, int year,
                       String authorName, String categoryName, int copies) throws Exception {
        try {
            ISBN isbnObj = new ISBN(isbn);
            Author author = new Author(UUID.randomUUID().toString(), authorName, null, null, null);
            Category category = new Category(UUID.randomUUID().toString(), categoryName, null);
            
            libraryService.addBook(isbnObj, title, publisher, Year.of(year), author, category, copies);
        } catch (ValidationException e) {
            throw new Exception("Validation error: " + e.getMessage(), e);
        }
    }

    @Override
    public void registerMember(String memberId, String name, String email,
                              String phoneNumber, LocalDate dateOfBirth) throws Exception {
        try {
            libraryService.registerMember(memberId, name, email, phoneNumber, dateOfBirth);
        } catch (ValidationException e) {
            throw new Exception("Validation error: " + e.getMessage(), e);
        }
    }

    @Override
    public void borrowBook(String memberId, String isbn) throws Exception {
        try {
            ISBN isbnObj = new ISBN(isbn);
            libraryService.borrowBook(memberId, isbnObj);
        } catch (LibraryException e) {
            throw new Exception("Borrowing error: " + e.getMessage(), e);
        }
    }

    @Override
    public void returnBook(String memberId, String isbn) throws Exception {
        try {
            ISBN isbnObj = new ISBN(isbn);
            libraryService.returnBook(memberId, isbnObj);
        } catch (LibraryException e) {
            throw new Exception("Return error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> searchBooks(String title, String author,
                                                  String category, boolean onlyAvailable) {
        List<Book> books = libraryService.advancedSearch(title, author, category, onlyAvailable);
        return convertBooksToMaps(books);
    }

    @Override
    public List<Map<String, Object>> getAvailableBooks() {
        List<Book> books = libraryService.getAvailableBooks();
        return convertBooksToMaps(books);
    }

    @Override
    public List<Map<String, Object>> getBorrowedBooks(String memberId) throws Exception {
        try {
            List<Book> books = libraryService.getBorrowedBooks(memberId);
            return convertBooksToMaps(books);
        } catch (LibraryException e) {
            throw new Exception("Error retrieving borrowed books: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getLibraryStatistics() {
        return libraryService.getLibraryStatistics();
    }

    @Override
    public List<Map<String, Object>> getAuditLogs() {
        List<LibraryService.AuditLog> logs = libraryService.getAuditLogs();
        List<Map<String, Object>> result = new ArrayList<>();
        for (LibraryService.AuditLog log : logs) {
            Map<String, Object> map = new HashMap<>();
            map.put("action", log.action);
            map.put("details", log.details);
            map.put("timestamp", log.timestamp);
            result.add(map);
        }
        return result;
    }

    private List<Map<String, Object>> convertBooksToMaps(List<Book> books) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Book book : books) {
            result.add(book.toMap());
        }
        return result;
    }
}
