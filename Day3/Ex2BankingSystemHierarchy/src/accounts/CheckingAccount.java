package accounts;

import transactions.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public class CheckingAccount extends Account {
    private final BigDecimal overdraftLimit;
    private final BigDecimal monthlyFee;

    public CheckingAccount(String accountNumber, List<String> holders, String currency,
                           BigDecimal initial, BigDecimal overdraftLimit, BigDecimal monthlyFee) {
        super(accountNumber, holders, currency, initial);
        this.overdraftLimit = overdraftLimit == null ? BigDecimal.ZERO : overdraftLimit;
        this.monthlyFee = monthlyFee == null ? BigDecimal.ZERO : monthlyFee;
    }

    @Override
    public boolean withdraw(java.math.BigDecimal amount, String performedBy) {
        if (amount == null || amount.signum() <= 0) return false;
        lock.lock();
        try {
            BigDecimal potential = balance.subtract(amount);
            BigDecimal minAllowed = overdraftLimit.negate(); // e.g., -500 allowed
            if (potential.compareTo(minAllowed) < 0) return false;
            balance = potential;
            addTransaction(new transactions.Transaction(TransactionType.WITHDRAWAL, accountNumber, null, amount, balance));
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void applyMonthlyUpdates() {
        if (monthlyFee.signum() == 0) return;
        lock.lock();
        try {
            balance = balance.subtract(monthlyFee);
            addTransaction(new transactions.Transaction(TransactionType.FEE, accountNumber, null, monthlyFee, balance));
        } finally {
            lock.unlock();
        }
    }
}
