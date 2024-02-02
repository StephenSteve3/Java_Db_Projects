import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class App {

    static Scanner in = new Scanner(System.in);
    static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    public static void main(String[] args){
        Bank bank = new Bank("ABC Bank", "Sivakasi", 500);
        while(true){
            try {
                System.out.println("-------------------------------------------------");
                System.out.println("1.Add Customer \n2.Create Account \n3.Banking \n4.Exit ");
                System.out.println("Enter your choice :");
                int ch = in.nextInt();
                switch(ch){
                    case 1:
                        addCustomer(bank);
                        break;
                    
                    case 2:
                        createAccount(bank);
                        break;

                    case 3:
                        banking(bank);
                        break;

                    case 4:
                        DbHelper.closeResource();
                        System.exit(0);
                    
                    default:
                        System.out.println("Invalid Choice!");
                }
            } catch (Exception e) {
                in.nextLine();
                System.out.println(e);
            }
        }
    }

    private static void banking(Bank bank) {
        while(true){
            try {
                System.out.println("--------------------------------");
                System.out.println("\n1.Deposit \n2.Withdraw \n3.Fund tranfer \n4.Check Balance \n5.Get Transactions \n6.Exit ");
                System.out.println("Enter your choice :");
                int ch = in.nextInt();
                switch(ch){
                    case 1:
                        deposit(bank);
                        break;
                    
                    case 2:
                        withdraw(bank);
                        break;

                    case 3:
                        fundTranfer(bank);
                        break;

                    case 4:
                        checkbalance(bank);
                        break;

                    case 5:
                        getTransactions(bank);
                        break;

                    case 6:
                        return;
                    
                    default:
                        System.out.println("Invalid Choice!");
                }
            } catch (Exception e) {
                in.nextLine();
                System.out.println(e);
            }
        }
    }

    private static void getTransactions(Bank bank) {
        long accountNumber = getAccountNumber();
        String pass = getString("Enter your password : ");
        List<Transaction> transactions = bank.getTransactions(accountNumber,pass);
        System.out.println("\n  Id \tAccount Number\t Amount  \tType\t   Date   \t  Time ");
        System.out.println("----------------------------------------------------------------------------------------");
        for (Transaction t : transactions) {
            System.out.println(t.getId() + "\t" + t.getAccountNumber() + "\t" + t.getAmount() + "\t" + t.getType() + "\t" + t.getDate() + "\t" + t.getTime());
        }
    }

    private static void checkbalance(Bank bank) {
        long accountNumber = getAccountNumber();
        String pass = getString("Enter your password : ");
        System.out.println(bank.getBalance(accountNumber,pass));
    }

    private static void fundTranfer(Bank bank) {
        System.out.println("Your Account details :");
        long accountNumber = getAccountNumber();
        in.nextLine();
        String pass = getString("Enter your password : ");
        int amount = getAmount();
        System.out.println("Transfer Account details :");
        long accountNumber2 = getAccountNumber();
        bank.tranferAmount(accountNumber, pass, accountNumber2, amount);
        System.out.println("Fund Tranferred Succesfully!");
    }

    private static void withdraw(Bank bank) {
        long accountNumber = getAccountNumber();
        in.nextLine();
        String pass = getString("Enter your password : ");
        int amount = getAmount();
        bank.withdraw(accountNumber,pass,amount);
        System.out.println("Amount Withdrawn Succesfully!");
    }

    private static void deposit(Bank bank) {
        long accountNumber = getAccountNumber();
        int amount = getAmount();
        bank.deposit(accountNumber, amount);
        System.out.println("Amount Deposited Succesfully!");
    }

    private static int getAmount() {
        while(true){
            System.out.println("Enter amount :");
            int amount = in.nextInt();
            if(amount > 0) return amount;
        }
    }

    private static long getAccountNumber() {
        while(true){
            System.out.println("Enter the 12-digit Account Number:");
            long acc = in.nextLong();
            if((acc+"").length() == 12){
                return acc;
            }
        }
    }

    private static void createAccount(Bank bank) {
        in.nextLine();
        System.out.println("Are you a Customer? (y/n) :");
        if(!in.nextLine().toLowerCase().equals("y")){
            addCustomer(bank);
        }
        String pan = getPanNumber();
        Customer c = bank.getCustomer(pan);
        AccountType type = getAccountType();
        in.nextLine();
        String pass = getPassword();
        long accountNumber = bank.createAccount(c,type,pass);
        System.out.println("Account Created");
        System.out.println("Your Account Number is " + accountNumber);
    }

    private static String getPassword() {
        String pass = getString("Set Password :");
        String pass1 = getString("Confirm Password :");
        if(pass.equals(pass1)){
            return pass;
        }
        return getPassword();
    }

    private static AccountType getAccountType() {
        AccountType[] accountTypes = AccountType.values();
        System.out.println("\nAccount Types :");
        for (int i = 0; i < accountTypes.length; i++) {
            System.out.println((i+1) + " -> " + accountTypes[i]);
        }
        while (true) {
            System.out.println("Select Account type :");
            int ch = in.nextInt();
            if(ch > 0 && ch < accountTypes.length)
                return accountTypes[ch-1];
        }
    }

    private static void addCustomer(Bank bank) {
        in.nextLine();
        String name = getString("Enter your name :");
        Gender gender = getGender();
        long mobile = getMobileNumber();
        in.nextLine();
        Date dob = getDate("Enter Date of Birth :");
        String pan = getPanNumber();
        bank.addCustomer(new Customer(name, mobile, gender, dob, pan));
        System.out.println("Customer Added");
    }

    private static long getMobileNumber() {
        while(true){
            System.out.println("Enter the 10-digit Mobile Number:");
            long mobile = in.nextLong();
            if((mobile+"").length() == 10){
                return mobile;
            }
        }
    }

    private static String getPanNumber() {
        String pattern = "[A-Z]{5}[0-9]{4}[A-Z]";
        String s = null;
        do {
            System.out.println("Enter PAN Number(Format: \"AAAAA1111A\"):");
            s = in.nextLine().toUpperCase();
        } while (!s.matches(pattern));
        return s;
    }

    private static Date getDate(String string) {
        Date date = null;
        while(date == null){
            System.out.println(string);
            String s = in.nextLine();
            try {
                date = sdf.parse(s);
            } catch (ParseException e) {}
        }
        return date;
    }

    private static Gender getGender() {
        System.out.println("\n1.Male \n2.Female");
        while(true){
            System.out.println("Select gender : ");
            int ch = in.nextInt();
            switch(ch){
                case 1 : return Gender.MALE;
                case 2 : return Gender.FEMALE;
            }
        }
    }

    private static String getString(String string) {
        System.out.println(string);
        string = "";
        while(string.isEmpty()){
            string = in.nextLine();
        }
        return string;
    }
}