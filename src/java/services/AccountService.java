package src.java.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import src.java.Account;
import src.java.Log;
import src.java.TransactionType;

public class AccountService {

    private LogService logService;
    private final String accountsFile;

    public AccountService(String accountsFile, LogService logService) {
        this.accountsFile = accountsFile;
        this.logService = logService;
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

    public void createAccount(String name, double balance){
        try {
            FileWriter myWriter = new FileWriter(accountsFile, true);
            Account account = new Account(name, balance);
            accounts.put(name, account);
            myWriter.write(name + String.valueOf(balance) + System.lineSeparator());
            myWriter.close();
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public double getBalance(String name){
        Account account = accounts.get(name);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + name);
        }
        double balance = account.getBalance();
        return balance;
    }

    public void withdraw(String name, int amount) {
        Account account = accounts.get(name);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + name);
        }
        account.withdraw(amount);
        logService.addLog(new Log(TransactionType.WITHDRAW, amount, name, null));
    }

    public void deposit(String name, int amount) {
        Account account = accounts.get(name);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + name);
        }
        account.deposit(amount);
        logService.addLog(new Log(TransactionType.DEPOSIT, amount, name, null));
    }

    public void transfer(String fromName, String recipantName, int amount) {
        Account account1 = accounts.get(fromName);
        Account account2 = accounts.get(recipantName);
        if (account1 == null || account2 == null) {
            throw new IllegalArgumentException("Account not found");
        }
        account1.withdraw(amount);
        account2.deposit(amount);
        logService.addLog(new Log(TransactionType.TRANSFER, amount, fromName, recipantName));
    }
}