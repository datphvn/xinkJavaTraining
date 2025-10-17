package com.library.test;

import com.library.domain.model.*;
import com.library.domain.repository.BookRepository;
import com.library.domain.valueobject.ISBN;
import com.library.infrastructure.repository.InMemoryBookRepository;
import com.library.infrastructure.persistence.CachedBookRepository;
import com.library.infrastructure.persistence.FileStorageUtil;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.UUID;

/**
 * Test class for repository functionality.
 * Demonstrates book repository operations and caching.
 * Pure OOP implementation without any framework dependencies.
 */
public class RepositoryTest {
    public static void main(String[] args) {
        try {
            System.out.println("========== REPOSITORY TEST ==========\n");

            // Initialize repositories
            BookRepository baseRepo = new InMemoryBookRepository();
            BookRepository repo = new CachedBookRepository(baseRepo);

            // Create author
            Author rowling = new Author("AU001", "J.K. Rowling", LocalDate.of(1965, 7, 31), "British", "Author of Harry Potter");

            // Create categories
            Category fantasy = new Category("CAT001", "Fantasy", "Fantasy novels");

            // Create books with proper constructors
            ISBN isbn1 = new ISBN("978-0439708180");
            Book b1 = new Book(isbn1, "Harry Potter and the Sorcerer's Stone", "Scholastic", Year.of(1998));
            b1.addAuthor(rowling);
            b1.addCategory(fantasy);
            b1.setPages(309);
            b1.addCopies(5);

            ISBN isbn2 = new ISBN("978-0316769174");
            Book b2 = new Book(isbn2, "The Casual Vacancy", "Little, Brown", Year.of(2012));
            b2.addAuthor(rowling);
            b2.addCategory(fantasy);
            b2.setPages(503);
            b2.addCopies(3);

            // Save books
            System.out.println("1. Saving books to repository...");
            Book saved1 = repo.save(b1);
            Book saved2 = repo.save(b2);
            System.out.println("   ✓ 2 books saved successfully\n");

            // Display all books
            System.out.println("2. All books in repository:");
            List<Book> allBooks = repo.findAll();
            for (Book book : allBooks) {
                System.out.println("   - " + book.getTitle() + " (ISBN: " + book.getIsbn().getValue() + ")");
            }
            System.out.println();

            // Find by ID (cache miss)
            System.out.println("3. Finding book by ID (cache miss):");
            UUID bookId = saved1.getId();
            var foundBook1 = repo.findById(bookId);
            if (foundBook1.isPresent()) {
                System.out.println("   ✓ Found: " + foundBook1.get().getTitle());
            }
            System.out.println();

            // Find by ID (cache hit)
            System.out.println("4. Finding same book by ID (cache hit):");
            var foundBook2 = repo.findById(bookId);
            if (foundBook2.isPresent()) {
                System.out.println("   ✓ Found from cache: " + foundBook2.get().getTitle());
            }
            System.out.println();

            // Find by ISBN
            System.out.println("5. Finding book by ISBN:");
            var foundByIsbn = repo.findByIsbn(isbn1);
            if (foundByIsbn.isPresent()) {
                System.out.println("   ✓ Found: " + foundByIsbn.get().getTitle());
            }
            System.out.println();

            // Find by title
            System.out.println("6. Finding books by title:");
            List<Book> byTitle = repo.findByTitle("Harry");
            System.out.println("   ✓ Found " + byTitle.size() + " book(s)");
            for (Book book : byTitle) {
                System.out.println("   - " + book.getTitle());
            }
            System.out.println();

            // Find by author
            System.out.println("7. Finding books by author:");
            List<Book> byAuthor = repo.findByAuthor("Rowling");
            System.out.println("   ✓ Found " + byAuthor.size() + " book(s)");
            for (Book book : byAuthor) {
                System.out.println("   - " + book.getTitle());
            }
            System.out.println();

            // Find available books
            System.out.println("8. Finding available books:");
            List<Book> available = repo.findAllAvailable();
            System.out.println("   ✓ Found " + available.size() + " available book(s)");
            System.out.println();

            // Save to CSV
            System.out.println("9. Saving books to CSV...");
            FileStorageUtil.saveBooksToCSV(repo.findAll());
            System.out.println();

            // Display cache statistics
            System.out.println("10. Cache Statistics:");
            System.out.println("   ✓ Cache size: " + ((CachedBookRepository) repo).getCacheSize() + " items");
            System.out.println("   ✓ Total books in repository: " + repo.count());

            System.out.println("\n========== TEST COMPLETED SUCCESSFULLY ==========");

        } catch (Exception e) {
            System.err.println("Error during test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
