package com.library.domain.model;

import com.library.domain.exception.LibraryException;
import com.library.domain.exception.ValidationException;
import com.library.domain.valueobject.ISBN;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

/**
 * Represents a book in the library system.
 * A book can be borrowed by members and belongs to a specific category.
 * Pure OOP implementation without any framework dependencies.
 */
public class Book extends BaseEntity {
    private final ISBN isbn;
    private String title;
    private String subtitle;
    private String publisher;
    private final Year publicationYear;
    private LocalDate publicationDate;
    private final Set<Author> authors;
    private final Set<Category> categories;
    private Set<String> keywords;
    private BookStatus status;
    private BookFormat format;
    private Integer pages;
    private String description;
    private Integer totalCopies;
    private Integer availableCopies;
    private String digitalUrl;
    private double averageRating;
    private int borrowingCount;

    /**
     * Constructor for creating a new Book.
     * @param isbn The ISBN of the book
     * @param title The title of the book
     * @param publisher The publisher of the book
     * @param publicationYear The year the book was published
     * @throws IllegalArgumentException if any required parameter is null
     */
    public Book(ISBN isbn, String title, String publisher, Year publicationYear) {
        super();
        this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.publisher = Objects.requireNonNull(publisher, "Publisher cannot be null");
        this.publicationYear = Objects.requireNonNull(publicationYear, "Publication year cannot be null");
        this.authors = new HashSet<>();
        this.categories = new HashSet<>();
        this.keywords = new HashSet<>();
        this.status = BookStatus.AVAILABLE;
        this.format = BookFormat.PHYSICAL;
        this.totalCopies = 0;
        this.availableCopies = 0;
        this.averageRating = 0.0;
        this.borrowingCount = 0;
    }

    // Getters
    public ISBN getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public Year getPublicationYear() {
        return publicationYear;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public Set<Author> getAuthors() {
        return new HashSet<>(authors);
    }

    public Set<Category> getCategories() {
        return new HashSet<>(categories);
    }

    public Set<String> getKeywords() {
        return new HashSet<>(keywords);
    }

    public BookStatus getStatus() {
        return status;
    }

    public BookFormat getFormat() {
        return format;
    }

    public Integer getPages() {
        return pages;
    }

    public String getDescription() {
        return description;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public String getDigitalUrl() {
        return digitalUrl;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getBorrowingCount() {
        return borrowingCount;
    }

    // Setters
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    public void setPages(Integer pages) {
        if (pages != null && pages <= 0) {
            throw new IllegalArgumentException("Pages must be greater than 0");
        }
        this.pages = pages;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    public void setFormat(BookFormat format) {
        this.format = Objects.requireNonNull(format, "Format cannot be null");
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    public void setDigitalUrl(String digitalUrl) {
        this.digitalUrl = digitalUrl;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    public void setAverageRating(double averageRating) {
        if (averageRating < 0 || averageRating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        this.averageRating = averageRating;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    // Business logic methods

    /**
     * Adds an author to this book.
     * @param author The author to add
     */
    public void addAuthor(Author author) {
        Objects.requireNonNull(author, "Author cannot be null");
        authors.add(author);
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Removes an author from this book.
     * @param author The author to remove
     */
    public void removeAuthor(Author author) {
        Objects.requireNonNull(author, "Author cannot be null");
        authors.remove(author);
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Adds a category to this book.
     * @param category The category to add
     */
    public void addCategory(Category category) {
        Objects.requireNonNull(category, "Category cannot be null");
        categories.add(category);
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Removes a category from this book.
     * @param category The category to remove
     */
    public void removeCategory(Category category) {
        Objects.requireNonNull(category, "Category cannot be null");
        categories.remove(category);
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Borrows a copy of this book.
     * @throws LibraryException if no copies are available
     */
    public void borrowCopy() {
        if (!canBorrow()) {
            throw new LibraryException("BOOK_NOT_AVAILABLE", 
                "Book cannot be borrowed: " + title);
        }
        if (availableCopies <= 0) {
            throw new LibraryException("NO_AVAILABLE_COPIES", 
                "No available copies of: " + title);
        }
        availableCopies--;
        borrowingCount++;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Returns a copy of this book.
     */
    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
            this.updatedAt = LocalDateTime.now();
            this.version++;
        }
    }

    /**
     * Adds copies of this book to the library.
     * @param count The number of copies to add
     */
    public void addCopies(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be greater than 0");
        }
        totalCopies += count;
        availableCopies += count;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Removes copies of this book from the library.
     * @param count The number of copies to remove
     */
    public void removeCopies(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be greater than 0");
        }
        if (count > availableCopies) {
            throw new IllegalArgumentException("Cannot remove more copies than available");
        }
        totalCopies -= count;
        availableCopies -= count;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Marks the book as borrowed.
     */
    public void markAsBorrowed() {
        status = BookStatus.BORROWED;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Marks the book as returned and available.
     */
    public void markAsReturned() {
        status = BookStatus.AVAILABLE;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Marks the book as lost.
     */
    public void markAsLost() {
        status = BookStatus.LOST;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Marks the book as damaged.
     */
    public void markAsDamaged() {
        status = BookStatus.DAMAGED;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Marks the book as under maintenance.
     */
    public void markAsUnderMaintenance() {
        status = BookStatus.UNDER_MAINTENANCE;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Checks if the book is available for borrowing.
     * @return true if the book is available, false otherwise
     */
    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE && availableCopies > 0;
    }

    /**
     * Checks if the book can be borrowed.
     * @return true if the book can be borrowed, false otherwise
     */
    public boolean canBorrow() {
        return isAvailable() && format != BookFormat.REFERENCE_ONLY;
    }

    /**
     * Borrows this book (decrements available copies and marks as borrowed).
     * @throws LibraryException if the book cannot be borrowed
     */
    public void borrow() {
        if (!canBorrow()) {
            throw new LibraryException("BOOK_NOT_AVAILABLE", "Book cannot be borrowed: " + title);
        }
        if (availableCopies <= 0) {
            throw new LibraryException("NO_AVAILABLE_COPIES", "No available copies of: " + title);
        }
        availableCopies--;
        borrowingCount++;
        status = BookStatus.BORROWED;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Returns this book (increments available copies and marks as available).
     */
    public void returnBook() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
        status = BookStatus.AVAILABLE;
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    /**
     * Checks if this book has a specific author.
     * @param author The author to check
     * @return true if the book has this author, false otherwise
     */
    public boolean hasAuthor(Author author) {
        return authors.contains(author);
    }

    /**
     * Checks if this book belongs to a specific category.
     * @param category The category to check
     * @return true if the book belongs to this category, false otherwise
     */
    public boolean belongsToCategory(Category category) {
        return categories.contains(category);
    }

    /**
     * Adds a keyword to the book.
     * @param keyword The keyword to add
     */
    public void addKeyword(String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            keywords.add(keyword.toLowerCase());
            this.updatedAt = LocalDateTime.now();
            this.version++;
        }
    }

    /**
     * Removes a keyword from the book.
     * @param keyword The keyword to remove
     */
    public void removeKeyword(String keyword) {
        if (keyword != null) {
            keywords.remove(keyword.toLowerCase());
            this.updatedAt = LocalDateTime.now();
            this.version++;
        }
    }

    /**
     * Updates the book details.
     * @param title New title (optional)
     * @param publisher New publisher (optional)
     * @param keywords New set of keywords (optional)
     */
    public void updateDetails(String title, String publisher, Set<String> keywords) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (publisher != null && !publisher.isBlank()) {
            this.publisher = publisher;
        }
        if (keywords != null && !keywords.isEmpty()) {
            this.keywords = new HashSet<>(keywords);
        }
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    @Override
    public void validate() throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (isbn == null) {
            errors.put("isbn", "ISBN is required");
        }

        if (title == null || title.isBlank()) {
            errors.put("title", "Title is required");
        }

        if (authors.isEmpty()) {
            errors.put("authors", "At least one author is required");
        }

        if (categories.isEmpty()) {
            errors.put("categories", "At least one category is required");
        }

        if (totalCopies < 0) {
            errors.put("totalCopies", "Total copies cannot be negative");
        }

        if (availableCopies < 0 || availableCopies > totalCopies) {
            errors.put("availableCopies", "Available copies must be between 0 and total copies");
        }

        if (pages != null && pages <= 0) {
            errors.put("pages", "Pages must be greater than 0");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("isbn", isbn.getValue());
        map.put("title", title);
        map.put("subtitle", subtitle);
        map.put("publisher", publisher);
        map.put("publicationYear", publicationYear);
        map.put("publicationDate", publicationDate);
        map.put("authors", authors.stream().map(Author::getName).toList());
        map.put("categories", categories.stream().map(Category::getName).toList());
        map.put("keywords", new ArrayList<>(keywords));
        map.put("pages", pages);
        map.put("description", description);
        map.put("totalCopies", totalCopies);
        map.put("availableCopies", availableCopies);
        map.put("status", status);
        map.put("format", format);
        map.put("averageRating", averageRating);
        map.put("borrowingCount", borrowingCount);
        map.put("createdAt", createdAt);
        map.put("updatedAt", updatedAt);
        map.put("deleted", deleted);
        return map;
    }

    @Override
    public void fromMap(Map<String, Object> data) {
        if (data.containsKey("title")) {
            this.title = (String) data.get("title");
        }
        if (data.containsKey("subtitle")) {
            this.subtitle = (String) data.get("subtitle");
        }
        if (data.containsKey("publisher")) {
            this.publisher = (String) data.get("publisher");
        }
        if (data.containsKey("pages")) {
            this.pages = (Integer) data.get("pages");
        }
        if (data.containsKey("description")) {
            this.description = (String) data.get("description");
        }
        if (data.containsKey("format")) {
            this.format = BookFormat.valueOf((String) data.get("format"));
        }
    }

    @Override
    public String getSummary() {
        return String.format("Book[isbn=%s, title=%s, authors=%d, copies=%d/%d, status=%s]",
                isbn, title, authors.size(), availableCopies, totalCopies, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn=" + isbn +
                ", title='" + title + '\'' +
                ", authors=" + authors.size() +
                ", status=" + status +
                ", copies=" + availableCopies + "/" + totalCopies +
                '}';
    }
}
