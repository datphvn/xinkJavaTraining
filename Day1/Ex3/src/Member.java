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
    private boolean active;

    public Member(String memberId, String name, String email, int maxBooksAllowed) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.membershipDate = LocalDate.now();
        this.borrowHistory = new ArrayList<>();
        this.outstandingFines = 0.0;
        this.maxBooksAllowed = maxBooksAllowed;
        this.active = true;
    }

    // Business rules
    public boolean canBorrow() {
        if (!active) return false;
        long activeBorrows = borrowHistory.stream().filter(r -> r.getStatus() == BorrowStatus.BORROWED).count();
        return activeBorrows < maxBooksAllowed && outstandingFines <= 50.0;
    }

    public void addBorrowRecord(BorrowRecord rec) { borrowHistory.add(rec); }
    public void addFine(double fine) { outstandingFines += fine; }
    public void payFine(double amount) { outstandingFines = Math.max(0, outstandingFines - amount); }
    public void deactivate() { active = false; }
    public void activate() { active = true; }

    // Getters/Setters (đầy đủ)
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getMembershipDate() { return membershipDate; }
    public void setMembershipDate(LocalDate membershipDate) { this.membershipDate = membershipDate; }
    public List<BorrowRecord> getBorrowHistory() { return borrowHistory; }
    public void setBorrowHistory(List<BorrowRecord> borrowHistory) { this.borrowHistory = borrowHistory; }
    public double getOutstandingFines() { return outstandingFines; }
    public void setOutstandingFines(double outstandingFines) { this.outstandingFines = outstandingFines; }
    public int getMaxBooksAllowed() { return maxBooksAllowed; }
    public void setMaxBooksAllowed(int maxBooksAllowed) { this.maxBooksAllowed = maxBooksAllowed; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Member[" + memberId + "] " + name + " | fines=" + String.format("%.2f", outstandingFines)
                + " | active=" + active + " | borrows=" + borrowHistory.size();
    }
}
