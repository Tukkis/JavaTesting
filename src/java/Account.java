package src.java;

public class Account {
    private String name;
    private double balance;

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false; // not enough balance
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        }
        balance -= amount;
        return true;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return name + "," + String.format("%.2f", balance);
    }
}