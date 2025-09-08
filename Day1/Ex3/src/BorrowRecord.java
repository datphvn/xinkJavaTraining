import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowRecord {
    private final Member member;
    private final Book book;
    private final LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;
    private BorrowStatus status;
    private int renewCount;

    public static final int LOAN_DAYS = 14;
    public static final double DAILY_FINE = 2.0;
    public static final int MAX_RENEWALS = 1;

    public BorrowRecord(Member member, Book book) {
        this.member = member;
        this.book = book;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(LOAN_DAYS);
        this.status = BorrowStatus.BORROWED;
        this.renewCount = 0;
    }

    public void renew() {
        if (status != BorrowStatus.BORROWED) throw new IllegalStateException("Only BORROWED can renew");
        if (renewCount >= MAX_RENEWALS) throw new IllegalStateException("Max renewals reached");
        this.dueDate = dueDate.plusDays(LOAN_DAYS);
        renewCount++;
    }

    public void returnBook() {
        if (status != BorrowStatus.BORROWED) return;
        returnDate = LocalDate.now();
        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            fine = daysLate * DAILY_FINE;
            member.addFine(fine);
            status = BorrowStatus.OVERDUE;
        } else {
            status = BorrowStatus.RETURNED;
        }
        book.returnCopy();
    }

    public double computeFineAsOf(LocalDate today) {
        if (status != BorrowStatus.BORROWED) return fine;
        if (today.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, today);
            return daysLate * DAILY_FINE;
        }
        return 0.0;
    }

    // Getters/Setters (đầy đủ)
    public Member getMember() { return member; }
    public Book getBook() { return book; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
    public BorrowStatus getStatus() { return status; }
    public void setStatus(BorrowStatus status) { this.status = status; }
    public int getRenewCount() { return renewCount; }
    public void setRenewCount(int renewCount) { this.renewCount = renewCount; }

    @Override
    public String toString() {
        return member.getName() + " borrowed '" + book.getTitle() + "' on " + borrowDate
                + " | Due: " + dueDate + " | Status: " + status + " | Fine: " + String.format("%.2f", fine);
    }
}
