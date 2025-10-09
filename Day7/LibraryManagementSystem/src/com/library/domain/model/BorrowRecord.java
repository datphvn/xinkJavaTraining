package com.library.domain.model;

import java.time.LocalDate;

public class BorrowRecord {
    private Member member;
    private Book book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean returned;

    public BorrowRecord(Member member, Book book, LocalDate borrowDate, int durationDays) {
        this.member = member;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(durationDays);
        this.returned = false;
    }

    public void markReturned() { this.returned = true; }
    public boolean isOverdue() { return !returned && LocalDate.now().isAfter(dueDate); }

    @Override
    public String toString() {
        return String.format("BorrowRecord[%s borrowed %s on %s, due %s]",
                member.getName(), book.getTitle(), borrowDate, dueDate);
    }
}
