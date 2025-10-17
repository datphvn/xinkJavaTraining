package com.library.test;

import com.library.domain.exception.LibraryException;
import com.library.domain.exception.ValidationException;
import com.library.domain.model.*;
import com.library.domain.valueobject.ISBN;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

/**
 * Unit tests for Book entity.
 */
public class BookTest {
    private Book book;
    private Author author;
    private Category category;
    private ISBN isbn;

    public void setUp() throws Exception {
        isbn = new ISBN("978-3-16-148410-0");
        author = new Author("AU001", "J.K. Rowling", LocalDate.of(1965, 7, 31), "British", "Famous author");
        category = new Category("CAT001", "Fantasy", "Fantasy books");
        book = new Book(isbn, "Harry Potter", "Bloomsbury", Year.of(1997));
        book.addAuthor(author);
        book.addCategory(category);
        book.setPages(309);
        book.addCopies(5);
    }

    public void testBookCreation() {
        assert book.getTitle().equals("Harry Potter");
        assert book.getPublisher().equals("Bloomsbury");
        assert book.getPublicationYear().equals(Year.of(1997));
        assert book.getStatus() == BookStatus.AVAILABLE;
        assert book.getTotalCopies() == 5;
        assert book.getAvailableCopies() == 5;
        System.out.println("✓ testBookCreation passed");
    }

    public void testAddAuthor() {
        Author author2 = new Author("AU002", "Stephen King", LocalDate.of(1947, 9, 21), "American", "Horror author");
        book.addAuthor(author2);
        assert book.getAuthors().size() == 2;
        assert book.hasAuthor(author2);
        System.out.println("✓ testAddAuthor passed");
    }

    public void testAddCategory() {
        Category category2 = new Category("CAT002", "Adventure", "Adventure books");
        book.addCategory(category2);
        assert book.getCategories().size() == 2;
        assert book.belongsToCategory(category2);
        System.out.println("✓ testAddCategory passed");
    }

    public void testBorrowCopy() {
        int initialAvailable = book.getAvailableCopies();
        book.borrowCopy();
        assert book.getAvailableCopies() == initialAvailable - 1;
        assert book.getBorrowingCount() == 1;
        System.out.println("✓ testBorrowCopy passed");
    }

    public void testReturnCopy() {
        book.borrowCopy();
        int afterBorrow = book.getAvailableCopies();
        book.returnCopy();
        assert book.getAvailableCopies() == afterBorrow + 1;
        System.out.println("✓ testReturnCopy passed");
    }

    public void testBorrowCopyWhenNotAvailable() {
        book.removeCopies(5); // Remove all copies
        try {
            book.borrowCopy();
            assert false : "Should throw exception";
        } catch (LibraryException e) {
            assert e.getErrorCode().equals("NO_AVAILABLE_COPIES");
            System.out.println("✓ testBorrowCopyWhenNotAvailable passed");
        }
    }

    public void testAddCopies() {
        int initialTotal = book.getTotalCopies();
        book.addCopies(3);
        assert book.getTotalCopies() == initialTotal + 3;
        assert book.getAvailableCopies() == initialTotal + 3;
        System.out.println("✓ testAddCopies passed");
    }

    public void testRemoveCopies() {
        int initialTotal = book.getTotalCopies();
        book.removeCopies(2);
        assert book.getTotalCopies() == initialTotal - 2;
        assert book.getAvailableCopies() == initialTotal - 2;
        System.out.println("✓ testRemoveCopies passed");
    }

    public void testMarkAsLost() {
        book.markAsLost();
        assert book.getStatus() == BookStatus.LOST;
        assert !book.isAvailable();
        System.out.println("✓ testMarkAsLost passed");
    }

    public void testMarkAsDamaged() {
        book.markAsDamaged();
        assert book.getStatus() == BookStatus.DAMAGED;
        assert !book.isAvailable();
        System.out.println("✓ testMarkAsDamaged passed");
    }

    public void testCanBorrow() {
        assert book.canBorrow();
        book.setFormat(BookFormat.REFERENCE_ONLY);
        assert !book.canBorrow();
        System.out.println("✓ testCanBorrow passed");
    }

    public void testAddKeyword() {
        book.addKeyword("Magic");
        book.addKeyword("Wizards");
        assert book.getKeywords().contains("magic");
        assert book.getKeywords().contains("wizards");
        System.out.println("✓ testAddKeyword passed");
    }

    public void testValidation() {
        Book invalidBook = new Book(isbn, "", "Publisher", Year.of(2000));
        try {
            invalidBook.validate();
            assert false : "Should throw ValidationException";
        } catch (ValidationException e) {
            assert e.hasFieldErrors();
            System.out.println("✓ testValidation passed");
        }
    }

    public void testToMap() {
        var map = book.toMap();
        assert map.containsKey("isbn");
        assert map.containsKey("title");
        assert map.containsKey("authors");
        assert map.get("title").equals("Harry Potter");
        System.out.println("✓ testToMap passed");
    }

    public void testSoftDelete() {
        assert !book.isDeleted();
        book.markAsDeleted();
        assert book.isDeleted();
        assert book.getDeletedAt() != null;
        System.out.println("✓ testSoftDelete passed");
    }

    public static void main(String[] args) {
        BookTest test = new BookTest();
        try {
            test.setUp();
            test.testBookCreation();
            test.testAddAuthor();
            test.testAddCategory();
            test.testBorrowCopy();
            test.testReturnCopy();
            test.testBorrowCopyWhenNotAvailable();
            test.testAddCopies();
            test.testRemoveCopies();
            test.testMarkAsLost();
            test.testMarkAsDamaged();
            test.testCanBorrow();
            test.testAddKeyword();
            test.testValidation();
            test.testToMap();
            test.testSoftDelete();
            System.out.println("\n✓ All BookTest tests passed!");
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
