// Electronics.java
import java.util.*;

public class Electronics extends Product {
    private String brand;
    private int warrantyMonths;
    private Map<String, String> specifications;

    public Electronics(String id, String name, String description, double basePrice, int stockQuantity, Category category,
                       String brand, int warrantyMonths) {
        super(id, name, description, basePrice, stockQuantity, category);
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
        this.specifications = new HashMap<>();
    }

    public void addSpecification(String key, String value) {
        specifications.put(key, value);
    }

    @Override
    public double calculateFinalPrice() {
        return super.getStockQuantity() > 5 ? super.basePrice * 0.95 : super.basePrice; // simple dynamic discount
    }

    @Override
    public boolean isAvailable() {
        return super.getStockQuantity() > 0;
    }

    @Override
    public String getProductType() {
        return "Electronics";
    }
}
