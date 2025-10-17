package com.library.infrastructure.repository;

import com.library.domain.model.Book;
import com.library.domain.model.BorrowRecord;
import com.library.domain.model.BorrowRecordStatus;
import com.library.domain.model.Member;
import com.library.domain.repository.BorrowingRepository;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of BorrowingRepository.
 * Tracks borrowing and returning of books by members.
 * Pure OOP implementation without any framework dependencies.
 */
public class InMemoryBorrowingRepository implements BorrowingRepository {
    private final Map<String, BorrowRecord> borrowRecords;
    private int recordCounter;

    public InMemoryBorrowingRepository() {
        this.borrowRecords = new ConcurrentHashMap<>();
        this.recordCounter = 0;
    }

    @Override
    public void recordBorrow(Member member, Book book) {
        Objects.requireNonNull(member, "Member cannot be null");
        Objects.requireNonNull(book, "Book cannot be null");

        String recordId = "BR-" + (++recordCounter);
        BorrowRecord record = new BorrowRecord(recordId, member, book, LocalDate.now());
        borrowRecords.put(recordId, record);
    }

    @Override
    public void recordReturn(Member member, Book book) {
        Objects.requireNonNull(member, "Member cannot be null");
        Objects.requireNonNull(book, "Book cannot be null");

        // Find the active borrow record for this member and book
        Optional<BorrowRecord> record = borrowRecords.values().stream()
            .filter(br -> br.getMember().equals(member) &&
                         br.getBook().equals(book) &&
                         br.getStatus() == BorrowRecordStatus.BORROWED)
            .findFirst();

        if (record.isPresent()) {
            record.get().markReturned(LocalDate.now());
        }
    }

    @Override
    public List<Book> findBorrowedBooksByMember(Member member) {
        Objects.requireNonNull(member, "Member cannot be null");
        return borrowRecords.values().stream()
            .filter(record -> record.getMember().equals(member) &&
                            record.getStatus() == BorrowRecordStatus.BORROWED)
            .map(BorrowRecord::getBook)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Member> findBorrowerOfBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        return borrowRecords.values().stream()
            .filter(record -> record.getBook().equals(book) &&
                            record.getStatus() == BorrowRecordStatus.BORROWED)
            .map(BorrowRecord::getMember)
            .findFirst();
    }

    /**
     * Gets all borrow records.
     * @return A list of all borrow records
     */
    public List<BorrowRecord> findAllRecords() {
        return new ArrayList<>(borrowRecords.values());
    }

    /**
     * Gets all active (not returned) borrow records.
     * @return A list of active borrow records
     */
    public List<BorrowRecord> findAllActiveRecords() {
        return borrowRecords.values().stream()
            .filter(record -> record.getStatus() == BorrowRecordStatus.BORROWED)
            .collect(Collectors.toList());
    }

    /**
     * Gets all overdue borrow records.
     * @return A list of overdue borrow records
     */
    public List<BorrowRecord> findAllOverdueRecords() {
        return borrowRecords.values().stream()
            .filter(BorrowRecord::isOverdue)
            .collect(Collectors.toList());
    }

    /**
     * Gets the borrow record by ID.
     * @param recordId The record ID
     * @return The borrow record, or empty if not found
     */
    public Optional<BorrowRecord> findRecordById(String recordId) {
        return Optional.ofNullable(borrowRecords.get(recordId));
    }

    /**
     * Clears all borrow records.
     */
    public void clearAllRecords() {
        borrowRecords.clear();
        recordCounter = 0;
    }
}
