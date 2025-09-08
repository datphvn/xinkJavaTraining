public class SavingsAccount extends Account {
    private final double annualInterestRate; // ví dụ 0.05 = 5%/year

    public SavingsAccount(String accountNumber, double annualInterestRate) {
        super(accountNumber);
        this.annualInterestRate = annualInterestRate;
    }

    @Override
    public void calculateInterest() {
        double monthlyRate = annualInterestRate / 12;
        double interest = balance * monthlyRate;
        balance += interest;
        transactions.add(new Transaction("Interest", interest, balance));
    }
}
