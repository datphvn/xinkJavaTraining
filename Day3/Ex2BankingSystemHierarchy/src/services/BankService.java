package services;

import accounts.*;
import interest.SimpleInterestStrategy;
import interest.InterestStrategy;
import transactions.Transaction;
import transactions.TransactionType;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BankService {
    private final Map<String, Account> accounts = new HashMap<>();
    private final FraudDetector fraudDetector;
    private final AtomicInteger accCounter = new AtomicInteger(1000);

    public BankService() {
        this.fraudDetector = new FraudDetector(BigDecimal.valueOf(10000));
    }

    public String openChecking(List<String> holders, String currency, BigDecimal initial,
                               BigDecimal overdraftLimit, BigDecimal monthlyFee) {
        String accNum = "CHK" + accCounter.getAndIncrement();
        Account a = new CheckingAccount(accNum, holders, currency, initial, overdraftLimit, monthlyFee);
        accounts.put(accNum, a);
        return accNum;
    }

    public String openSavings(List<String> holders, String currency, BigDecimal initial, double annualRate) {
        String accNum = "SAV" + accCounter.getAndIncrement();
        InterestStrategy strat = new SimpleInterestStrategy(BigDecimal.valueOf(annualRate));
        Account a = new SavingsAccount(accNum, holders, currency, initial, BigDecimal.ZERO, strat);
        accounts.put(accNum, a);
        return accNum;
    }

    public String openCreditCard(List<String> holders, String currency, BigDecimal creditLimit,
                                 BigDecimal monthlyInterestRate, BigDecimal lateFee) {
        String accNum = "CC" + accCounter.getAndIncrement();
        Account a = new CreditCardAccount(accNum, holders, currency, creditLimit, monthlyInterestRate, lateFee);
        accounts.put(accNum, a);
        return accNum;
    }

    public String openInvestment(List<String> holders, String currency, BigDecimal initial, InterestStrategy strat) {
        String accNum = "INV" + accCounter.getAndIncrement();
        Account a = new InvestmentAccount(accNum, holders, currency, initial, strat);
        accounts.put(accNum, a);
        return accNum;
    }

    public boolean deposit(String accountNumber, BigDecimal amount, String performedBy) {
        Account a = accounts.get(accountNumber);
        if (a == null) return false;
        Transaction preview = new Transaction(TransactionType.DEPOSIT, null, accountNumber, amount, a.getBalance().add(amount));
        if (fraudDetector.isSuspicious(a.getTransactionHistory(), preview)) {
            System.out.println("⚠ Fraud detected on deposit to " + accountNumber);
        }
        return a.deposit(amount, performedBy);
    }

    public boolean withdraw(String accountNumber, BigDecimal amount, String performedBy) {
        Account a = accounts.get(accountNumber);
        if (a == null) return false;
        Transaction preview = new Transaction(TransactionType.WITHDRAWAL, accountNumber, null, amount, a.getBalance().subtract(amount));
        if (fraudDetector.isSuspicious(a.getTransactionHistory(), preview)) {
            System.out.println("⚠ Fraud detected on withdrawal from " + accountNumber);
            return false;
        }
        return a.withdraw(amount, performedBy);
    }

    public boolean transfer(String fromAcc, String toAcc, BigDecimal amount, String performedBy) {
        Account src = accounts.get(fromAcc);
        Account dst = accounts.get(toAcc);
        if (src == null || dst == null) return false;

        synchronized (this) {
            Transaction preview = new Transaction(TransactionType.TRANSFER, fromAcc, toAcc, amount, src.getBalance().subtract(amount));
            if (fraudDetector.isSuspicious(src.getTransactionHistory(), preview)) {
                System.out.println("⚠ Fraud detected - blocking transfer from " + fromAcc);
                return false;
            }
            boolean w = src.withdraw(amount, performedBy);
            if (!w) return false;
            boolean d = dst.deposit(amount, performedBy);
            if (!d) {
                // rollback
                src.deposit(amount, performedBy);
                return false;
            }
            // transactions are already recorded inside deposit/withdraw implementations
            return true;
        }
    }

    public Account getAccount(String accNo) { return accounts.get(accNo); }

    public void applyMonthlyUpdates() {
        for (Account a : accounts.values()) {
            a.applyMonthlyUpdates();
        }
    }

    public List<Transaction> getStatement(String accNo) {
        Account a = accounts.get(accNo);
        return a == null ? Collections.emptyList() : a.getTransactionHistory();
    }

    public Set<String> listAccounts() { return Collections.unmodifiableSet(accounts.keySet()); }
}
