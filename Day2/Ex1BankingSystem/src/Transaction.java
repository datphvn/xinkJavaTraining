

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class Transaction {
    private final LocalDateTime timestamp;
    private final TransactionType type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;

    public Transaction(TransactionType type, BigDecimal amount, BigDecimal balanceAfter) {
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }

    @Override
    public String toString() {
        return timestamp + " | " + type + " | " + amount + " | Balance: " + balanceAfter;
    }
}
