package accounts;

import interest.InterestStrategy;
import transactions.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public class SavingsAccount extends Account {
    private final BigDecimal minimumBalance;
    private final InterestStrategy interestStrategy;

    public SavingsAccount(String accountNumber, List<String> holders, String currency,
                          BigDecimal initial, BigDecimal minimumBalance, InterestStrategy interestStrategy) {
        super(accountNumber, holders, currency, initial);
        this.minimumBalance = minimumBalance == null ? BigDecimal.ZERO : minimumBalance;
        this.interestStrategy = interestStrategy;
    }

    @Override
    public boolean withdraw(BigDecimal amount, String performedBy) {
        if (amount == null || amount.signum() <= 0) return false;
        lock.lock();
        try {
            BigDecimal prospective = balance.subtract(amount);
            if (prospective.compareTo(BigDecimal.ZERO) < 0) return false; // no overdraft
            balance = prospective;
            addTransaction(new transactions.Transaction(TransactionType.WITHDRAWAL, accountNumber, null, amount, balance));
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void creditInterest(int days) {
        if (interestStrategy == null) return;
        lock.lock();
        try {
            BigDecimal interest = interestStrategy.calculateInterest(balance, days);
            if (interest.signum() > 0) {
                balance = balance.add(interest);
                addTransaction(new transactions.Transaction(TransactionType.INTEREST, null, accountNumber, interest, balance));
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void applyMonthlyUpdates() {
        creditInterest(30);
    }
}
