package users;

import accounts.Account;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private List<Account> accounts;

    public User(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getName() {
        return name;
    }
}
