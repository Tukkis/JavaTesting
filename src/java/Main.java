package src.java;

import src.java.services.AccountService;
import src.java.services.LogService;

import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) {
        System.out.println("hello");
        try {
            AccountService accountService = new AccountService("accounts.txt");
            LogService logService = new LogService("logs.txt");
            Map<String, Account> accounts = accountService.readAccounts();
            List<Log> logs = logService.readLogs();
            for (Account acc : accounts.values()) {
                System.out.println(acc);
            }
            System.out.println("");
            for (Log log : logs) {
                System.out.println(log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}