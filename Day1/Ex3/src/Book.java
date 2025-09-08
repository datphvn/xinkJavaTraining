import java.time.LocalDate;

public class Book {
    private final String isbn;
    private String title;
    private String author;
    private String category;
    private int totalCopies;
    private int availableCopies;
    private LocalDate publishDate;
    private BookFormat format;

    public Book(String isbn, String title, String author, String category,
                int totalCopies, LocalDate publishDate, BookFormat format) {
        if (!isValidISBN(isbn)) throw new IllegalArgumentException("Invalid ISBN");
        if (totalCopies < 0) throw new IllegalArgumentException("totalCopies >= 0");
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.publishDate = publishDate;
        this.format = format;
    }

    // ISBN-10/13 basic validation
    private boolean isValidISBN(String s) {
        return s != null && (s.matches("\\d{10}") || s.matches("\\d{13}"));
    }

    // Copy management
    public boolean borrowCopy() {
        if (format == BookFormat.DIGITAL) return true; // unlimited digital copies
        if (availableCopies > 0) { availableCopies--; return true; }
        return false;
    }

    public void returnCopy() {
        if (format == BookFormat.DIGITAL) return;
        if (availableCopies < totalCopies) availableCopies++;
    }

    public void addCopies(int n) {
        if (format == BookFormat.DIGITAL) return;
        if (n <= 0) throw new IllegalArgumentException("n > 0");
        totalCopies += n;
        availableCopies += n;
    }

    public void removeCopies(int n) {
        if (format == BookFormat.DIGITAL) return;
        if (n <= 0) throw new IllegalArgumentException("n > 0");
        if (n > availableCopies) throw new IllegalArgumentException("Cannot remove borrowed copies");
        totalCopies -= n;
        availableCopies -= n;
    }

    // Search helper
    public boolean matches(String keyword) {
        String k = keyword.toLowerCase();
        return title.toLowerCase().contains(k) || author.toLowerCase().contains(k) || category.toLowerCase().contains(k) || isbn.contains(k);
    }

    // Getters/Setters (đầy đủ)
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
    public BookFormat getFormat() { return format; }
    public void setFormat(BookFormat format) { this.format = format; }

    @Override
    public String toString() {
        return "[" + isbn + "] " + title + " by " + author + " | " + format +
                " | " + availableCopies + "/" + totalCopies + " available";
    }
}
