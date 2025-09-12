
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ProductSearchService {
    private final List<Product> products;

    public ProductSearchService(List<Product> products) {
        this.products = products;
    }

    public List<Product> searchByName(String q) {
        String k = q.toLowerCase();
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    public List<Product> filterByPriceRange(BigDecimal min, BigDecimal max) {
        return products.stream()
                .filter(p -> {
                    BigDecimal price = p.calculateFinalPrice();
                    return price.compareTo(min) >= 0 && price.compareTo(max) <= 0;
                })
                .collect(Collectors.toList());
    }

    public List<Product> filterByAttribute(String key, String value) {
        return products.stream()
                .filter(p -> value.equals(p.getAttribute(key)))
                .collect(Collectors.toList());
    }

    public List<Product> filterByCategory(Category root, String categoryName) {
        List<Product> result = new ArrayList<>();
        traverseCategory(root, categoryName, result);
        return result;
    }

    private void traverseCategory(Category c, String match, List<Product> out) {
        if (c.getName().equalsIgnoreCase(match)) {
            for (CatalogComponent child : c.getChildren()) {
                if (child instanceof ProductItem) out.add(((ProductItem) child).getProduct());
            }
            return;
        }
        for (CatalogComponent child : c.getChildren()) {
            if (child instanceof Category) traverseCategory((Category) child, match, out);
        }
    }
}
