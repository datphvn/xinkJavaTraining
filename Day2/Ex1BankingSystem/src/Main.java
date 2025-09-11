

import java.math.BigDecimal;
import java.util.Currency;

public class Main {
    public static void main(String[] args) {
        SavingsAccount sa = new SavingsAccount("Alice", new BigDecimal("1000"),
                Currency.getInstance("USD"), new BigDecimal("0.02"));

        CheckingAccount ca = new CheckingAccount("Bob", new BigDecimal("500"),
                Currency.getInstance("USD"), new BigDecimal("300"));

        sa.deposit(new BigDecimal("200"));
        sa.withdraw(new BigDecimal("300"));
        sa.applyMonthlyInterest();

        ca.withdraw(new BigDecimal("700")); // overdraft
        ca.deposit(new BigDecimal("100"));

        System.out.println("=== Savings Transactions ===");
        sa.getTransactions().forEach(System.out::println);

        System.out.println("=== Checking Transactions ===");
        ca.getTransactions().forEach(System.out::println);
    }
}
