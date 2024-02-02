public class Account {

    private long accountNumber;
    private String password;
    private AccountType type;
    private int amount;

    public Account(long accountNumber, String password, AccountType type) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.type = type;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public AccountType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public void setPassword(String oldPassword, String newPassword) {
        if(oldPassword.equals(password) && newPassword!=null && !newPassword.isEmpty()){
            this.password = newPassword;
            return;
        }
        throw new InvalidCredentialsException("Password is wrong!");
    }

    public boolean authenticate(String password1){
        if(password1.equals(this.password)){
            return true;
        }
        return false;
    }

    public void deposit(int amount){
        if(amount < 1) throw new InvalidAmountException("Invalid Amount Specified!");
        this.amount += amount;
    }

    public void withdraw(int amount, int minimumBalance){
        if(amount < 1) throw new InvalidAmountException("Invalid Amount Specified!");
        if(this.amount - amount < 0) throw new InsufficientBalanceException("Insufficient Balance!");
        if(this.amount - amount < minimumBalance) throw new InsufficientBalanceException("Minimum Balance Required!");
        this.amount -= amount;
    }

    @Override
    public String toString() {
        return "Account Number : " + accountNumber + "\nAccount Type : " + type + "\nAvailable Balance : " + amount;
    }
}