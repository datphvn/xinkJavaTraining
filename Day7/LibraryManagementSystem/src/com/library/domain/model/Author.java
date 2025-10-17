package com.library.domain.model;

import com.library.domain.exception.ValidationException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an author in the library system.
 * An author can write multiple books.
 * Pure OOP implementation without any framework dependencies.
 */
public class Author extends BaseEntity {
    private String authorId;
    private String name;
    private LocalDate dateOfBirth;
    private String nationality;
    private String biography;
    private final Set<Book> books;

    /**
     * Constructor for creating a new Author.
     * @param authorId Unique identifier for the author
     * @param name The name of the author
     * @param dateOfBirth The date of birth of the author
     * @param nationality The nationality of the author
     * @param biography The biography of the author
     * @throws IllegalArgumentException if authorId or name is null or blank
     */
    public Author(String authorId, String name, LocalDate dateOfBirth, String nationality, String biography) {
        super();
        this.authorId = Objects.requireNonNull(authorId, "Author ID cannot be null");
        this.name = Objects.requireNonNull(name, "Author name cannot be null");
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.biography = biography;
        this.books = new HashSet<>();
    }

    // Getters
    public String getAuthorId() {
        return authorId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getBiography() {
        return biography;
    }

    public Set<Book> getBooks() {
        return new HashSet<>(books);
    }

    // Setters
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Author name cannot be null");
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    // Business methods

    /**
     * Adds a book written by this author.
     * @param book The book to add
     */
    public void addBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        books.add(book);
    }

    /**
     * Removes a book from this author's collection.
     * @param book The book to remove
     */
    public void removeBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        books.remove(book);
    }

    /**
     * Gets the number of books written by this author.
     * @return The number of books
     */
    public int getBookCount() {
        return books.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return Objects.equals(authorId, author.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id='" + authorId + '\'' +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", bookCount=" + books.size() +
                '}';
    }

    @Override
    public void validate() throws ValidationException {
        Map<String, String> errors = new HashMap<>();
        if (authorId == null || authorId.isBlank()) {
            errors.put("authorId", "Author ID is required");
        }
        if (name == null || name.isBlank()) {
            errors.put("name", "Author name is required");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public String getSummary() {
        return String.format("Author[id=%s, name=%s, books=%d]", authorId, name, books.size());
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", authorId);
        map.put("name", name);
        map.put("dateOfBirth", dateOfBirth);
        map.put("nationality", nationality);
        map.put("biography", biography);
        map.put("bookCount", books.size());
        return map;
    }

    @Override
    public void fromMap(Map<String, Object> data) {
        if (data == null) {
            return;
        }
        if (data.containsKey("name")) {
            this.name = (String) data.get("name");
        }
        if (data.containsKey("dateOfBirth")) {
            this.dateOfBirth = (LocalDate) data.get("dateOfBirth");
        }
        if (data.containsKey("nationality")) {
            this.nationality = (String) data.get("nationality");
        }
        if (data.containsKey("biography")) {
            this.biography = (String) data.get("biography");
        }
    }
}
