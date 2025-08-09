package src.java;


public class Log {

    private TransactionType type;
    private double amount;
    private String accountName1; 
    private String accountName2; 

    public Log(TransactionType type, double amount, String accountName1, String accountName2) {
        this.type = type;
        this.accountName1 = accountName1;
        this.accountName2 = accountName2;
        this.amount = amount;
    }

    public Log(TransactionType type, double amount, String accountName) {
        this.type = type;
        this.accountName1 = accountName;
        this.accountName2 = null;
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getAccountName1() {
        return accountName1;
    }

    public String getAccountName2() {
        return accountName2;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        switch (type) {
            case DEPOSIT:
                return String.format("Deposit %.2f to %s", amount, accountName1);
            case WITHDRAW:
                return String.format("Withdraw %.2f from %s", amount, accountName1);
            case TRANSFER:
                return String.format("Transfer %.2f from %s to %s", amount, accountName1, accountName2);
            default:
                return "Unknown transaction";
        }
    }
}