// Category.java
import java.util.*;

public class Category {
    private String name;
    private List<Category> subCategories;
    private List<Product> products;

    public Category(String name) {
        this.name = name;
        this.subCategories = new ArrayList<>();
        this.products = new ArrayList<>();
    }

    public void addSubCategory(Category sub) {
        subCategories.add(sub);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public String getName() { return name; }
}
