import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    
    private String name;
    private String branch;
    private ArrayList<Customer> customers;
    private ArrayList<Account> accounts;
    private int minimumBalance;
    private long accNoCounter = 100_000_111_111L;
    private int transactionCounter = 10001;

    public Bank(String name, String branch) {
        this.name = name;
        this.branch = branch;
        customers = new ArrayList<>();
        accounts = new ArrayList<>();
    }

    public Bank(String name, String branch, int minimumBalance){
        this(name, branch);
        this.minimumBalance = minimumBalance;
    }

    public String getName() {
        return name;
    }

    public String getBranch() {
        return branch;
    }

    public int getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(int minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public void addCustomer(Customer c){
        this.customers.add(c);
        DbHelper.storeData(c);
    }

    public void addAccount(Customer c, Account account){
        c.addAccount(account);
        accounts.add(account);
    }

    public void deposit(long accountNumber, int amount){
        Account account = getAccount(accountNumber);
        account.deposit(amount);
        DbHelper.updateData(account);
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        int transaction_id = transactionCounter++;
        Transaction t = new Transaction(transaction_id, account.getAccountNumber(), amount, TransactionType.DEPOSIT,date,time);
        DbHelper.storeData(t);
    }

    public void withdraw(long accountNumber, String password, int amount) {
        Account account = getAccount(accountNumber,password);
        account.withdraw(amount, minimumBalance);
        DbHelper.updateData(account);
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        int transaction_id = transactionCounter++;
        Transaction t = new Transaction(transaction_id, account.getAccountNumber(), amount, TransactionType.WITHDRAW,date,time);
        DbHelper.storeData(t);
    }

    public void tranferAmount(long accountNumber1, String password, long accountNumber2, int amount) {
        Account account1 = getAccount(accountNumber1,password);
        Account account2 = getAccount(accountNumber2);
        account1.withdraw(amount, minimumBalance);
        account2.deposit(amount);
        DbHelper.updateData(account1);
        DbHelper.updateData(account2);
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        int transaction_id = transactionCounter++;
        Transaction t = new Transaction(transaction_id, account1.getAccountNumber(), amount, TransactionType.WITHDRAW,date,time);
        DbHelper.storeData(t);
        t = new Transaction(transaction_id, account2.getAccountNumber(), amount, TransactionType.DEPOSIT,date,time);
        DbHelper.storeData(t);
    }

    public String getBalance(long accountNumber, String password) {
        Account account = getAccount(accountNumber, password);
        return account.toString();
    }

    public Customer getCustomer(String panNumber){
        for(Customer c : customers){
            if(panNumber.equals(c.getPanNumber())){
                return c;
            }
        }
        throw new InvalidCredentialsException("PAN Number is invalid!");
    }

    private Account getAccount(long accountNumber,String password) {
        for (Account account : accounts) {
            if(account.getAccountNumber() == accountNumber){
                if(account.authenticate(password)) return account;
                throw new InvalidCredentialsException("Password is wrong!");
            }
        }
        throw new InvalidCredentialsException("Account Number is invalid!");
    }

    @Override
    public String toString() {
        return name+branch;
    }

    public long createAccount(Customer c, AccountType type, String password) {
        Account account = new Account(accNoCounter++, password, type);
        this.addAccount(c, account);
        DbHelper.storeData(account, c, password);
        deposit(account.getAccountNumber(),minimumBalance);
        return account.getAccountNumber();
    }

    private Account getAccount(long accountNumber) {
        for (Account account : accounts) {
            if(account.getAccountNumber() == accountNumber){
                return account;
            }
        }
        throw new InvalidCredentialsException("Account Number is invalid!");
    }

    public List<Transaction> getTransactions(long accountNumber, String password) {
        Account account = getAccount(accountNumber,password);        
        return DbHelper.getTransactions(account);
    }

}