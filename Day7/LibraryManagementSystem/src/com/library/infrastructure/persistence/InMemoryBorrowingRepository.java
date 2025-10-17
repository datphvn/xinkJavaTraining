package com.library.infrastructure.persistence;

import com.library.domain.model.Book;
import com.library.domain.model.Member;
import com.library.domain.repository.BorrowingRepository;
import com.library.domain.valueobject.ISBN;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of BorrowingRepository.
 * Tracks borrowing and returning of books by members.
 * Pure OOP implementation without any framework dependencies.
 */
public class InMemoryBorrowingRepository implements BorrowingRepository {

    private final Map<String, List<Book>> memberBorrowMap;
    private final Map<ISBN, String> bookBorrowerMap;

    public InMemoryBorrowingRepository() {
        this.memberBorrowMap = new ConcurrentHashMap<>();
        this.bookBorrowerMap = new ConcurrentHashMap<>();
    }

    @Override
    public void recordBorrow(Member member, Book book) {
        Objects.requireNonNull(member, "Member cannot be null");
        Objects.requireNonNull(book, "Book cannot be null");

        memberBorrowMap.computeIfAbsent(member.getMemberId(), k -> new ArrayList<>()).add(book);
        bookBorrowerMap.put(book.getIsbn(), member.getMemberId());
    }

    @Override
    public void recordReturn(Member member, Book book) {
        Objects.requireNonNull(member, "Member cannot be null");
        Objects.requireNonNull(book, "Book cannot be null");

        List<Book> borrowed = memberBorrowMap.getOrDefault(member.getMemberId(), new ArrayList<>());
        borrowed.remove(book);
        bookBorrowerMap.remove(book.getIsbn());
    }

    @Override
    public List<Book> findBorrowedBooksByMember(Member member) {
        Objects.requireNonNull(member, "Member cannot be null");
        return new ArrayList<>(memberBorrowMap.getOrDefault(member.getMemberId(), Collections.emptyList()));
    }

    @Override
    public Optional<Member> findBorrowerOfBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        String memberId = bookBorrowerMap.get(book.getIsbn());
        // Note: We only have the member ID stored. To get the full Member object,
        // the caller should use the MemberRepository to fetch the member by ID.
        // For now, we return empty Optional since we don't have access to the full Member data.
        return Optional.empty();
    }

    /**
     * Gets the member ID who borrowed a specific book.
     * @param book The book to check
     * @return An Optional containing the member ID, or empty if not borrowed
     */
    public Optional<String> findBorrowerIdOfBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        return Optional.ofNullable(bookBorrowerMap.get(book.getIsbn()));
    }

    /**
     * Gets all borrow records (member ID -> list of borrowed books).
     * @return A map of member IDs to their borrowed books
     */
    public Map<String, List<Book>> getAllBorrowRecords() {
        return new ConcurrentHashMap<>(memberBorrowMap);
    }

    /**
     * Gets all book borrower mappings (ISBN -> member ID).
     * @return A map of ISBNs to borrower member IDs
     */
    public Map<ISBN, String> getAllBookBorrowers() {
        return new ConcurrentHashMap<>(bookBorrowerMap);
    }

    /**
     * Clears all borrow records.
     */
    public void clearAllRecords() {
        memberBorrowMap.clear();
        bookBorrowerMap.clear();
    }

    /**
     * Gets the number of members with borrowed books.
     * @return The count of members with active borrows
     */
    public int getMemberBorrowCount() {
        return memberBorrowMap.size();
    }

    /**
     * Gets the total number of borrowed books.
     * @return The total count of borrowed books
     */
    public int getTotalBorrowedBooksCount() {
        return bookBorrowerMap.size();
    }
}
