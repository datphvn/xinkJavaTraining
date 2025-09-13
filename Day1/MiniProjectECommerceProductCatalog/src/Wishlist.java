// Wishlist.java
import java.util.*;

public class Wishlist {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product p) {
        if (!products.contains(p)) {
            products.add(p);
        }
    }

    public void removeProduct(Product p) {
        products.remove(p);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
}
