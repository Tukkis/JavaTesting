package src.java.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import src.java.Account;

public class AccountService {

    private final String accountsFile;

    public AccountService(String accountsFile) {
        this.accountsFile = accountsFile;
    }

    public Map<String, Account> accounts = new HashMap<>();

    public Map<String, Account> readAccounts() {
        try {
            File accountFile = new File(accountsFile);
            Scanner fileReader = new Scanner(accountFile);
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] parts = line.split(",");
                String name = parts[0].trim();
                double balance = Double.parseDouble(parts[1].trim());
                accounts.put(name, new Account(name, balance));
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return accounts;
    }

    public double getBalance(String name){
        Account account = accounts.get(name);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + name);
        }
        double balance = account.getBalance();
        return balance;
    }

    public boolean withdraw(String name, int amount) {
        Account account = accounts.get(name);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + name);
        }
        return account.withdraw(amount);
    }

    public void deposit(String name, int amount) {
        Account account = accounts.get(name);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + name);
        }
        account.deposit(amount);
    }

}