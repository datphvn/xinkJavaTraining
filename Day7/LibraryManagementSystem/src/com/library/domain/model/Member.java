package com.library.domain.model;

import com.library.domain.exception.LibraryException;
import com.library.domain.exception.ValidationException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a library member who can borrow and return books.
 * Pure OOP implementation without any framework dependencies.
 */
public class Member extends BaseEntity {
    private final String memberId;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate joinDate;
    private LocalDate dateOfBirth;
    private MemberStatus status;
    private final Set<Book> borrowedBooks;
    private static final int DEFAULT_BORROW_LIMIT = 5;
    private int borrowLimit;

    /**
     * Constructor for creating a new Member.
     * @param memberId Unique identifier for the member
     * @param name The name of the member
     * @param email The email of the member
     * @param phoneNumber The phone number of the member
     * @param dateOfBirth The date of birth of the member
     * @throws IllegalArgumentException if memberId or name is null or blank
     */
    public Member(String memberId, String name, String email, String phoneNumber, LocalDate dateOfBirth) {
        super();
        this.memberId = Objects.requireNonNull(memberId, "Member ID cannot be null");
        this.name = Objects.requireNonNull(name, "Member name cannot be null");
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.joinDate = LocalDate.now();
        this.dateOfBirth = dateOfBirth;
        this.status = MemberStatus.ACTIVE;
        this.borrowedBooks = new HashSet<>();
        this.borrowLimit = DEFAULT_BORROW_LIMIT;
    }

    // Getters
    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public Set<Book> getBorrowedBooks() {
        return new HashSet<>(borrowedBooks);
    }

    public int getBorrowLimit() {
        return borrowLimit;
    }

    public int getBorrowedBookCount() {
        return borrowedBooks.size();
    }

    // Setters
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Member name cannot be null");
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBorrowLimit(int borrowLimit) {
        if (borrowLimit <= 0) {
            throw new IllegalArgumentException("Borrow limit must be greater than 0");
        }
        this.borrowLimit = borrowLimit;
    }

    // Business methods

    /**
     * Borrows a book for this member.
     * @param book The book to borrow
     * @throws LibraryException if the book is not available or borrow limit is reached
     */
    public void borrowBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");

        if (status != MemberStatus.ACTIVE) {
            throw new LibraryException("Member is not active. Current status: " + status);
        }

        if (!book.isAvailable()) {
            throw new LibraryException("Book is not available for borrowing. Status: " + book.getStatus());
        }

        if (borrowedBooks.size() >= borrowLimit) {
            throw new LibraryException("Borrow limit reached. Current limit: " + borrowLimit);
        }

        book.borrow();
        borrowedBooks.add(book);
    }

    /**
     * Returns a borrowed book.
     * @param book The book to return
     * @throws LibraryException if the member didn't borrow this book
     */
    public void returnBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");

        if (!borrowedBooks.contains(book)) {
            throw new LibraryException("This member didn't borrow this book");
        }

        borrowedBooks.remove(book);
        book.returnBook();
    }

    /**
     * Suspends the member account.
     */
    public void suspend() {
        status = MemberStatus.SUSPENDED;
    }

    /**
     * Reactivates the member account.
     */
    public void reactivate() {
        status = MemberStatus.ACTIVE;
    }

    /**
     * Deactivates the member account.
     */
    public void deactivate() {
        status = MemberStatus.INACTIVE;
    }

    /**
     * Checks if the member can borrow more books.
     * @return true if the member can borrow more books, false otherwise
     */
    public boolean canBorrowMore() {
        return status == MemberStatus.ACTIVE && borrowedBooks.size() < borrowLimit;
    }

    /**
     * Gets the number of available slots for borrowing.
     * @return The number of available slots
     */
    public int getAvailableBorrowSlots() {
        return borrowLimit - borrowedBooks.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }

    @Override
    public String toString() {
        return String.format("Member[id=%s, name=%s, status=%s, borrowed=%d/%d books]",
                memberId, name, status, borrowedBooks.size(), borrowLimit);
    }

    @Override
    public void validate() throws ValidationException {
        Map<String, String> errors = new HashMap<>();
        if (memberId == null || memberId.isBlank()) {
            errors.put("memberId", "Member ID is required");
        }
        if (name == null || name.isBlank()) {
            errors.put("name", "Member name is required");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public String getSummary() {
        return String.format("Member[id=%s, name=%s, status=%s, borrowed=%d/%d]",
                memberId, name, status, borrowedBooks.size(), borrowLimit);
    }

    public java.util.Map<String, Object> toMap() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("memberId", memberId);
        map.put("name", name);
        map.put("email", email);
        map.put("phoneNumber", phoneNumber);
        map.put("joinDate", joinDate);
        map.put("dateOfBirth", dateOfBirth);
        map.put("status", status);
        map.put("borrowedBookCount", borrowedBooks.size());
        map.put("borrowLimit", borrowLimit);
        return map;
    }

    @Override
    public void fromMap(java.util.Map<String, Object> data) {
        if (data == null) {
            return;
        }
        if (data.containsKey("name")) {
            this.name = (String) data.get("name");
        }
        if (data.containsKey("email")) {
            this.email = (String) data.get("email");
        }
        if (data.containsKey("phoneNumber")) {
            this.phoneNumber = (String) data.get("phoneNumber");
        }
        if (data.containsKey("dateOfBirth")) {
            this.dateOfBirth = (LocalDate) data.get("dateOfBirth");
        }
        if (data.containsKey("status")) {
            this.status = (MemberStatus) data.get("status");
        }
        if (data.containsKey("borrowLimit")) {
            this.borrowLimit = (Integer) data.get("borrowLimit");
        }
    }
}
