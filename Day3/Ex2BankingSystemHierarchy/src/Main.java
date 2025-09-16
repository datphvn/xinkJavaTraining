import interest.SimpleInterestStrategy;
import services.BankService;
import transactions.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankService bank = new BankService();
        Scanner sc = new Scanner(System.in);

        // create sample accounts
        String acc1 = bank.openChecking(List.of("Alice"), "USD", BigDecimal.valueOf(1000), BigDecimal.valueOf(500), BigDecimal.valueOf(5));
        String acc2 = bank.openSavings(List.of("Bob"), "USD", BigDecimal.valueOf(2000), 0.02);
        String acc3 = bank.openCreditCard(List.of("Carol"), "USD", BigDecimal.valueOf(5000), BigDecimal.valueOf(0.02), BigDecimal.valueOf(25));
        String acc4 = bank.openInvestment(List.of("Dave"), "USD", BigDecimal.valueOf(10000), new SimpleInterestStrategy(BigDecimal.valueOf(0.05)));

        System.out.println("Sample accounts: " + acc1 + ", " + acc2 + ", " + acc3 + ", " + acc4);

        while (true) {
            System.out.println("\n=== BANK MENU ===");
            System.out.println("1) List accounts");
            System.out.println("2) Deposit");
            System.out.println("3) Withdraw / Charge");
            System.out.println("4) Transfer");
            System.out.println("5) Statement");
            System.out.println("6) Apply monthly updates");
            System.out.println("0) Exit");
            System.out.print("choice> ");
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            int choice;
            try { choice = Integer.parseInt(line); } catch (Exception e) { System.out.println("Invalid"); continue; }

            switch (choice) {
                case 1 -> {
                    System.out.println("Accounts: " + bank.listAccounts());
                }
                case 2 -> {
                    System.out.print("Account: "); String a = sc.nextLine().trim();
                    System.out.print("Amount: "); BigDecimal amt = new BigDecimal(sc.nextLine().trim());
                    boolean ok = bank.deposit(a, amt, "CLI");
                    System.out.println(ok ? "OK" : "Failed");
                }
                case 3 -> {
                    System.out.print("Account: "); String a = sc.nextLine().trim();
                    System.out.print("Amount: "); BigDecimal amt = new BigDecimal(sc.nextLine().trim());
                    boolean ok = bank.withdraw(a, amt, "CLI");
                    System.out.println(ok ? "OK" : "Failed");
                }
                case 4 -> {
                    System.out.print("From: "); String f = sc.nextLine().trim();
                    System.out.print("To: "); String t = sc.nextLine().trim();
                    System.out.print("Amount: "); BigDecimal amt = new BigDecimal(sc.nextLine().trim());
                    boolean ok = bank.transfer(f, t, amt, "CLI");
                    System.out.println(ok ? "OK" : "Failed");
                }
                case 5 -> {
                    System.out.print("Account: "); String a = sc.nextLine().trim();
                    List<Transaction> stm = bank.getStatement(a);
                    if (stm.isEmpty()) System.out.println("No tx");
                    else stm.forEach(System.out::println);
                }
                case 6 -> {
                    bank.applyMonthlyUpdates();
                    System.out.println("Applied monthly updates to all accounts.");
                }
                case 0 -> { System.out.println("Bye"); sc.close(); return; }
                default -> System.out.println("Invalid");
            }
        }
    }
}
