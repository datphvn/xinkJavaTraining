
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BulkDiscountStrategy implements PricingStrategy {
    private final int threshold;
    private final BigDecimal discountPercent;

    public BulkDiscountStrategy(int threshold, BigDecimal discountPercent) {
        this.threshold = threshold;
        this.discountPercent = discountPercent;
    }

    @Override
    public BigDecimal calculatePrice(Product product, int quantity) {
        BigDecimal base = product.getBasePrice().multiply(BigDecimal.valueOf(quantity));
        if (quantity >= threshold) {
            BigDecimal discount = base.multiply(discountPercent);
            return base.subtract(discount).setScale(2, RoundingMode.HALF_UP);
        }
        return base;
    }
}
