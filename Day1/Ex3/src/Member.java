import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private final String memberId;
    private String name;
    private String email;
    private LocalDate membershipDate;
    private List<BorrowRecord> borrowHistory;
    private double outstandingFines;
    private int maxBooksAllowed;

    public Member(String memberId, String name, String email, int maxBooksAllowed) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.membershipDate = LocalDate.now();
        this.borrowHistory = new ArrayList<>();
        this.outstandingFines = 0.0;
        this.maxBooksAllowed = maxBooksAllowed;
    }

    // Borrowing rules
    public boolean canBorrow() {
        long active = borrowHistory.stream().filter(r -> r.getStatus() == BorrowStatus.BORROWED).count();
        return active < maxBooksAllowed && outstandingFines <= 50.0;
    }

    // Fine management
    public void addFine(double fine) { outstandingFines += fine; }
    public void payFine(double amount) { outstandingFines = Math.max(0, outstandingFines - amount); }

    // Borrowing
    public void addBorrowRecord(BorrowRecord record) { borrowHistory.add(record); }

    // Getters & setters
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getMembershipDate() { return membershipDate; }
    public void setMembershipDate(LocalDate membershipDate) { this.membershipDate = membershipDate; }
    public List<BorrowRecord> getBorrowHistory() { return borrowHistory; }
    public double getOutstandingFines() { return outstandingFines; }
    public void setOutstandingFines(double outstandingFines) { this.outstandingFines = outstandingFines; }
    public int getMaxBooksAllowed() { return maxBooksAllowed; }
    public void setMaxBooksAllowed(int maxBooksAllowed) { this.maxBooksAllowed = maxBooksAllowed; }

    @Override
    public String toString() {
        return String.format("Member[%s] %s | Fines: %.2f | Books: %d", memberId, name, outstandingFines, borrowHistory.size());
    }
}
