package accounts;

import interest.InterestStrategy;
import transactions.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public class InvestmentAccount extends Account {
    private final InterestStrategy strategy;

    public InvestmentAccount(String accountNumber, List<String> holders, String currency,
                             BigDecimal initial, InterestStrategy strategy) {
        super(accountNumber, holders, currency, initial);
        this.strategy = strategy;
    }

    @Override
    public void applyMonthlyUpdates() {
        if (strategy == null) return;
        lock.lock();
        try {
            BigDecimal interest = strategy.calculateInterest(balance, 30);
            if (interest.signum() > 0) {
                balance = balance.add(interest);
                addTransaction(new transactions.Transaction(TransactionType.INTEREST, null, accountNumber, interest, balance));
            }
        } finally {
            lock.unlock();
        }
    }
}
