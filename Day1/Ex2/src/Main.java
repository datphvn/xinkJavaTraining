public class Main {
    public static void main(String[] args) {
        BankSystem bank = new BankSystem();

        CheckingAccount ca = new CheckingAccount("CHK1001", 500);
        SavingsAccount sa = new SavingsAccount("SAV2001", 0.05);

        bank.addAccount(ca);
        bank.addAccount(sa);

        // Deposit / Withdraw
        ca.deposit(1000);
        ca.withdraw(200);

        // Transfer
        bank.transfer("CHK1001", "SAV2001", 300);

        // Freeze account test
        ca.freeze();
        try {
            ca.deposit(100);
        } catch (Exception e) {
            System.out.println("Expected error (frozen): " + e.getMessage());
        }
        ca.unfreeze();
        ca.deposit(100); // OK sau khi unfreeze

        // Apply monthly interest to savings
        bank.applyMonthlyInterest();

        // Generate Statements
        bank.generateAllStatements();
    }
}
