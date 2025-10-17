package com.library.domain.model;

import com.library.domain.exception.ValidationException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a record of a book being borrowed by a member.
 * Tracks borrowing history and due dates.
 * Pure OOP implementation without any framework dependencies.
 */
public class BorrowRecord extends BaseEntity {
    private String recordId;
    private final Member member;
    private final Book book;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private BorrowRecordStatus status;
    private static final int DEFAULT_BORROW_DURATION_DAYS = 14;

    /**
     * Constructor for creating a new BorrowRecord.
     * @param recordId Unique identifier for the record
     * @param member The member who borrowed the book
     * @param book The book being borrowed
     * @param borrowDate The date the book was borrowed
     * @param durationDays The number of days the book can be borrowed
     * @throws IllegalArgumentException if any required parameter is null
     */
    public BorrowRecord(String recordId, Member member, Book book, LocalDate borrowDate, int durationDays) {
        super();
        this.recordId = Objects.requireNonNull(recordId, "Record ID cannot be null");
        this.member = Objects.requireNonNull(member, "Member cannot be null");
        this.book = Objects.requireNonNull(book, "Book cannot be null");
        this.borrowDate = Objects.requireNonNull(borrowDate, "Borrow date cannot be null");
        this.dueDate = borrowDate.plusDays(durationDays);
        this.returnDate = null;
        this.status = BorrowRecordStatus.BORROWED;
    }

    /**
     * Constructor with default borrow duration.
     */
    public BorrowRecord(String recordId, Member member, Book book, LocalDate borrowDate) {
        this(recordId, member, book, borrowDate, DEFAULT_BORROW_DURATION_DAYS);
    }

    // Getters
    public String getRecordId() {
        return recordId;
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public BorrowRecordStatus getStatus() {
        return status;
    }

    // Business methods

    /**
     * Marks the book as returned.
     * @param returnDate The date the book was returned
     */
    public void markReturned(LocalDate returnDate) {
        Objects.requireNonNull(returnDate, "Return date cannot be null");
        this.returnDate = returnDate;
        this.status = BorrowRecordStatus.RETURNED;
    }

    /**
     * Checks if the borrow record is overdue.
     * @return true if the book is overdue, false otherwise
     */
    public boolean isOverdue() {
        return status == BorrowRecordStatus.BORROWED && LocalDate.now().isAfter(dueDate);
    }

    /**
     * Checks if the book has been returned.
     * @return true if the book has been returned, false otherwise
     */
    public boolean isReturned() {
        return status == BorrowRecordStatus.RETURNED;
    }

    /**
     * Gets the number of days the book is borrowed.
     * @return The number of days
     */
    public long getBorrowDurationDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(borrowDate, 
            returnDate != null ? returnDate : LocalDate.now());
    }

    /**
     * Gets the number of days overdue.
     * @return The number of days overdue, or 0 if not overdue
     */
    public long getOverdueDays() {
        if (!isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BorrowRecord)) return false;
        BorrowRecord record = (BorrowRecord) o;
        return Objects.equals(recordId, record.recordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId);
    }

    @Override
    public String toString() {
        return String.format("BorrowRecord[id=%s, %s borrowed %s on %s, due %s, status=%s]",
                recordId, member.getName(), book.getTitle(), borrowDate, dueDate, status);
    }

    @Override
    public void validate() throws ValidationException {
        Map<String, String> errors = new HashMap<>();
        if (recordId == null || recordId.isBlank()) {
            errors.put("recordId", "Record ID is required");
        }
        if (member == null) {
            errors.put("member", "Member is required");
        }
        if (book == null) {
            errors.put("book", "Book is required");
        }
        if (borrowDate == null) {
            errors.put("borrowDate", "Borrow date is required");
        }
        if (dueDate == null) {
            errors.put("dueDate", "Due date is required");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public String getSummary() {
        return String.format("BorrowRecord[%s -> %s, due %s, status=%s]",
                member.getName(), book.getTitle(), dueDate, status);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("recordId", recordId);
        map.put("memberId", member.getMemberId());
        map.put("bookIsbn", book.getIsbn().getValue());
        map.put("borrowDate", borrowDate);
        map.put("dueDate", dueDate);
        map.put("returnDate", returnDate);
        map.put("status", status);
        map.put("borrowDurationDays", getBorrowDurationDays());
        map.put("overdueDays", getOverdueDays());
        return map;
    }

    @Override
    public void fromMap(Map<String, Object> data) {
        if (data == null) {
            return;
        }
        if (data.containsKey("status")) {
            this.status = BorrowRecordStatus.valueOf((String) data.get("status"));
        }
        if (data.containsKey("returnDate")) {
            this.returnDate = (LocalDate) data.get("returnDate");
        }
    }
}
