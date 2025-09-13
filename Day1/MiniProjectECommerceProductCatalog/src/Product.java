// Product.java
import java.util.*;

public abstract class Product {
    private final String id;
    private String name;
    private String description;
    double basePrice;
    private int stockQuantity;
    private Category category;
    private final List<Review> reviews;

    public Product(String id, String name, String description, double basePrice, int stockQuantity, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.reviews = new ArrayList<>();
    }

    // Common methods
    public void addReview(Review review) {
        reviews.add(review);
    }

    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }

    public void adjustStock(int amount) {
        stockQuantity += amount;
    }

    public int getStockQuantity() { return stockQuantity; }
    public String getId() { return id; }
    public String getName() { return name; }

    // Abstract methods
    public abstract double calculateFinalPrice();
    public abstract boolean isAvailable();
    public abstract String getProductType();
}
