import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private final LocalDateTime timestamp;
    private final String type; // Deposit, Withdraw, Transfer, Interest
    private final double amount;
    private final double balanceAfter;

    public Transaction(String type, double amount, double balanceAfter) {
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    @Override
    public String toString() {
        return timestamp + " | " + type + " | " + amount + " | Balance: " + balanceAfter;
    }
}
