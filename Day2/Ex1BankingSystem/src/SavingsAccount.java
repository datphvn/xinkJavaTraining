

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingsAccount extends Account {
    private static final BigDecimal MINIMUM_BALANCE = new BigDecimal("500");
    private final BigDecimal interestRate;
    private LocalDate lastInterestCalculation;

    public SavingsAccount(String holder, BigDecimal initialDeposit, java.util.Currency currency, BigDecimal interestRate) {
        super(holder, initialDeposit, currency);
        this.interestRate = interestRate;
        this.lastInterestCalculation = LocalDate.now();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        lock.lock();
        try {
            validateActive();
            BigDecimal newBalance = getBalance().subtract(amount);
            if (newBalance.compareTo(MINIMUM_BALANCE) < 0)
                throw new IllegalArgumentException("Cannot go below minimum balance");
            setBalance(newBalance);
            recordTransaction(TransactionType.WITHDRAWAL, amount);
        } finally {
            lock.unlock();
        }
    }

    public void applyMonthlyInterest() {
        LocalDate today = LocalDate.now();
        if (today.isAfter(lastInterestCalculation.plusMonths(1))) {
            BigDecimal interest = getBalance().multiply(interestRate);
            setBalance(getBalance().add(interest));
            recordTransaction(TransactionType.INTEREST, interest);
            lastInterestCalculation = today;
        }
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }
}
