package src.java.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.java.Log;
import src.java.TransactionType;

public class LogService {

    private final String logsFile;

    public LogService(String logsFile) {
        this.logsFile = logsFile;
    }

    public List<Log> Logs = new ArrayList<>();

    public List<Log> readLogs() {
        List<Log> logs = new ArrayList<>();
        
        try {
            Scanner fileReader = new Scanner(new File(logsFile)); 
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] parts = line.split(",");

                TransactionType type = TransactionType.valueOf(parts[0].trim());
                double amount = Double.parseDouble(parts[1].trim());
                String name1 = parts[2].trim();
                String name2 = parts.length >= 4 ? parts[3].trim() : null;

                Log log;
                if (type == TransactionType.TRANSFER) {
                    log = new Log(type, amount, name1, name2);
                } else {
                    log = new Log(type, amount, name1);
                }

                logs.add(log);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Invalid log format: " + e.getMessage());
        }

        return logs;
    }

    public boolean addLog(Log logToAdd){
        try {
            FileWriter myWriter = new FileWriter(logsFile, true);
            List<String> partsList = new ArrayList<>();
            Object[] values = {
                logToAdd.getType(),
                logToAdd.getAmount(),
                logToAdd.getAccountName1(),
                logToAdd.getAccountName2(),
            };
            for (Object value : values) {
                if(value != null){
                    partsList.add(value.toString());
                } else {
                    partsList.add("");
                }
            }
            myWriter.write(String.join(",", partsList) + System.lineSeparator());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException  e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
