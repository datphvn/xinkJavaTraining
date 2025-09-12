

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Build products (Builder pattern)
        ProductBuilder builder = new ProductBuilder();
        PhysicalProduct laptop = builder.id("P100")
                .name("Laptop Pro")
                .description("High-end laptop")
                .basePrice(new BigDecimal("1500.00"))
                .addAttribute("brand", "Acme")
                .buildPhysical(10);

        DigitalProduct ebook = builder.id("D200")
                .name("Learn Java")
                .description("Ebook about Java")
                .basePrice(new BigDecimal("29.99"))
                .buildDigital("https://download.example/java");

        ServiceProduct cleaning = builder.id("S300")
                .name("Home Cleaning")
                .description("2 hour cleaning")
                .basePrice(new BigDecimal("80.00"))
                .buildService(2);

        // Pricing strategy (Strategy pattern)
        laptop.setPricingStrategy(new PercentageDiscountStrategy(new BigDecimal("0.10"))); // 10% off
        ebook.setPricingStrategy(new NoDiscountStrategy());
        cleaning.setPricingStrategy(new BulkDiscountStrategy(5, new BigDecimal("0.15"))); // example

        // Composite categories (Composite pattern)
        Category root = new Category("All");
        Category electronics = new Category("Electronics");
        root.add(electronics);
        electronics.add(new ProductItem(laptop));
        Category books = new Category("Books");
        root.add(books);
        books.add(new ProductItem(ebook));
        Category services = new Category("Services");
        root.add(services);
        services.add(new ProductItem(cleaning));

        // Inventory manager + observer (Observer pattern)
        InventoryManager inventory = new InventoryManager(3); // threshold = 3
        inventory.registerObserver(new EmailAlertService());

        // decrease stock to trigger alert
        inventory.decreaseStock(laptop, 8); // laptop stock becomes 2 -> alert

        // Reviews
        laptop.addReview(new Review("alice", 5, "Excellent!"));
        laptop.addReview(new Review("bob", 4, "Very good"));
        System.out.println("Laptop avg rating: " + laptop.getAverageRating());

        // Searching
        List<Product> all = new ArrayList<>();
        all.add(laptop); all.add(ebook); all.add(cleaning);
        ProductSearchService search = new ProductSearchService(all);
        System.out.println("Search 'john' -> " + search.searchByName("Java"));
        System.out.println("Filter price 50..2000 -> " + search.filterByPriceRange(new BigDecimal("50"), new BigDecimal("2000")));

        // display catalog
        root.display("");
    }
}
