package com.library.ui.cli;

import com.library.application.facade.LibraryFacade;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Command-line interface for the library management system.
 * Provides interactive menu for users to manage books and members.
 * Pure OOP implementation without any framework dependencies.
 */
public class LibraryCLI {
    private final LibraryFacade libraryFacade;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;
    private boolean running;

    public LibraryCLI(LibraryFacade libraryFacade) {
        this.libraryFacade = libraryFacade;
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.running = true;
    }

    /**
     * Starts the CLI application.
     */
    public void start() {
        displayWelcome();
        while (running) {
            displayMainMenu();
            processMainMenuChoice();
        }
        displayGoodbye();
    }

    private void displayWelcome() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     LIBRARY MANAGEMENT SYSTEM - Command Line Interface      ║");
        System.out.println("║              Pure OOP Implementation in Java                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }

    private void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Borrowing Operations");
        System.out.println("4. Search & Browse");
        System.out.println("5. View Statistics");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private void processMainMenuChoice() {
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                handleBookManagement();
                break;
            case "2":
                handleMemberManagement();
                break;
            case "3":
                handleBorrowingOperations();
                break;
            case "4":
                handleSearchAndBrowse();
                break;
            case "5":
                handleViewStatistics();
                break;
            case "6":
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleBookManagement() {
        System.out.println("\n========== BOOK MANAGEMENT ==========");
        System.out.println("1. Add a new book");
        System.out.println("2. Back to main menu");
        System.out.print("Enter your choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                addNewBook();
                break;
            case "2":
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void addNewBook() {
        try {
            System.out.print("Enter ISBN: ");
            String isbn = scanner.nextLine().trim();

            System.out.print("Enter title: ");
            String title = scanner.nextLine().trim();

            System.out.print("Enter publisher: ");
            String publisher = scanner.nextLine().trim();

            System.out.print("Enter publication year: ");
            int year = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter author name: ");
            String authorName = scanner.nextLine().trim();

            System.out.print("Enter category name: ");
            String categoryName = scanner.nextLine().trim();

            System.out.print("Enter number of copies: ");
            int copies = Integer.parseInt(scanner.nextLine().trim());

            libraryFacade.addBook(isbn, title, publisher, year, authorName, categoryName, copies);
            System.out.println("✓ Book added successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error adding book: " + e.getMessage());
        }
    }

    private void handleMemberManagement() {
        System.out.println("\n========== MEMBER MANAGEMENT ==========");
        System.out.println("1. Register a new member");
        System.out.println("2. Back to main menu");
        System.out.print("Enter your choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                registerNewMember();
                break;
            case "2":
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void registerNewMember() {
        try {
            System.out.print("Enter member ID: ");
            String memberId = scanner.nextLine().trim();

            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter phone number: ");
            String phoneNumber = scanner.nextLine().trim();

            System.out.print("Enter date of birth (yyyy-MM-dd): ");
            LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine().trim(), dateFormatter);

            libraryFacade.registerMember(memberId, name, email, phoneNumber, dateOfBirth);
            System.out.println("✓ Member registered successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error registering member: " + e.getMessage());
        }
    }

    private void handleBorrowingOperations() {
        System.out.println("\n========== BORROWING OPERATIONS ==========");
        System.out.println("1. Borrow a book");
        System.out.println("2. Return a book");
        System.out.println("3. View borrowed books");
        System.out.println("4. Back to main menu");
        System.out.print("Enter your choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                borrowBook();
                break;
            case "2":
                returnBook();
                break;
            case "3":
                viewBorrowedBooks();
                break;
            case "4":
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void borrowBook() {
        try {
            System.out.print("Enter member ID: ");
            String memberId = scanner.nextLine().trim();

            System.out.print("Enter book ISBN: ");
            String isbn = scanner.nextLine().trim();

            libraryFacade.borrowBook(memberId, isbn);
            System.out.println("✓ Book borrowed successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error borrowing book: " + e.getMessage());
        }
    }

    private void returnBook() {
        try {
            System.out.print("Enter member ID: ");
            String memberId = scanner.nextLine().trim();

            System.out.print("Enter book ISBN: ");
            String isbn = scanner.nextLine().trim();

            libraryFacade.returnBook(memberId, isbn);
            System.out.println("✓ Book returned successfully!");
        } catch (Exception e) {
            System.out.println("✗ Error returning book: " + e.getMessage());
        }
    }

    private void viewBorrowedBooks() {
        try {
            System.out.print("Enter member ID: ");
            String memberId = scanner.nextLine().trim();

            List<Map<String, Object>> books = libraryFacade.getBorrowedBooks(memberId);
            if (books.isEmpty()) {
                System.out.println("No borrowed books found.");
            } else {
                System.out.println("\n========== BORROWED BOOKS ==========");
                for (Map<String, Object> book : books) {
                    System.out.println("- " + book.get("title") + " (ISBN: " + book.get("isbn") + ")");
                }
            }
        } catch (Exception e) {
            System.out.println("✗ Error retrieving borrowed books: " + e.getMessage());
        }
    }

    private void handleSearchAndBrowse() {
        System.out.println("\n========== SEARCH & BROWSE ==========");
        System.out.println("1. View available books");
        System.out.println("2. Search books");
        System.out.println("3. Back to main menu");
        System.out.print("Enter your choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                viewAvailableBooks();
                break;
            case "2":
                searchBooks();
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void viewAvailableBooks() {
        List<Map<String, Object>> books = libraryFacade.getAvailableBooks();
        displayBooks(books, "AVAILABLE BOOKS");
    }

    private void searchBooks() {
        System.out.print("Enter title (or leave blank): ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter author (or leave blank): ");
        String author = scanner.nextLine().trim();

        System.out.print("Enter category (or leave blank): ");
        String category = scanner.nextLine().trim();

        List<Map<String, Object>> books = libraryFacade.searchBooks(
            title.isEmpty() ? null : title,
            author.isEmpty() ? null : author,
            category.isEmpty() ? null : category,
            false
        );
        displayBooks(books, "SEARCH RESULTS");
    }

    private void displayBooks(List<Map<String, Object>> books, String title) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("\n========== " + title + " ==========");
            for (Map<String, Object> book : books) {
                System.out.println("- " + book.get("title") + " by " + book.get("authors") +
                                 " (ISBN: " + book.get("isbn") + ")");
            }
        }
    }

    private void handleViewStatistics() {
        Map<String, Object> stats = libraryFacade.getLibraryStatistics();
        System.out.println("\n========== LIBRARY STATISTICS ==========");
        System.out.println("Total Books: " + stats.get("totalBooks"));
        System.out.println("Available Books: " + stats.get("availableBooks"));
        System.out.println("Total Members: " + stats.get("totalMembers"));
        System.out.println("Active Members: " + stats.get("activeMembers"));
        System.out.println("Total Borrowings: " + stats.get("totalBorrowings"));
        System.out.println("Average Rating: " + String.format("%.2f", stats.get("averageRating")));
    }

    private void displayGoodbye() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                  Thank you for using the                    ║");
        System.out.println("║            Library Management System. Goodbye!              ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
}
