package interest;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SimpleInterestStrategy implements InterestStrategy {
    // annualRate as fraction: e.g. 0.02 for 2% annual
    private final BigDecimal annualRate;

    public SimpleInterestStrategy(BigDecimal annualRate) {
        this.annualRate = annualRate == null ? BigDecimal.ZERO : annualRate;
    }

    @Override
    public BigDecimal calculateInterest(BigDecimal balance, int days) {
        if (balance == null || balance.signum() <= 0) return BigDecimal.ZERO;
        BigDecimal daily = annualRate.divide(BigDecimal.valueOf(365), 12, RoundingMode.HALF_UP);
        BigDecimal interest = balance.multiply(daily).multiply(BigDecimal.valueOf(days));
        return interest.setScale(2, RoundingMode.HALF_UP);
    }
}
