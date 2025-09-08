import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryManagementSystem {
    private List<Book> books;
    private List<Member> members;
    private List<BorrowRecord> borrowRecords;
    private Map<String, List<Book>> categoryIndex;

    // Advanced
    private Map<String, Deque<Reservation>> reservations; // isbn -> queue
    private NotificationService notificationService;

    public LibraryManagementSystem() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.borrowRecords = new ArrayList<>();
        this.categoryIndex = new HashMap<>();
        this.reservations = new HashMap<>();
        this.notificationService = new NotificationService();
    }

    // ---------------- Book & Member Management (Inventory) ----------------
    public void addBook(Book book) {
        books.add(book);
        categoryIndex.computeIfAbsent(book.getCategory(), k -> new ArrayList<>()).add(book);
    }

    public void removeBook(String isbn) {
        Book b = findBook(isbn);
        if (b == null) return;
        if (b.getFormat() == BookFormat.PHYSICAL && b.getAvailableCopies() < b.getTotalCopies())
            throw new IllegalStateException("Cannot remove; some copies are borrowed");
        books.remove(b);
        categoryIndex.getOrDefault(b.getCategory(), new ArrayList<>()).remove(b);
        reservations.remove(isbn);
    }

    public void addCopies(String isbn, int n) { Book b = findBook(isbn); if (b != null) b.addCopies(n); }
    public void removeCopies(String isbn, int n) { Book b = findBook(isbn); if (b != null) b.removeCopies(n); }

    public List<Book> lowStockReport(int threshold) {
        return books.stream()
                .filter(b -> b.getFormat() == BookFormat.PHYSICAL && b.getAvailableCopies() <= threshold)
                .collect(Collectors.toList());
    }

    public void registerMember(Member m) { members.add(m); }
    public void removeMember(String memberId) { members.removeIf(m -> m.getMemberId().equals(memberId)); }

    // ---------------- Borrowing / Returning / Reservation ----------------
    public boolean borrowBook(String memberId, String isbn) {
        Member m = findMember(memberId);
        Book b = findBook(isbn);
        if (m == null || b == null) return false;
        // Nếu có người đang chờ và không phải là người đầu hàng đợi, không cho mượn
        Deque<Reservation> q = reservations.getOrDefault(isbn, new ArrayDeque<>());
        if (!q.isEmpty() && (q.peekFirst() == null || !q.peekFirst().getMember().equals(m))) {
            return false;
        }
        if (m.canBorrow() && b.borrowCopy()) {
            BorrowRecord r = new BorrowRecord(m, b);
            borrowRecords.add(r);
            m.addBorrowRecord(r);
            // nếu m là người đầu queue -> lấy ra
            if (!q.isEmpty() && q.peekFirst().getMember().equals(m)) q.pollFirst();
            return true;
        } else {
            // hết sách -> thêm đặt chỗ
            reserveBook(memberId, isbn);
            return false;
        }
    }

    public void returnBook(String memberId, String isbn) {
        Optional<BorrowRecord> opt = borrowRecords.stream()
                .filter(r -> r.getMember().getMemberId().equals(memberId)
                        && r.getBook().getIsbn().equals(isbn)
                        && r.getStatus() == BorrowStatus.BORROWED)
                .findFirst();
        if (opt.isEmpty()) return;
        BorrowRecord rec = opt.get();
        rec.returnBook();
        Book b = rec.getBook();

        // nếu có người chờ -> báo ready
        Deque<Reservation> q = reservations.getOrDefault(isbn, new ArrayDeque<>());
        if (!q.isEmpty()) {
            Reservation next = q.peekFirst();
            notificationService.sendReservationReady(next.getMember(), b);
        }
    }

    public void reserveBook(String memberId, String isbn) {
        Member m = findMember(memberId);
        Book b = findBook(isbn);
        if (m == null || b == null) return;
        reservations.computeIfAbsent(isbn, k -> new ArrayDeque<>()).addLast(new Reservation(isbn, m));
    }

    // ---------------- Search ----------------
    public List<Book> searchBooks(String keyword) {
        return books.stream().filter(b -> b.matches(keyword)).collect(Collectors.toList());
    }

    public List<Book> searchByCategory(String category) {
        return new ArrayList<>(categoryIndex.getOrDefault(category, Collections.emptyList()));
    }

    // ---------------- Notifications (Late return) ----------------
    // Chạy mỗi ngày: gửi nhắc trước hạn 2 ngày; quá hạn -> gửi thông báo + cộng dồn ước tính
    public void processDailyNotifications(LocalDate today) {
        for (BorrowRecord r : borrowRecords) {
            if (r.getStatus() != BorrowStatus.BORROWED) continue;
            LocalDate due = r.getDueDate();
            if (today.equals(due.minusDays(2))) {
                notificationService.sendDueReminder(r.getMember(), r.getBook(), due);
            } else if (today.isAfter(due)) {
                double fineSoFar = r.computeFineAsOf(today);
                notificationService.sendOverdueNotice(r.getMember(), r.getBook(), due, fineSoFar);
            }
        }
    }

    // ---------------- Reports (Analytics) ----------------
    public List<Book> popularBooks(int topK) {
        Map<Book, Long> count = borrowRecords.stream()
                .collect(Collectors.groupingBy(BorrowRecord::getBook, Collectors.counting()));
        return count.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(topK)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public String memberActivityReport(String memberId) {
        Member m = findMember(memberId);
        if (m == null) return "Member not found";
        StringBuilder sb = new StringBuilder("Activity Report for " + m.getName() + " (" + m.getMemberId() + ")\n");
        sb.append("Outstanding fines: ").append(String.format("%.2f", m.getOutstandingFines())).append("\n");
        long borrowed = m.getBorrowHistory().stream().filter(r -> r.getStatus() == BorrowStatus.BORROWED).count();
        long returned = m.getBorrowHistory().stream().filter(r -> r.getStatus() == BorrowStatus.RETURNED).count();
        long overdue = m.getBorrowHistory().stream().filter(r -> r.getStatus() == BorrowStatus.OVERDUE).count();
        sb.append("Borrowed: ").append(borrowed)
                .append(" | Returned: ").append(returned)
                .append(" | Overdue: ").append(overdue).append("\n");
        sb.append("Recent records:\n");
        m.getBorrowHistory().stream().limit(10).forEach(r -> sb.append(" - ").append(r).append("\n"));
        return sb.toString();
    }

    // ---------------- Helpers & Getters ----------------
    private Member findMember(String id) {
        return members.stream().filter(m -> m.getMemberId().equals(id)).findFirst().orElse(null);
    }
    private Book findBook(String isbn) {
        return books.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().orElse(null);
    }

    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
    public List<Member> getMembers() { return members; }
    public void setMembers(List<Member> members) { this.members = members; }
    public List<BorrowRecord> getBorrowRecords() { return borrowRecords; }
    public void setBorrowRecords(List<BorrowRecord> borrowRecords) { this.borrowRecords = borrowRecords; }
    public Map<String, List<Book>> getCategoryIndex() { return categoryIndex; }
    public void setCategoryIndex(Map<String, List<Book>> categoryIndex) { this.categoryIndex = categoryIndex; }
    public Map<String, Deque<Reservation>> getReservations() { return reservations; }
    public void setReservations(Map<String, Deque<Reservation>> reservations) { this.reservations = reservations; }
    public NotificationService getNotificationService() { return notificationService; }
    public void setNotificationService(NotificationService notificationService) { this.notificationService = notificationService; }
}
