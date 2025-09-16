package services;

import transactions.Transaction;
import transactions.TransactionType;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

public class FraudDetector {
    private final BigDecimal largeTransferThreshold;

    public FraudDetector(BigDecimal largeTransferThreshold) {
        this.largeTransferThreshold = largeTransferThreshold == null ? BigDecimal.valueOf(10_000) : largeTransferThreshold;
    }

    public boolean isSuspicious(List<Transaction> history, Transaction newTx) {
        if (newTx == null) return false;
        if (newTx.getType() == TransactionType.TRANSFER || newTx.getType() == TransactionType.WITHDRAWAL) {
            if (newTx.getAmount().compareTo(largeTransferThreshold) >= 0) return true;
        }
        // rapid withdrawals within short window example:
        int recentCount = 0;
        for (int i = history.size() - 1; i >= 0 && recentCount < 10; i--) {
            Transaction t = history.get(i);
            if (Duration.between(t.getTimestamp(), newTx.getTimestamp()).toMinutes() <= 10) {
                if (t.getType() == TransactionType.WITHDRAWAL) recentCount++;
            } else break;
        }
        if (recentCount >= 5 && newTx.getType() == TransactionType.WITHDRAWAL) return true;
        return false;
    }
}
