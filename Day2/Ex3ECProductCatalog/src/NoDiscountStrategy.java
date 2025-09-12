
import java.math.BigDecimal;

public class NoDiscountStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculatePrice(Product product, int quantity) {
        return product.getBasePrice().multiply(BigDecimal.valueOf(quantity));
    }
}
