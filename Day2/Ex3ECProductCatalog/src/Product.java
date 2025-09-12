

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public abstract class Product {
    protected final String id;
    protected String name;
    protected String description;
    protected BigDecimal basePrice;
    protected Map<String, String> attributes = new HashMap<>(); // dynamic attributes
    protected List<Review> reviews = new ArrayList<>();
    protected PricingStrategy pricingStrategy; // Strategy pattern
    protected LocalDateTime createdAt = LocalDateTime.now();

    public Product(String id, String name, BigDecimal basePrice) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.basePrice = basePrice == null ? BigDecimal.ZERO : basePrice;
    }

    // Pricing: delegate to strategy (quantity-aware)
    public BigDecimal calculateFinalPrice(int quantity) {
        if (pricingStrategy == null) return basePrice.multiply(BigDecimal.valueOf(quantity));
        return pricingStrategy.calculatePrice(this, quantity);
    }

    // convenience
    public BigDecimal calculateFinalPrice() { return calculateFinalPrice(1); }

    // dynamic attributes
    public void addAttribute(String key, String value) { attributes.put(key, value); }
    public String getAttribute(String key) { return attributes.get(key); }
    public Map<String, String> getAttributes() { return Collections.unmodifiableMap(attributes); }

    // reviews
    public void addReview(Review r) {
        reviews.add(r);
    }
    public List<Review> getReviews() { return Collections.unmodifiableList(reviews); }
    public double getAverageRating() {
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }

    // getters/setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public void setPricingStrategy(PricingStrategy pricingStrategy) { this.pricingStrategy = pricingStrategy; }
    public PricingStrategy getPricingStrategy() { return pricingStrategy; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public abstract String getProductType();
    public abstract boolean isAvailable(); // physical: stock>0, digital/service: always true
}
