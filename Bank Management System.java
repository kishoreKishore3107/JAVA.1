import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


class BankAccount {
    private static final AtomicInteger ID_GEN = new AtomicInteger(1000);

    private final int accountId;
    private String holderName;
    private double balance;
    private List<String> transactionHistory = new ArrayList<>();

    public BankAccount(String holderName, double initialDeposit) {
        this.accountId = ID_GEN.incrementAndGet();
        this.holderName = holderName;
        this.balance = initialDeposit;
        transactionHistory.add("Account created with ₹" + initialDeposit);
    }

    public synchronized void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposited ₹" + amount);
    }

    public synchronized boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add("Withdrew ₹" + amount);
            return true;
        }
        transactionHistory.add("Failed withdrawal attempt of ₹" + amount);
        return false;
    }

    public synchronized boolean transferTo(BankAccount target, double amount) {
        if (this.withdraw(amount)) {
            target.deposit(amount);
            transactionHistory.add("Transferred ₹" + amount + " to Account " + target.accountId);
            return true;
        }
        return false;
    }

    public void showDetails() {
        System.out.println("\nAccount ID: " + accountId);
        System.out.println("Holder: " + holderName);
        System.out.println("Balance: ₹" + balance);
    }

    public void showHistory() {
        System.out.println("\nTransaction History for " + holderName);
        for (String t : transactionHistory) {
            System.out.println(t);
        }
    }
}

class Bank {
    private List<BankAccount> accounts = new ArrayList<>();

    public BankAccount createAccount(String name, double deposit) {
        BankAccount acc = new BankAccount(name, deposit);
        accounts.add(acc);
        return acc;
    }

    public void showAllAccounts() {
        System.out.println("\n=== ALL ACCOUNTS ===");
        for (BankAccount acc : accounts) {
            acc.showDetails();
        }
    }
}

public class BankManagementSystem {

    public static void main(String[] args) {

        Bank bank = new Bank();


        BankAccount acc1 = bank.createAccount("Arun", 5000);
        BankAccount acc2 = bank.createAccount("Kiran", 3000);

        acc1.deposit(2000);
        acc1.withdraw(1000);

        acc2.deposit(500);
        acc2.withdraw(700);


        acc1.transferTo(acc2, 1500);


        bank.showAllAccounts();

        acc1.showHistory();
        acc2.showHistory();
    }
}
