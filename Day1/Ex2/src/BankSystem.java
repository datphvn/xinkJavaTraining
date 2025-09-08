import java.util.*;

public class BankSystem {
    private final Map<String, Account> accounts = new HashMap<>();

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void transfer(String fromAcc, String toAcc, double amount) {
        Account from = accounts.get(fromAcc);
        Account to = accounts.get(toAcc);
        if (from == null || to == null) throw new IllegalArgumentException("Account not found");
        from.withdraw(amount);
        to.deposit(amount);
        from.getTransactions().add(new Transaction("Transfer Out", amount, from.getBalance()));
        to.getTransactions().add(new Transaction("Transfer In", amount, to.getBalance()));
    }

    public void generateAllStatements() {
        for (Account acc : accounts.values()) {
            System.out.println(acc.generateStatement());
        }
    }

    public void applyMonthlyInterest() {
        for (Account acc : accounts.values()) {
            acc.calculateInterest();
        }
    }
}
