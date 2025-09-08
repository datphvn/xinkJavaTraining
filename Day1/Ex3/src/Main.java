import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LibraryManagementSystem lib = new LibraryManagementSystem();

        // Inventory: add books (PHYSICAL & DIGITAL)
        Book b1 = new Book("1234567890", "Java Programming", "James Gosling", "Programming",
                3, LocalDate.of(2020,1,1), BookFormat.PHYSICAL);
        Book b2 = new Book("9876543210", "Data Structures", "Robert Lafore", "CS",
                2, LocalDate.of(2018,5,5), BookFormat.PHYSICAL);
        Book b3 = new Book("1111111111111", "Clean Architecture (eBook)", "Robert C. Martin", "Programming",
                0, LocalDate.of(2017,9,1), BookFormat.DIGITAL);
        lib.addBook(b1);
        lib.addBook(b2);
        lib.addBook(b3);

        // Members
        Member m1 = new Member("M001", "Alice", "alice@mail.com", 3);
        Member m2 = new Member("M002", "Bob", "bob@mail.com", 2);
        Member m3 = new Member("M003", "Carol", "carol@mail.com", 2);
        lib.registerMember(m1);
        lib.registerMember(m2);
        lib.registerMember(m3);

        // Borrow PHYSICAL until out-of-stock, then auto-reserve
        System.out.println("Borrow attempts on PHYSICAL:");
        System.out.println("m1 -> b1: " + lib.borrowBook("M001", "1234567890")); // ok
        System.out.println("m2 -> b1: " + lib.borrowBook("M002", "1234567890")); // ok
        System.out.println("m3 -> b1: " + lib.borrowBook("M003", "1234567890")); // ok (last copy)
        System.out.println("m1 again -> b1 (should reserve): " + lib.borrowBook("M001", "1234567890")); // reserve

        // Borrow DIGITAL (unlimited copies)
        System.out.println("Borrow eBook:");
        System.out.println("m2 -> b3: " + lib.borrowBook("M002", "1111111111111")); // always ok

        // Process notifications (simulate dates)
        System.out.println("\n-- Notifications simulation --");
        LocalDate today = LocalDate.now();
        lib.processDailyNotifications(today.plusDays(BorrowRecord.LOAN_DAYS - 2)); // reminder 2 days before due
        lib.processDailyNotifications(today.plusDays(BorrowRecord.LOAN_DAYS + 1)); // overdue notice

        // Return one physical copy -> trigger reservation ready for next in queue
        System.out.println("\n-- Return & Reservation Ready --");
        lib.returnBook("M002", "1234567890"); // free 1 copy -> notify first reservation

        // Popular books analytics
        System.out.println("\nTop popular books:");
        lib.popularBooks(5).forEach(System.out::println);

        // Member activity report
        System.out.println("\nMember activity:");
        System.out.println(lib.memberActivityReport("M001"));

        // Inventory management
        System.out.println("\nLow stock report (threshold=1):");
        lib.lowStockReport(1).forEach(System.out::println);
        lib.addCopies("9876543210", 2);
        lib.removeCopies("9876543210", 1);
        System.out.println("After inventory updates:");
        lib.getBooks().forEach(System.out::println);
    }
}
