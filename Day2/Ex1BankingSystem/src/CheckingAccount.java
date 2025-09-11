

import java.math.BigDecimal;

public class CheckingAccount extends Account {
    private BigDecimal overdraftLimit;
    private static final BigDecimal OVERDRAFT_FEE = new BigDecimal("35");

    public CheckingAccount(String holder, BigDecimal initialDeposit, java.util.Currency currency, BigDecimal overdraftLimit) {
        super(holder, initialDeposit, currency);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        lock.lock();
        try {
            validateActive();
            BigDecimal newBalance = getBalance().subtract(amount);

            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                if (newBalance.abs().compareTo(overdraftLimit) <= 0) {
                    setBalance(newBalance.subtract(OVERDRAFT_FEE));
                    recordTransaction(TransactionType.WITHDRAWAL, amount);
                    recordTransaction(TransactionType.OVERDRAFT_FEE, OVERDRAFT_FEE);
                } else {
                    throw new IllegalArgumentException("Overdraft limit exceeded");
                }
            } else {
                setBalance(newBalance);
                recordTransaction(TransactionType.WITHDRAWAL, amount);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }
}
