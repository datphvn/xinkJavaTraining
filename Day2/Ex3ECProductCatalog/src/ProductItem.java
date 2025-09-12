
public class ProductItem extends CatalogComponent {
    private final Product product;

    public ProductItem(Product product) { this.product = product; }

    @Override
    public void display(String indent) {
        System.out.println(indent + "- " + product.getName() + " | " + product.getProductType()
                + " | Price: " + product.calculateFinalPrice());
    }

    public Product getProduct() { return product; }
}
