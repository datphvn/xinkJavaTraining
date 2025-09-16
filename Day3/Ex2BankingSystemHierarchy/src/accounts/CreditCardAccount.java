package accounts;

import transactions.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public class CreditCardAccount extends Account {
    private final BigDecimal creditLimit;
    private final BigDecimal monthlyInterestRate; // e.g., 0.02 => 2% monthly
    private final BigDecimal lateFee;

    public CreditCardAccount(String accountNumber, List<String> holders, String currency,
                             BigDecimal creditLimit, BigDecimal monthlyInterestRate, BigDecimal lateFee) {
        super(accountNumber, holders, currency, BigDecimal.ZERO);
        this.creditLimit = creditLimit == null ? BigDecimal.ZERO : creditLimit;
        this.monthlyInterestRate = monthlyInterestRate == null ? BigDecimal.ZERO : monthlyInterestRate;
        this.lateFee = lateFee == null ? BigDecimal.ZERO : lateFee;
    }

    // "Withdraw" = make purchase -> increases owed balance (positive)
    @Override
    public boolean withdraw(BigDecimal amount, String performedBy) {
        if (amount == null || amount.signum() <= 0) return false;
        lock.lock();
        try {
            BigDecimal prospective = balance.add(amount); // balance = owed amount
            if (prospective.compareTo(creditLimit) > 0) return false;
            balance = prospective;
            addTransaction(new transactions.Transaction(TransactionType.WITHDRAWAL, null, accountNumber, amount, balance));
            return true;
        } finally {
            lock.unlock();
        }
    }

    // deposit = payment -> reduces owed balance
    @Override
    public boolean deposit(BigDecimal amount, String performedBy) {
        if (amount == null || amount.signum() <= 0) return false;
        lock.lock();
        try {
            balance = balance.subtract(amount);
            if (balance.compareTo(BigDecimal.ZERO) < 0) balance = BigDecimal.ZERO; // no negative owed
            addTransaction(new transactions.Transaction(TransactionType.PAYMENT, accountNumber, null, amount, balance));
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void applyMonthlyUpdates() {
        lock.lock();
        try {
            if (balance.signum() > 0) {
                BigDecimal interest = balance.multiply(monthlyInterestRate).setScale(2, BigDecimal.ROUND_HALF_UP);
                balance = balance.add(interest);
                addTransaction(new transactions.Transaction(TransactionType.INTEREST, null, accountNumber, interest, balance));
                // optionally apply late fee (example)
                addTransaction(new transactions.Transaction(TransactionType.FEE, null, accountNumber, lateFee, balance));
                balance = balance.add(lateFee);
            }
        } finally {
            lock.unlock();
        }
    }
}
