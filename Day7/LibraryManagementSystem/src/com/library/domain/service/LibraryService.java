package com.library.domain.service;

import com.library.domain.exception.LibraryException;
import com.library.domain.exception.ValidationException;
import com.library.domain.model.*;
import com.library.domain.repository.BookRepository;
import com.library.domain.repository.MemberRepository;
import com.library.domain.valueobject.ISBN;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Core library service implementing business logic.
 * Handles borrowing, returning, searching, and member management.
 */
public class LibraryService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final RuleEngine ruleEngine;
    private final WorkflowEngine workflowEngine;
    private final ReentrantLock lock = new ReentrantLock();
    private final List<AuditLog> auditLogs;

    public LibraryService(BookRepository bookRepository, 
                         MemberRepository memberRepository,
                         RuleEngine ruleEngine,
                         WorkflowEngine workflowEngine) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.ruleEngine = ruleEngine;
        this.workflowEngine = workflowEngine;
        this.auditLogs = Collections.synchronizedList(new ArrayList<>());
    }

    // ==================== BOOK MANAGEMENT ====================

    /**
     * Adds a new book to the library.
     */
    public Book addBook(ISBN isbn, String title, String publisher, Year publicationYear,
                       Author author, Category category, int copies) throws ValidationException {
        lock.lock();
        try {
            Book book = new Book(isbn, title, publisher, publicationYear);
            book.addAuthor(author);
            book.addCategory(category);
            book.addCopies(copies);
            book.validate();

            Book saved = bookRepository.save(book);
            logAction("ADD_BOOK", "Book added: " + title);
            return saved;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Finds a book by ISBN.
     */
    public Optional<Book> findBookByIsbn(ISBN isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    /**
     * Searches books by title.
     */
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    /**
     * Searches books by author.
     */
    public List<Book> searchBooksByAuthor(String authorName) {
        return bookRepository.findByAuthor(authorName);
    }

    /**
     * Gets all available books.
     */
    public List<Book> getAvailableBooks() {
        return bookRepository.findAllAvailable();
    }

    /**
     * Updates book details.
     */
    public Book updateBook(Book book) throws ValidationException {
        lock.lock();
        try {
            book.validate();
            Book updated = bookRepository.save(book);
            logAction("UPDATE_BOOK", "Book updated: " + book.getTitle());
            return updated;
        } finally {
            lock.unlock();
        }
    }

    // ==================== MEMBER MANAGEMENT ====================

    /**
     * Registers a new member.
     */
    public Member registerMember(String memberId, String name, String email, 
                                String phoneNumber, LocalDate dateOfBirth) throws ValidationException {
        lock.lock();
        try {
            Member member = new Member(memberId, name, email, phoneNumber, dateOfBirth);
            member.validate();

            Member saved = memberRepository.save(member);
            logAction("REGISTER_MEMBER", "Member registered: " + name);
            return saved;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Finds a member by ID.
     */
    public Optional<Member> findMember(String memberId) {
        return memberRepository.findById(UUID.fromString(memberId));
    }

    /**
     * Gets all active members.
     */
    public List<Member> getActiveMembers() {
        return memberRepository.findAllActive();
    }

    /**
     * Updates member information.
     */
    public Member updateMember(Member member) throws ValidationException {
        lock.lock();
        try {
            member.validate();
            Member updated = memberRepository.save(member);
            logAction("UPDATE_MEMBER", "Member updated: " + member.getName());
            return updated;
        } finally {
            lock.unlock();
        }
    }

    // ==================== BORROWING OPERATIONS ====================

    /**
     * Borrows a book for a member using the workflow engine.
     */
    public BorrowRecord borrowBook(String memberId, ISBN isbn) throws LibraryException {
        lock.lock();
        try {
            // Find member and book
            Member member = memberRepository.findById(UUID.fromString(memberId))
                .orElseThrow(() -> new LibraryException("MEMBER_NOT_FOUND", "Member not found: " + memberId));

            Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new LibraryException("BOOK_NOT_FOUND", "Book not found: " + isbn));

            // Execute borrowing workflow
            WorkflowContext context = new WorkflowContext();
            context.put("member", member);
            context.put("book", book);

            WorkflowResult result = workflowEngine.executeWorkflow("BOOK_BORROWING", context);

            if (!result.isSuccess()) {
                throw new LibraryException("BORROWING_FAILED", result.getMessage());
            }

            // Create borrow record
            BorrowRecord record = new BorrowRecord(
                UUID.randomUUID().toString(),
                member,
                book,
                LocalDate.now(),
                14
            );

            // Save updated entities
            bookRepository.save(book);
            memberRepository.save(member);

            logAction("BORROW_BOOK", "Book borrowed: " + book.getTitle() + " by " + member.getName());
            return record;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns a borrowed book.
     */
    public void returnBook(String memberId, ISBN isbn) throws LibraryException {
        lock.lock();
        try {
            Member member = memberRepository.findById(UUID.fromString(memberId))
                .orElseThrow(() -> new LibraryException("MEMBER_NOT_FOUND", "Member not found: " + memberId));

            Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new LibraryException("BOOK_NOT_FOUND", "Book not found: " + isbn));

            // Validate member has borrowed this book
            if (!member.getBorrowedBooks().contains(book)) {
                throw new LibraryException("NOT_BORROWED", "Member has not borrowed this book");
            }

            // Return the book
            member.returnBook(book);
            book.returnCopy();

            // Save updated entities
            bookRepository.save(book);
            memberRepository.save(member);

            logAction("RETURN_BOOK", "Book returned: " + book.getTitle() + " by " + member.getName());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets borrowed books for a member.
     */
    public List<Book> getBorrowedBooks(String memberId) throws LibraryException {
        Member member = memberRepository.findById(UUID.fromString(memberId))
            .orElseThrow(() -> new LibraryException("MEMBER_NOT_FOUND", "Member not found: " + memberId));
        return new ArrayList<>(member.getBorrowedBooks());
    }

    // ==================== SEARCH AND FILTER ====================

    /**
     * Advanced search with multiple criteria.
     */
    public List<Book> advancedSearch(String title, String author, String category, boolean onlyAvailable) {
        return bookRepository.findAll(book -> {
            if (book.isDeleted()) return false;
            
            if (title != null && !book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                return false;
            }
            
            if (author != null && !book.getAuthors().stream()
                .anyMatch(a -> a.getName().toLowerCase().contains(author.toLowerCase()))) {
                return false;
            }
            
            if (category != null && !book.getCategories().stream()
                .anyMatch(c -> c.getName().toLowerCase().contains(category.toLowerCase()))) {
                return false;
            }
            
            if (onlyAvailable && !book.isAvailable()) {
                return false;
            }
            
            return true;
        });
    }

    /**
     * Gets book recommendations for a member.
     */
    public List<Book> getRecommendations(String memberId, int maxRecommendations) throws LibraryException {
        Member member = memberRepository.findById(UUID.fromString(memberId))
            .orElseThrow(() -> new LibraryException("MEMBER_NOT_FOUND", "Member not found: " + memberId));

        List<Book> borrowedBooks = new ArrayList<>(member.getBorrowedBooks());
        if (borrowedBooks.isEmpty()) {
            return bookRepository.findAllAvailable().stream()
                .limit(maxRecommendations)
                .collect(Collectors.toList());
        }

        // Get preferred categories and authors
        Set<Category> preferredCategories = borrowedBooks.stream()
            .flatMap(b -> b.getCategories().stream())
            .collect(Collectors.toSet());

        Set<Author> preferredAuthors = borrowedBooks.stream()
            .flatMap(b -> b.getAuthors().stream())
            .collect(Collectors.toSet());

        // Score and rank recommendations
        return bookRepository.findAllAvailable().stream()
            .filter(book -> !borrowedBooks.contains(book))
            .sorted((b1, b2) -> {
                int score1 = calculateRecommendationScore(b1, preferredCategories, preferredAuthors);
                int score2 = calculateRecommendationScore(b2, preferredCategories, preferredAuthors);
                return Integer.compare(score2, score1);
            })
            .limit(maxRecommendations)
            .collect(Collectors.toList());
    }

    private int calculateRecommendationScore(Book book, Set<Category> categories, Set<Author> authors) {
        int score = 0;
        score += book.getCategories().stream()
            .filter(categories::contains)
            .count() * 2;
        score += book.getAuthors().stream()
            .filter(authors::contains)
            .count() * 3;
        score += (int) book.getAverageRating();
        return score;
    }

    // ==================== REPORTING ====================

    /**
     * Gets library statistics.
     */
    public Map<String, Object> getLibraryStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Book> allBooks = bookRepository.findAll();
        List<Member> allMembers = memberRepository.findAll();

        stats.put("totalBooks", allBooks.size());
        stats.put("availableBooks", bookRepository.findAllAvailable().size());
        stats.put("totalMembers", allMembers.size());
        stats.put("activeMembers", memberRepository.findAllActive().size());
        stats.put("totalBorrowings", allBooks.stream()
            .mapToInt(Book::getBorrowingCount)
            .sum());
        stats.put("averageRating", allBooks.stream()
            .mapToDouble(Book::getAverageRating)
            .average()
            .orElse(0.0));

        return stats;
    }

    /**
     * Gets audit logs.
     */
    public List<AuditLog> getAuditLogs() {
        return new ArrayList<>(auditLogs);
    }

    private void logAction(String action, String details) {
        auditLogs.add(new AuditLog(action, details, LocalDate.now()));
    }

    /**
     * Audit log entry.
     */
    public static class AuditLog {
        public final String action;
        public final String details;
        public final LocalDate timestamp;

        public AuditLog(String action, String details, LocalDate timestamp) {
            this.action = action;
            this.details = details;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return timestamp + " - " + action + ": " + details;
        }
    }
}
