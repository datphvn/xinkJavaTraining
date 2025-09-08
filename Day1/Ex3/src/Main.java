import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LibraryManagementSystem lib = new LibraryManagementSystem();

        // Add books
        Book b1 = new Book("1234567890", "Java Programming", "James Gosling", "Programming", 5, LocalDate.of(2020, 1, 1));
        Book b2 = new Book("9876543210", "Data Structures", "Robert Lafore", "CS", 3, LocalDate.of(2018, 5, 5));
        lib.addBook(b1);
        lib.addBook(b2);

        // Register members
        Member m1 = new Member("M001", "Alice", "alice@mail.com", 3);
        Member m2 = new Member("M002", "Bob", "bob@mail.com", 2);
        lib.registerMember(m1);
        lib.registerMember(m2);

        // Borrow books
        lib.borrowBook("M001", "1234567890");
        lib.borrowBook("M001", "9876543210");

        // Return one book
        lib.returnBook("M001", "9876543210");

        // Search books
        System.out.println("Search for 'Java': " + lib.searchBooks("Java"));

        // Report popular books
        System.out.println("Popular books: " + lib.getPopularBooks());
    }
}
