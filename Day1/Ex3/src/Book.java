import java.time.LocalDate;

public class Book {
    private final String isbn;
    private String title;
    private String author;
    private String category;
    private int totalCopies;
    private int availableCopies;
    private LocalDate publishDate;

    public Book(String isbn, String title, String author, String category, int totalCopies, LocalDate publishDate) {
        if (!isValidISBN(isbn)) throw new IllegalArgumentException("Invalid ISBN format");
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.publishDate = publishDate;
    }

    // ISBN validation
    private boolean isValidISBN(String isbn) {
        return isbn != null && (isbn.matches("\\d{10}") || isbn.matches("\\d{13}"));
    }

    // Copy management
    public boolean borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    public void returnCopy() {
        if (availableCopies < totalCopies) availableCopies++;
    }

    // Search
    public boolean matches(String keyword) {
        return title.toLowerCase().contains(keyword.toLowerCase()) ||
                author.toLowerCase().contains(keyword.toLowerCase()) ||
                category.toLowerCase().contains(keyword.toLowerCase());
    }

    // Getters & setters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
    public LocalDate getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }

    @Override
    public String toString() {
        return String.format("[%s] %s by %s | %d/%d available", isbn, title, author, availableCopies, totalCopies);
    }
}
