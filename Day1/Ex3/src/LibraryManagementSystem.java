import java.util.*;
import java.util.stream.Collectors;

public class LibraryManagementSystem {
    private List<Book> books;
    private List<Member> members;
    private List<BorrowRecord> borrowRecords;
    private Map<String, List<Book>> categoryIndex;

    public LibraryManagementSystem() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.borrowRecords = new ArrayList<>();
        this.categoryIndex = new HashMap<>();
    }

    // Book & member management
    public void addBook(Book book) {
        books.add(book);
        categoryIndex.computeIfAbsent(book.getCategory(), k -> new ArrayList<>()).add(book);
    }
    public void removeBook(String isbn) { books.removeIf(b -> b.getIsbn().equals(isbn)); }
    public void registerMember(Member member) { members.add(member); }

    // Borrowing/returning
    public boolean borrowBook(String memberId, String isbn) {
        Member m = findMember(memberId);
        Book b = findBook(isbn);
        if (m != null && b != null && m.canBorrow() && b.borrowCopy()) {
            BorrowRecord r = new BorrowRecord(m, b);
            borrowRecords.add(r);
            m.addBorrowRecord(r);
            return true;
        }
        return false;
    }

    public void returnBook(String memberId, String isbn) {
        borrowRecords.stream()
                .filter(r -> r.getMember().getMemberId().equals(memberId)
                        && r.getBook().getIsbn().equals(isbn)
                        && r.getStatus() == BorrowStatus.BORROWED)
                .findFirst()
                .ifPresent(BorrowRecord::returnBook);
    }

    // Search
    public List<Book> searchBooks(String keyword) {
        return books.stream().filter(b -> b.matches(keyword)).collect(Collectors.toList());
    }

    // Reports
    public List<Book> getPopularBooks() {
        Map<Book, Long> count = borrowRecords.stream()
                .collect(Collectors.groupingBy(BorrowRecord::getBook, Collectors.counting()));
        return count.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Helpers
    private Member findMember(String id) {
        return members.stream().filter(m -> m.getMemberId().equals(id)).findFirst().orElse(null);
    }
    private Book findBook(String isbn) {
        return books.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().orElse(null);
    }

    // Getters
    public List<Book> getBooks() { return books; }
    public List<Member> getMembers() { return members; }
    public List<BorrowRecord> getBorrowRecords() { return borrowRecords; }
}
