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

    public BorrowRecord(Member member, Book book) {
        this.member = member;
        this.book = book;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(14);
        this.status = BorrowStatus.BORROWED;
    }

    public void returnBook() {
        returnDate = LocalDate.now();
        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            fine = daysLate * 2.0;
            member.addFine(fine);
            status = BorrowStatus.OVERDUE;
        } else {
            status = BorrowStatus.RETURNED;
        }
        book.returnCopy();
    }

    // Getters & setters
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

    @Override
    public String toString() {
        return String.format("%s borrowed %s on %s | Due: %s | Status: %s | Fine: %.2f",
                member.getName(), book.getTitle(), borrowDate, dueDate, status, fine);
    }
}
