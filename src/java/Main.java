package src.java;

import java.io.File; 
import java.io.FileNotFoundException;  

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class Main {
    
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final String LOG_FILE = "../transactions.txt";


    public static void main(String[] args) {
        System.out.println("hello");
        try {
            Map<String, Account> accounts = readAccounts();
            for (Map.Entry<String, Account> entry : accounts.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Account> readAccounts() {
        Map<String, Account> accounts = new HashMap<>();
        try {
            File accountFile = new File(ACCOUNTS_FILE);
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
}