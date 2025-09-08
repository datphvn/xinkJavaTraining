public class CheckingAccount extends Account {
    private final double overdraftLimit;

    public CheckingAccount(String accountNumber, double overdraftLimit) {
        super(accountNumber);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (frozen) throw new IllegalStateException("Account is frozen");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (balance + overdraftLimit < amount)
            throw new IllegalArgumentException("Exceeds overdraft limit");
        balance -= amount;
        transactions.add(new Transaction("Withdraw", amount, balance));
    }

    @Override
    public void calculateInterest() {

    }
}
