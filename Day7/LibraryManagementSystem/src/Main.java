import com.library.application.facade.LibraryFacade;
import com.library.application.facade.LibraryFacadeImpl;
import com.library.domain.model.*;
import com.library.domain.repository.BookRepository;
import com.library.domain.repository.MemberRepository;
import com.library.domain.service.*;
import com.library.domain.valueobject.ISBN;
import com.library.infrastructure.repository.InMemoryBookRepository;
import com.library.infrastructure.repository.InMemoryMemberRepository;
import com.library.ui.cli.LibraryCLI;
import java.time.LocalDate;
import java.time.Year;
import java.util.UUID;

/**
 * Main entry point for Library Management System.
 * Initializes all components and starts the CLI.
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Initialize repositories
            BookRepository bookRepository = new InMemoryBookRepository();
            MemberRepository memberRepository = new InMemoryMemberRepository();

            // Initialize rule engine
            RuleEngine ruleEngine = new RuleEngine();
            ruleEngine.registerRule(Member.class, new MemberStatusRule());
            ruleEngine.registerRule(Member.class, new BorrowingLimitRule());
            ruleEngine.registerRule(Book.class, new BookAvailabilityRule());

            // Initialize workflow engine
            WorkflowEngine workflowEngine = new WorkflowEngine();
            workflowEngine.registerWorkflow(new BookBorrowingWorkflow(ruleEngine));

            // Initialize service
            LibraryService libraryService = new LibraryService(
                bookRepository,
                memberRepository,
                ruleEngine,
                workflowEngine
            );

            // Initialize facade
            LibraryFacade facade = new LibraryFacadeImpl(libraryService);

            // Seed sample data
            seedSampleData(libraryService, bookRepository, memberRepository);

            // Start CLI
            LibraryCLI cli = new LibraryCLI(facade);
            cli.start();

        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void seedSampleData(LibraryService libraryService,
                                      BookRepository bookRepository,
                                      MemberRepository memberRepository) {
        try {
            // Add sample authors
            Author author1 = new Author("AU001", "J.K. Rowling", LocalDate.of(1965, 7, 31), "British", "Author of Harry Potter");
            Author author2 = new Author("AU002", "George Orwell", LocalDate.of(1903, 6, 25), "British", "Author of 1984");

            // Add sample categories
            Category category1 = new Category("CAT001", "Fantasy", "Fantasy novels");
            Category category2 = new Category("CAT002", "Dystopian", "Dystopian fiction");

            // Add sample books
            ISBN isbn1 = new ISBN("978-0747532699");
            Book book1 = new Book(isbn1, "Harry Potter and the Philosopher's Stone", "Bloomsbury", Year.of(1997));
            book1.addAuthor(author1);
            book1.addCategory(category1);
            book1.setPages(309);
            book1.addCopies(5);
            bookRepository.save(book1);

            ISBN isbn2 = new ISBN("978-0451524935");
            Book book2 = new Book(isbn2, "1984", "Secker & Warburg", Year.of(1949));
            book2.addAuthor(author2);
            book2.addCategory(category2);
            book2.setPages(328);
            book2.addCopies(3);
            bookRepository.save(book2);

            // Add sample members
            Member member1 = new Member(UUID.randomUUID().toString(), "Alice Johnson", "alice@example.com", "555-0001", LocalDate.of(1990, 1, 15));
            Member member2 = new Member(UUID.randomUUID().toString(), "Bob Smith", "bob@example.com", "555-0002", LocalDate.of(1985, 5, 20));
            memberRepository.save(member1);
            memberRepository.save(member2);

            System.out.println("✓ Sample data loaded successfully!");
            System.out.println("  - 2 books added");
            System.out.println("  - 2 members registered");
            System.out.println();

        } catch (Exception e) {
            System.err.println("Warning: Could not seed sample data: " + e.getMessage());
        }
    }
}