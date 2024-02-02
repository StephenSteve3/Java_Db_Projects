import java.util.ArrayList;
import java.util.Date;

public class Customer {

    private String name;
    private Gender gender;
    private Date dob;
    private String panNumber;
    private long mobile;
    private ArrayList<Account> accounts;
    
    public Customer(String name, long mobile, Gender gender, Date dob, String panNumber) {
        this.name = name;
        this.mobile = mobile;
        this.gender = gender;
        this.dob = dob;
        this.panNumber = panNumber;
        this.accounts = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public java.sql.Date getDob() {
        return new java.sql.Date(dob.getTime());
    }
    
    public long getMobile() {
        return mobile;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void addAccount(Account account){
        accounts.add(account);
    }

    public Account getAccount(long accountNumber,String password) {
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
        return name;
    }

}