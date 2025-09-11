

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Account {
    private static int nextAccountNumber = 1000;
    private static final ReentrantLock staticLock = new ReentrantLock();

    private final int accountNumber;
    private final String accountHolder;
    private BigDecimal balance;
    private final Currency currency;
    private final List<Transaction> transactions;
    private AccountStatus status;
    private final LocalDateTime openedDate;
    protected final ReentrantLock lock = new ReentrantLock();

    public Account(String accountHolder, BigDecimal initialDeposit, Currency currency) {
        if (initialDeposit.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Initial deposit cannot be negative");

        staticLock.lock();
        try {
            this.accountNumber = nextAccountNumber++;
        } finally {
            staticLock.unlock();
        }

        this.accountHolder = accountHolder;
        this.balance = initialDeposit;
        this.currency = currency;
        this.transactions = new ArrayList<>();
        this.status = AccountStatus.ACTIVE;
        this.openedDate = LocalDateTime.now();
        recordTransaction(TransactionType.DEPOSIT, initialDeposit);
    }

    public void deposit(BigDecimal amount) {
        lock.lock();
        try {
            validateActive();
            if (amount.compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("Deposit must be positive");
            balance = balance.add(amount);
            recordTransaction(TransactionType.DEPOSIT, amount);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(BigDecimal amount) {
        lock.lock();
        try {
            validateActive();
            if (amount.compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("Withdrawal must be positive");
            if (balance.compareTo(amount) < 0)
                throw new IllegalArgumentException("Insufficient funds");
            balance = balance.subtract(amount);
            recordTransaction(TransactionType.WITHDRAWAL, amount);
        } finally {
            lock.unlock();
        }
    }

    protected void recordTransaction(TransactionType type, BigDecimal amount) {
        transactions.add(new Transaction(type, amount, balance));
    }

    protected void validateActive() {
        if (status != AccountStatus.ACTIVE)
            throw new IllegalStateException("Account not active");
    }

    public abstract String getAccountType();

    public int getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public BigDecimal getBalance() { return balance; }
    public Currency getCurrency() { return currency; }
    public List<Transaction> getTransactions() { return Collections.unmodifiableList(transactions); }
    public AccountStatus getStatus() { return status; }
    public LocalDateTime getOpenedDate() { return openedDate; }

    public void setStatus(AccountStatus status) { this.status = status; }
    protected void setBalance(BigDecimal newBalance) { this.balance = newBalance; }
}
