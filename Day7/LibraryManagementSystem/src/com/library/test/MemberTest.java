package com.library.test;

import com.library.domain.exception.LibraryException;
import com.library.domain.model.*;
import com.library.domain.valueobject.ISBN;
import java.time.LocalDate;
import java.time.Year;

/**
 * Unit tests for Member entity.
 */
public class MemberTest {
    private Member member;
    private Book book;
    private Author author;
    private Category category;

    public void setUp() throws Exception {
        member = new Member("M001", "John Doe", "john@example.com", "123456789", LocalDate.of(1990, 1, 1));
        
        ISBN isbn = new ISBN("978-3-16-148410-0");
        author = new Author("AU001", "J.K. Rowling", LocalDate.of(1965, 7, 31), "British", "Author");
        category = new Category("CAT001", "Fantasy", "Fantasy books");
        book = new Book(isbn, "Harry Potter", "Bloomsbury", Year.of(1997));
        book.addAuthor(author);
        book.addCategory(category);
        book.addCopies(5);
    }

    public void testMemberCreation() {
        assert member.getMemberId().equals("M001");
        assert member.getName().equals("John Doe");
        assert member.getStatus() == MemberStatus.ACTIVE;
        assert member.getBorrowLimit() == 5;
        System.out.println("✓ testMemberCreation passed");
    }

    public void testBorrowBook() {
        member.borrowBook(book);
        assert member.getBorrowedBookCount() == 1;
        assert member.getBorrowedBooks().contains(book);
        assert book.getAvailableCopies() == 4;
        System.out.println("✓ testBorrowBook passed");
    }

    public void testReturnBook() {
        member.borrowBook(book);
        int availableAfterBorrow = book.getAvailableCopies();
        member.returnBook(book);
        assert member.getBorrowedBookCount() == 0;
        assert book.getAvailableCopies() == availableAfterBorrow + 1;
        System.out.println("✓ testReturnBook passed");
    }

    public void testBorrowLimitExceeded() {
        // Create 5 books and borrow them all
        for (int i = 0; i < 5; i++) {
            ISBN isbn = new ISBN("978-3-16-14841" + i + "-0");
            Book b = new Book(isbn, "Book " + i, "Publisher", Year.of(2000));
            Author a = new Author("AU" + i, "Author " + i, LocalDate.now(), "Country", "Bio");
            Category c = new Category("CAT" + i, "Category " + i, "Description");
            b.addAuthor(a);
            b.addCategory(c);
            b.addCopies(1);
            member.borrowBook(b);
        }
        
        // Try to borrow one more
        ISBN isbn = new ISBN("978-3-16-148410-9");
        Book extraBook = new Book(isbn, "Extra Book", "Publisher", Year.of(2000));
        Author a = new Author("AU99", "Author 99", LocalDate.now(), "Country", "Bio");
        Category c = new Category("CAT99", "Category 99", "Description");
        extraBook.addAuthor(a);
        extraBook.addCategory(c);
        extraBook.addCopies(1);
        
        try {
            member.borrowBook(extraBook);
            assert false : "Should throw exception";
        } catch (LibraryException e) {
            assert e.getErrorCode().equals("BORROW_LIMIT_EXCEEDED");
            System.out.println("✓ testBorrowLimitExceeded passed");
        }
    }

    public void testSuspendMember() {
        member.suspend();
        assert member.getStatus() == MemberStatus.SUSPENDED;
        
        try {
            member.borrowBook(book);
            assert false : "Should throw exception";
        } catch (LibraryException e) {
            assert e.getErrorCode().equals("MEMBER_NOT_ACTIVE");
            System.out.println("✓ testSuspendMember passed");
        }
    }

    public void testReactivateMember() {
        member.suspend();
        member.reactivate();
        assert member.getStatus() == MemberStatus.ACTIVE;
        member.borrowBook(book);
        assert member.getBorrowedBookCount() == 1;
        System.out.println("✓ testReactivateMember passed");
    }

    public void testCanBorrowMore() {
        assert member.canBorrowMore();
        
        // Borrow 5 books
        for (int i = 0; i < 5; i++) {
            ISBN isbn = new ISBN("978-3-16-14841" + i + "-0");
            Book b = new Book(isbn, "Book " + i, "Publisher", Year.of(2000));
            Author a = new Author("AU" + i, "Author " + i, LocalDate.now(), "Country", "Bio");
            Category c = new Category("CAT" + i, "Category " + i, "Description");
            b.addAuthor(a);
            b.addCategory(c);
            b.addCopies(1);
            member.borrowBook(b);
        }
        
        assert !member.canBorrowMore();
        System.out.println("✓ testCanBorrowMore passed");
    }

    public void testGetAvailableBorrowSlots() {
        assert member.getAvailableBorrowSlots() == 5;
        member.borrowBook(book);
        assert member.getAvailableBorrowSlots() == 4;
        System.out.println("✓ testGetAvailableBorrowSlots passed");
    }

    public void testSetBorrowLimit() {
        member.setBorrowLimit(10);
        assert member.getBorrowLimit() == 10;
        System.out.println("✓ testSetBorrowLimit passed");
    }

    public void testInvalidBorrowLimit() {
        try {
            member.setBorrowLimit(0);
            assert false : "Should throw exception";
        } catch (IllegalArgumentException e) {
            System.out.println("✓ testInvalidBorrowLimit passed");
        }
    }

    public void testMemberInfo() {
        assert member.getEmail().equals("john@example.com");
        assert member.getPhoneNumber().equals("123456789");
        assert member.getDateOfBirth().equals(LocalDate.of(1990, 1, 1));
        System.out.println("✓ testMemberInfo passed");
    }

    public void testToMap() {
        var map = member.toMap();
        assert map.containsKey("memberId");
        assert map.containsKey("name");
        assert map.get("name").equals("John Doe");
        System.out.println("✓ testToMap passed");
    }

    public static void main(String[] args) {
        MemberTest test = new MemberTest();
        try {
            test.setUp();
            test.testMemberCreation();
            test.testBorrowBook();
            test.testReturnBook();
            test.testBorrowLimitExceeded();
            test.testSuspendMember();
            test.testReactivateMember();
            test.testCanBorrowMore();
            test.testGetAvailableBorrowSlots();
            test.testSetBorrowLimit();
            test.testInvalidBorrowLimit();
            test.testMemberInfo();
            test.testToMap();
            System.out.println("\n✓ All MemberTest tests passed!");
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
