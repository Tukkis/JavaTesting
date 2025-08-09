package src.java.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import src.java.Account;
import src.java.Log;
import src.java.services.LogService;
import src.java.services.AccountService;
import src.java.TransactionType;

class AccountServiceTest {

    private Path accountsFilePath;
    private Path logFilePath;
    private LogService logService;
    private AccountService accountService;

    @BeforeEach
void setUp() throws IOException {
    accountsFilePath = Files.createTempFile("accountsTest", ".txt");
    Files.write(accountsFilePath, """
        Alice,1000.50
        Bob,250.75
        Charlie,560.00
    """.getBytes());

    logFilePath = Files.createTempFile("logsTest", ".txt");
    Files.write(logFilePath, """
        DEPOSIT,200.00,Alice,
        WITHDRAW,50.75,Bob,
        TRANSFER,100.00,Alice,Bob
    """.getBytes());

    logService = new LogService(logFilePath.toString());
    accountService = new AccountService(accountsFilePath.toString(), logService);
}

@AfterEach
void tearDown() throws IOException {
    Files.deleteIfExists(accountsFilePath);
    Files.deleteIfExists(logFilePath);
}

    @Test
    void testReadAccounts() {
        Map<String, Account> accounts = accountService.readAccounts();

        assertEquals(3, accounts.size());
        assertEquals(1000.50, accounts.get("Alice").getBalance());
        assertEquals(250.75, accounts.get("Bob").getBalance());
        assertEquals(560.00, accounts.get("Charlie").getBalance());
    }

    @Test
    void testCreateAccount() throws IOException {
        accountService.readAccounts();
        accountService.createAccount("David", 300.00);

        assertEquals(300.00, accountService.getBalance("David"));

        String fileContent = Files.readString(accountsFilePath);
        assertTrue(fileContent.contains("David300.0"));
    }

    @Test
    void testGetBalance_existingAccount() {
        accountService.readAccounts();
        assertEquals(1000.50, accountService.getBalance("Alice"));
    }

    @Test
    void testGetBalance_nonExistingAccount() {
        accountService.readAccounts();
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
        accountService.getBalance("NonExistent"));
        assertTrue(ex.getMessage().contains("Account not found"));
    }

    @Test
    void testWithdraw_logsCorrectly() {
        accountService.readAccounts();
        accountService.withdraw("Alice", 100);
        List<Log> logs = logService.readLogs();

        assertEquals(900.50, accountService.getBalance("Alice"));
        assertEquals(4, logService.readLogs().size());

        Log log = logs.get(logs.size() - 1);
        assertEquals(TransactionType.WITHDRAW, log.getType());
        assertEquals(100, log.getAmount());
        assertEquals("Alice", log.getAccountName1());
        assertNull(log.getAccountName2());
    }

    @Test
    void testWithdraw_nonExistingAccount() {
        accountService.readAccounts();
        assertThrows(IllegalArgumentException.class, () ->
                accountService.withdraw("Ghost", 50));
    }

    @Test
    void testDeposit_logsCorrectly() {
        accountService.readAccounts();
        accountService.deposit("Bob", 200);
        List<Log> logs = logService.readLogs();

        assertEquals(450.75, accountService.getBalance("Bob"));
        assertEquals(4, logs.size());

        Log log = logs.get(logs.size() - 1);
        assertEquals(TransactionType.DEPOSIT, log.getType());
        assertEquals(200, log.getAmount());
        assertEquals("Bob", log.getAccountName1());
        assertNull(log.getAccountName2());
    }

    @Test
    void testTransfer_logsCorrectly() {
        accountService.readAccounts();
        accountService.transfer("Alice", "Bob", 200);
        List<Log> logs = logService.readLogs();

        assertEquals(800.50, accountService.getBalance("Alice"));
        assertEquals(450.75, accountService.getBalance("Bob"));
        assertEquals(4, logService.readLogs().size());

        Log log = logs.get(logs.size() - 1);
        assertEquals(TransactionType.TRANSFER, log.getType());
        assertEquals(200, log.getAmount());
        assertEquals("Alice", log.getAccountName1());
        assertEquals("Bob", log.getAccountName2());
    }

    @Test
    void testTransfer_missingAccount() {
        accountService.readAccounts();
        assertThrows(IllegalArgumentException.class, () ->
                accountService.transfer("Alice", "Ghost", 50));
    }
}