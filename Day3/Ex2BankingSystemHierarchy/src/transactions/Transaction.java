package transactions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class Transaction {
    private final String id;
    private final Instant timestamp;
    private final TransactionType type;
    private final String fromAccount; // nullable
    private final String toAccount;   // nullable
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;

    public Transaction(TransactionType type, String fromAccount, String toAccount,
                       BigDecimal amount, BigDecimal balanceAfter) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.type = type;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount == null ? BigDecimal.ZERO : amount;
        this.balanceAfter = balanceAfter == null ? BigDecimal.ZERO : balanceAfter;
    }

    public String getId() { return id; }
    public Instant getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }
    public String getFromAccount() { return fromAccount; }
    public String getToAccount() { return toAccount; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }

    @Override
    public String toString() {
        return String.format("%s | %s | from=%s to=%s amount=%s balAfter=%s",
                timestamp, type, fromAccount, toAccount, amount, balanceAfter);
    }
}
