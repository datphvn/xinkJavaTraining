package com.library.presentation.cli;

import com.library.application.facade.LibraryFacade;
import com.library.application.facade.LibraryFacadeImpl;
import com.library.domain.model.*;
import com.library.domain.repository.BookRepository;
import com.library.domain.repository.MemberRepository;
import com.library.domain.service.*;
import com.library.infrastructure.repository.InMemoryBookRepository;
import com.library.infrastructure.repository.InMemoryMemberRepository;
import com.library.domain.valueobject.ISBN;
import java.time.LocalDate;
import java.time.Year;
import java.util.UUID;

/**
 * CLI entry point for the Library Management System.
 * Demonstrates the complete workflow of the system.
 * Pure OOP implementation without any framework dependencies.
 */
public class LibraryCLI {
    public static void main(String[] args) {
        try {
            // Initialize repositories
            BookRepository bookRepo = new InMemoryBookRepository();
            MemberRepository memberRepo = new InMemoryMemberRepository();

            // Initialize rule engine
            RuleEngine ruleEngine = new RuleEngine();
            ruleEngine.registerRule(Member.class, new MemberStatusRule());
            ruleEngine.registerRule(Member.class, new BorrowingLimitRule());
            ruleEngine.registerRule(Book.class, new BookAvailabilityRule());

            // Initialize workflow engine
            WorkflowEngine workflowEngine = new WorkflowEngine();
            workflowEngine.registerWorkflow(new BookBorrowingWorkflow(ruleEngine));

            // Initialize service
            LibraryService libraryService = new LibraryService(
                bookRepo,
                memberRepo,
                ruleEngine,
                workflowEngine
            );

            // Initialize facade
            LibraryFacade facade = new LibraryFacadeImpl(libraryService);

            // Seed sample data
            seedSampleData(libraryService, bookRepo, memberRepo);

            // Demonstrate borrowing workflow
            demonstrateBorrowingWorkflow(facade);

        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void seedSampleData(LibraryService libraryService,
                                      BookRepository bookRepo,
                                      MemberRepository memberRepo) throws Exception {
        try {
            // Create authors
            Author author1 = new Author("AU001", "George Orwell", LocalDate.of(1903, 6, 25), "British", "Author of 1984");
            Author author2 = new Author("AU002", "J.K. Rowling", LocalDate.of(1965, 7, 31), "British", "Author of Harry Potter");

            // Create categories
            Category category1 = new Category("CAT001", "Dystopian", "Dystopian fiction");
            Category category2 = new Category("CAT002", "Fantasy", "Fantasy novels");

            // Create and save books
            ISBN isbn1 = new ISBN("978-0451524935");
            Book book1 = new Book(isbn1, "1984", "Secker & Warburg", Year.of(1949));
            book1.addAuthor(author1);
            book1.addCategory(category1);
            book1.setPages(328);
            book1.addCopies(3);
            bookRepo.save(book1);

            ISBN isbn2 = new ISBN("978-0747532699");
            Book book2 = new Book(isbn2, "Harry Potter and the Philosopher's Stone", "Bloomsbury", Year.of(1997));
            book2.addAuthor(author2);
            book2.addCategory(category2);
            book2.setPages(309);
            book2.addCopies(5);
            bookRepo.save(book2);

            // Create and save members
            Member member1 = new Member("M001", "Alice Johnson", "alice@example.com", "555-0001", LocalDate.of(1990, 1, 15));
            Member member2 = new Member("M002", "Bob Smith", "bob@example.com", "555-0002", LocalDate.of(1985, 5, 20));
            memberRepo.save(member1);
            memberRepo.save(member2);

            System.out.println("✓ Sample data loaded successfully!");
            System.out.println("  - 2 books added");
            System.out.println("  - 2 members registered\n");

        } catch (Exception e) {
            System.err.println("Warning: Could not seed sample data: " + e.getMessage());
        }
    }

    private static void demonstrateBorrowingWorkflow(LibraryFacade facade) {
        System.out.println("========== DEMONSTRATING BORROWING WORKFLOW ==========\n");

        try {
            // Borrow a book
            System.out.println("1. Borrowing book...");
            facade.borrowBook("M001", "978-0451524935");
            System.out.println("   ✓ Book borrowed successfully!\n");

            // View borrowed books
            System.out.println("2. Viewing borrowed books for member M001...");
            var borrowedBooks = facade.getBorrowedBooks("M001");
            if (borrowedBooks.isEmpty()) {
                System.out.println("   No borrowed books found.");
            } else {
                System.out.println("   Borrowed books:");
                for (var book : borrowedBooks) {
                    System.out.println("   - " + book.get("title"));
                }
            }
            System.out.println();

            // Return the book
            System.out.println("3. Returning book...");
            facade.returnBook("M001", "978-0451524935");
            System.out.println("   ✓ Book returned successfully!\n");

            // View library statistics
            System.out.println("4. Library Statistics:");
            var stats = facade.getLibraryStatistics();
            System.out.println("   Total Books: " + stats.get("totalBooks"));
            System.out.println("   Available Books: " + stats.get("availableBooks"));
            System.out.println("   Total Members: " + stats.get("totalMembers"));
            System.out.println("   Active Members: " + stats.get("activeMembers"));

        } catch (Exception e) {
            System.err.println("Error during demonstration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
