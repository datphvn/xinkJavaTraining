import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Serializable {
    protected final String accountNumber;
    protected double balance;
    protected boolean frozen;
    protected final List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;
        this.frozen = false;
    }

    public void deposit(double amount) {
        if (frozen) throw new IllegalStateException("Account is frozen");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
        transactions.add(new Transaction("Deposit", amount, balance));
    }

    public void withdraw(double amount) {
        if (frozen) throw new IllegalStateException("Account is frozen");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (balance < amount) throw new IllegalArgumentException("Insufficient funds");
        balance -= amount;
        transactions.add(new Transaction("Withdraw", amount, balance));
    }

    public void freeze() { frozen = true; }
    public void unfreeze() { frozen = false; }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public boolean isFrozen() { return frozen; }
    public List<Transaction> getTransactions() { return transactions; }


    public abstract void calculateInterest();

    // Sinh statement (theo tháng, nhưng demo thì in toàn bộ)
    public String generateStatement() {
        StringBuilder sb = new StringBuilder();
        sb.append("Statement for Account ").append(accountNumber).append("\n");
        for (Transaction t : transactions) {
            sb.append(t).append("\n");
        }
        sb.append("Final Balance: ").append(balance).append("\n");
        return sb.toString();
    }
}
