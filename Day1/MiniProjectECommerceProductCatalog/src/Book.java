// Book.java
public class Book extends Product {
    private String author;
    private String isbn;

    public Book(String id, String name, String description, double basePrice, int stockQuantity, Category category,
                String author, String isbn) {
        super(id, name, description, basePrice, stockQuantity, category);
        this.author = author;
        this.isbn = isbn;
    }

    @Override
    public double calculateFinalPrice() {
        return super.basePrice; // No discount for now
    }

    @Override
    public boolean isAvailable() {
        return super.getStockQuantity() > 0;
    }

    @Override
    public String getProductType() {
        return "Book";
    }
}
