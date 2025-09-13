// Clothing.java
public class Clothing extends Product {
    private String size;
    private String material;

    public Clothing(String id, String name, String description, double basePrice, int stockQuantity, Category category,
                    String size, String material) {
        super(id, name, description, basePrice, stockQuantity, category);
        this.size = size;
        this.material = material;
    }

    @Override
    public double calculateFinalPrice() {
        return super.basePrice * 0.9; // example 10% discount
    }

    @Override
    public boolean isAvailable() {
        return super.getStockQuantity() > 0;
    }

    @Override
    public String getProductType() {
        return "Clothing";
    }
}
