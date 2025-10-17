package com.library.presentation.cli;

import com.library.domain.exception.LibraryException;
import com.library.domain.exception.ValidationException;
import com.library.domain.model.*;
import com.library.domain.repository.BookRepository;
import com.library.domain.repository.MemberRepository;
import com.library.domain.service.*;
import com.library.domain.valueobject.ISBN;
import java.time.LocalDate;
import java.util.*;

/**
 * Command-line interface for Library Management System.
 */
public class LibraryCommandLine {
    private final LibraryService libraryService;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final Scanner scanner;
    private boolean running = true;

    public LibraryCommandLine(LibraryService libraryService,
                            BookRepository bookRepository,
                            MemberRepository memberRepository) {
        this.libraryService = libraryService;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║     Library Management System (Pure OOP)              ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();
            handleMainMenuChoice(choice);
        }

        System.out.println("\nThank you for using Library Management System!");
        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\n┌─ Main Menu ─────────────────────────────────────┐");
        System.out.println("│ 1. Book Management                              │");
        System.out.println("│ 2. Member Management                            │");
        System.out.println("│ 3. Borrowing Operations                         │");
        System.out.println("│ 4. Search & Recommendations                     │");
        System.out.println("│ 5. Reports & Statistics                         │");
        System.out.println("│ 6. Exit                                         │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("Choose option: ");
    }

    private void handleMainMenuChoice(String choice) {
        switch (choice) {
            case "1" -> bookManagementMenu();
            case "2" -> memberManagementMenu();
            case "3" -> borrowingOperationsMenu();
            case "4" -> searchAndRecommendationsMenu();
            case "5" -> reportsMenu();
            case "6" -> running = false;
            default -> System.out.println("Invalid choice.");
        }
    }

    private void bookManagementMenu() {
        System.out.println("\n┌─ Book Management ───────────────────────────────┐");
        System.out.println("│ 1. Add Book                                     │");
        System.out.println("│ 2. View All Books                               │");
        System.out.println("│ 3. Search Book by ISBN                          │");
        System.out.println("│ 4. Back                                         │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("Choose: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> addBook();
            case "2" -> viewAllBooks();
            case "3" -> searchBookByISBN();
        }
    }

    private void addBook() {
        try {
            System.out.print("ISBN: ");
            String isbnStr = scanner.nextLine().trim();
            ISBN isbn = new ISBN(isbnStr);

            System.out.print("Title: ");
            String title = scanner.nextLine().trim();

            System.out.print("Publisher: ");
            String publisher = scanner.nextLine().trim();

            System.out.print("Year: ");
            int year = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Author: ");
            String authorName = scanner.nextLine().trim();
            Author author = new Author("AU" + System.nanoTime(), authorName, LocalDate.now(), "Unknown", "");

            System.out.print("Category: ");
            String categoryName = scanner.nextLine().trim();
            Category category = new Category("CAT" + System.nanoTime(), categoryName, "");

            System.out.print("Copies: ");
            int copies = Integer.parseInt(scanner.nextLine().trim());

            Book book = libraryService.addBook(isbn, title, publisher, java.time.Year.of(year), author, category, copies);
            System.out.println("Book added: " + book.getId());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("No books.");
            return;
        }
        for (Book book : books) {
            System.out.println(book.getSummary());
        }
    }

    private void searchBookByISBN() {
        System.out.print("ISBN: ");
        String isbnStr = scanner.nextLine().trim();
        try {
            ISBN isbn = new ISBN(isbnStr);
            Optional<Book> book = libraryService.findBookByIsbn(isbn);
            if (book.isPresent()) {
                System.out.println("Found: " + book.get().getSummary());
            } else {
                System.out.println("Not found.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void memberManagementMenu() {
        System.out.println("\n┌─ Member Management ────────────────────────────┐");
        System.out.println("│ 1. Register Member                              │");
        System.out.println("│ 2. View All Members                             │");
        System.out.println("│ 3. Back                                         │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("Choose: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> registerMember();
            case "2" -> viewAllMembers();
        }
    }

    private void registerMember() {
        try {
            String memberId = UUID.randomUUID().toString();
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Phone: ");
            String phone = scanner.nextLine().trim();

            System.out.print("DOB (YYYY-MM-DD): ");
            LocalDate dob = LocalDate.parse(scanner.nextLine().trim());

            Member member = libraryService.registerMember(memberId, name, email, phone, dob);
            System.out.println("Member registered: " + member.getId());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllMembers() {
        List<Member> members = memberRepository.findAll();
        if (members.isEmpty()) {
            System.out.println("No members.");
            return;
        }
        for (Member member : members) {
            System.out.println(member.getSummary());
        }
    }

    private void borrowingOperationsMenu() {
        System.out.println("\n┌─ Borrowing ────────────────────────────────────┐");
        System.out.println("│ 1. Borrow Book                                  │");
        System.out.println("│ 2. Return Book                                  │");
        System.out.println("│ 3. View Borrowed Books                          │");
        System.out.println("│ 4. Back                                         │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("Choose: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> borrowBook();
            case "2" -> returnBook();
            case "3" -> viewBorrowedBooks();
        }
    }

    private void borrowBook() {
        try {
            System.out.print("Member ID: ");
            String memberId = scanner.nextLine().trim();

            System.out.print("ISBN: ");
            String isbnStr = scanner.nextLine().trim();
            ISBN isbn = new ISBN(isbnStr);

            BorrowRecord record = libraryService.borrowBook(memberId, isbn);
            System.out.println("Borrowed! Due: " + record.getDueDate());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void returnBook() {
        try {
            System.out.print("Member ID: ");
            String memberId = scanner.nextLine().trim();

            System.out.print("ISBN: ");
            String isbnStr = scanner.nextLine().trim();
            ISBN isbn = new ISBN(isbnStr);

            libraryService.returnBook(memberId, isbn);
            System.out.println("Returned successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewBorrowedBooks() {
        try {
            System.out.print("Member ID: ");
            String memberId = scanner.nextLine().trim();
            List<Book> books = libraryService.getBorrowedBooks(memberId);
            if (books.isEmpty()) {
                System.out.println("No borrowed books.");
            } else {
                for (Book book : books) {
                    System.out.println("- " + book.getTitle());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void searchAndRecommendationsMenu() {
        System.out.println("\n┌─ Search & Recommendations ─────────────────────┐");
        System.out.println("│ 1. Search Books                                 │");
        System.out.println("│ 2. Get Recommendations                          │");
        System.out.println("│ 3. Back                                         │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("Choose: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> searchBooks();
            case "2" -> getRecommendations();
        }
    }

    private void searchBooks() {
        System.out.print("Title (or empty): ");
        String title = scanner.nextLine().trim();
        title = title.isEmpty() ? null : title;

        System.out.print("Author (or empty): ");
        String author = scanner.nextLine().trim();
        author = author.isEmpty() ? null : author;

        System.out.print("Only available? (y/n): ");
        boolean onlyAvailable = scanner.nextLine().trim().equalsIgnoreCase("y");

        List<Book> results = libraryService.advancedSearch(title, author, null, onlyAvailable);
        if (results.isEmpty()) {
            System.out.println("No results.");
        } else {
            for (Book book : results) {
                System.out.println("- " + book.getSummary());
            }
        }
    }

    private void getRecommendations() {
        try {
            System.out.print("Member ID: ");
            String memberId = scanner.nextLine().trim();
            List<Book> recommendations = libraryService.getRecommendations(memberId, 5);
            if (recommendations.isEmpty()) {
                System.out.println("No recommendations.");
            } else {
                for (Book book : recommendations) {
                    System.out.println("- " + book.getTitle());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void reportsMenu() {
        System.out.println("\n┌─ Reports ──────────────────────────────────────┐");
        System.out.println("│ 1. Library Statistics                           │");
        System.out.println("│ 2. Audit Logs                                   │");
        System.out.println("│ 3. Back                                         │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("Choose: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> showStatistics();
            case "2" -> showAuditLogs();
        }
    }

    private void showStatistics() {
        Map<String, Object> stats = libraryService.getLibraryStatistics();
        System.out.println("\n┌─ Statistics ───────────────────────────────────┐");
        stats.forEach((key, value) -> System.out.println("│ " + key + ": " + value));
        System.out.println("└─────────────────────────────────────────────────┘");
    }

    private void showAuditLogs() {
        List<LibraryService.AuditLog> logs = libraryService.getAuditLogs();
        System.out.println("\n┌─ Audit Logs ───────────────────────────────────┐");
        for (LibraryService.AuditLog log : logs) {
            System.out.println("│ " + log);
        }
        System.out.println("└─────────────────────────────────────────────────┘");
    }
}
