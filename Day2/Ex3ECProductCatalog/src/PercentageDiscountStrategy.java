

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageDiscountStrategy implements PricingStrategy {
    private final BigDecimal percent; // 0.15 for 15%

    public PercentageDiscountStrategy(BigDecimal percent) {
        if (percent.compareTo(BigDecimal.ZERO) < 0 || percent.compareTo(BigDecimal.ONE) > 0)
            throw new IllegalArgumentException("percent 0..1");
        this.percent = percent;
    }

    @Override
    public BigDecimal calculatePrice(Product product, int quantity) {
        BigDecimal base = product.getBasePrice().multiply(BigDecimal.valueOf(quantity));
        BigDecimal discount = base.multiply(percent);
        return base.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }
}
