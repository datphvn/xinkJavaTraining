// MainMenu.java
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Product> catalog = new ArrayList<>();
    private static final ShoppingCart cart = new ShoppingCart();
    private static final Wishlist wishlist = new Wishlist();

    public static void main(String[] args) {
        boolean running = true;

        // Tạo sẵn category
        Category electronicsCat = new Category("Electronics");
        Category clothingCat = new Category("Clothing");
        Category bookCat = new Category("Books");

        while (running) {
            System.out.println("\n===== PRODUCT CATALOG MENU =====");
            System.out.println("1. Add Product");
            System.out.println("2. Show All Products");
            System.out.println("3. Add Review to Product");
            System.out.println("4. Add Product to Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Manage Wishlist");
            System.out.println("7. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addProduct(electronicsCat, clothingCat, bookCat);
                case 2 -> showProducts();
                case 3 -> addReview();
                case 4 -> addToCart();
                case 5 -> viewCart();
                case 6 -> manageWishlist();
                case 7 -> running = false;
                default -> System.out.println("Invalid choice!");
            }
        }
        System.out.println("Exiting program...");
    }

    private static void addProduct(Category electronicsCat, Category clothingCat, Category bookCat) {
        System.out.println("Choose type: 1=Electronics, 2=Clothing, 3=Book");
        int type = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Description: ");
        String desc = scanner.nextLine();
        System.out.print("Enter Base Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Stock Quantity: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        Product p = null;
        if (type == 1) {
            System.out.print("Enter Brand: ");
            String brand = scanner.nextLine();
            System.out.print("Warranty Months: ");
            int warranty = scanner.nextInt();
            p = new Electronics(id, name, desc, price, stock, electronicsCat, brand, warranty);
            electronicsCat.addProduct(p);
        } else if (type == 2) {
            System.out.print("Enter Size: ");
            String size = scanner.nextLine();
            System.out.print("Enter Material: ");
            String material = scanner.nextLine();
            p = new Clothing(id, name, desc, price, stock, clothingCat, size, material);
            clothingCat.addProduct(p);
        } else if (type == 3) {
            System.out.print("Enter Author: ");
            String author = scanner.nextLine();
            System.out.print("Enter ISBN: ");
            String isbn = scanner.nextLine();
            p = new Book(id, name, desc, price, stock, bookCat, author, isbn);
            bookCat.addProduct(p);
        }

        if (p != null) {
            catalog.add(p);
            System.out.println("Product added successfully!");
        }
    }

    private static void showProducts() {
        if (catalog.isEmpty()) {
            System.out.println("No products in catalog.");
            return;
        }
        for (int i = 0; i < catalog.size(); i++) {
            Product p = catalog.get(i);
            System.out.println((i + 1) + ". " + p.getProductType() + " - " + p.getName() +
                    " ($" + p.calculateFinalPrice() + "), Stock: " + p.getStockQuantity());
        }
    }

    private static void addReview() {
        showProducts();
        if (catalog.isEmpty()) return;
        System.out.print("Choose product number: ");
        int idx = scanner.nextInt() - 1;
        scanner.nextLine();

        System.out.print("Reviewer name: ");
        String name = scanner.nextLine();
        System.out.print("Rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Comment: ");
        String comment = scanner.nextLine();

        Review r = new Review(name, rating, comment);
        catalog.get(idx).addReview(r);
        System.out.println("Review added!");
    }

    private static void addToCart() {
        showProducts();
        if (catalog.isEmpty()) return;
        System.out.print("Choose product number: ");
        int idx = scanner.nextInt() - 1;
        System.out.print("Quantity: ");
        int qty = scanner.nextInt();
        cart.addItem(catalog.get(idx), qty);
        System.out.println("Added to cart!");
    }

    private static void viewCart() {
        System.out.println("Cart total = $" + cart.calculateTotal());
    }

    private static void manageWishlist() {
        System.out.println("1. Add product to wishlist");
        System.out.println("2. Show wishlist");
        int ch = scanner.nextInt();
        scanner.nextLine();
        if (ch == 1) {
            showProducts();
            System.out.print("Choose product number: ");
            int idx = scanner.nextInt() - 1;
            wishlist.addProduct(catalog.get(idx));
            System.out.println("Added to wishlist!");
        } else if (ch == 2) {
            System.out.println("Wishlist:");
            for (Product p : wishlist.getProducts()) {
                System.out.println("- " + p.getName());
            }
        }
    }
}
