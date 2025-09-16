package accounts;

import transactions.Transaction;
import transactions.TransactionType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Account {
    protected final String accountNumber;
    protected final List<String> holders; // authorized users (IDs or names)
    protected BigDecimal balance;
    protected final String currency;
    protected final List<Transaction> transactionHistory = new ArrayList<>();
    protected final ReentrantLock lock = new ReentrantLock();

    public Account(String accountNumber, List<String> holders, String currency, BigDecimal initial) {
        this.accountNumber = accountNumber;
        this.holders = holders == null ? new ArrayList<>() : new ArrayList<>(holders);
        this.currency = currency == null ? "USD" : currency;
        this.balance = initial == null ? BigDecimal.ZERO : initial;
    }

    public String getAccountNumber() { return accountNumber; }
    public List<String> getHolders() { return Collections.unmodifiableList(holders); }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }

    protected void addTransaction(Transaction tx) {
        transactionHistory.add(tx);
    }

    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    // return true if success
    public boolean deposit(BigDecimal amount, String performedBy) {
        if (amount == null || amount.signum() <= 0) return false;
        lock.lock();
        try {
            balance = balance.add(amount);
            addTransaction(new Transaction(TransactionType.DEPOSIT, null, accountNumber, amount, balance));
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean withdraw(BigDecimal amount, String performedBy) {
        if (amount == null || amount.signum() <= 0) return false;
        lock.lock();
        try {
            if (balance.compareTo(amount) < 0) return false; // default: no overdraft
            balance = balance.subtract(amount);
            addTransaction(new Transaction(TransactionType.WITHDRAWAL, accountNumber, null, amount, balance));
            return true;
        } finally {
            lock.unlock();
        }
    }

    // subclasses may override for account-specific monthly behavior
    public void applyMonthlyUpdates() { }

    // utility for subclasses to force-add a transaction (e.g., fees/interest)
    protected void record(TransactionType type, BigDecimal amount) {
        lock.lock();
        try {
            if (type == TransactionType.INTEREST || type == TransactionType.DEPOSIT || type == TransactionType.PAYMENT) {
                balance = balance.add(amount);
            } else if (type == TransactionType.FEE || type == TransactionType.WITHDRAWAL) {
                balance = balance.subtract(amount);
            } else if (type == TransactionType.TRANSFER) {
                // transfer handled by BankService
                balance = balance.add(amount); // sign must be provided by caller (positive/negative)
            }
            addTransaction(new Transaction(type, null, accountNumber, amount, balance));
        } finally {
            lock.unlock();
        }
    }
}
