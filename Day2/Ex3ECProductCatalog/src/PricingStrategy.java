

import java.math.BigDecimal;

@FunctionalInterface
public interface PricingStrategy {
    // calculate price for given product and quantity
    BigDecimal calculatePrice(Product product, int quantity);
}
