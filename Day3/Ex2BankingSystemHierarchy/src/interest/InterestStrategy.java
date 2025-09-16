package interest;

import java.math.BigDecimal;

public interface InterestStrategy {
    // calculate interest for given balance for N days
    BigDecimal calculateInterest(BigDecimal balance, int days);
}
