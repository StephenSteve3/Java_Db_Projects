import java.util.ArrayList;
import java.util.Scanner;

public class App {
    static Scanner in = new Scanner(System.in);
    public static void main(String[] args){
        StockManipulator stockManipulator = new StockManipulator();
        stockManipulator.setDaemon(true);
        stockManipulator.start();
        while(true){
            try {
                System.out.println("--------------------------------");
                System.out.println("1. LOGIN \n2. SIGN UP \n3.EXIT");
                System.out.println("--------------------------------");
                int ch = getInt("Enter your choice : ");
                switch(ch){
                    case 1:
                        login();
                        break;
                    
                    case 2:
                        signUp();
                        break;
                    
                    case 3:
                        DbHelper.closeResource();
                        return;
    
                    default:
                        System.out.println("Invalid Choice !");
                }
            } catch (Exception e) {
                in.nextLine();
                System.out.println(e);
            }
        }
    }

    private static void signUp() {
        in.nextLine();
        String email = getEmail();
        if(!DbHelper.isNewEmail(email)){
            System.out.println("Email already exists!");
            return;
        }
        String password = getPassword();
        String name = getString("Enter name : ");
        Gender gender = getGender();
        long phone = getMobileNumber();
        long aadhar = getAadhar();
        Trader trader = new Trader(name, gender, email, aadhar, phone, 50000);
        int id = DbHelper.storeData(trader, password);
        if(id == -1){
            System.out.println("Aadhar already registered!");
            return;
        }
        trader.setId(id);
        System.out.println("Signed Up Succesfully");
        menu(trader);
    }

    private static void login() {
        in.nextLine();
        String email = getEmail();
        String password = getString("Enter Password :");
        Trader trader = DbHelper.getTrader(email,password);
        if(trader == null){
            System.out.println("Invalid Credentials!");
            return;
        } 
        System.out.println("Logged In Successfully!");
        menu(trader);
    }

    private static void menu(Trader trader) {
        while(true){
            try {
                System.out.println("************************");
                System.out.println("1.Buy Stock");
                System.out.println("2.Sell Stock");
                System.out.println("3.Portfolio");
                System.out.println("4.Log out");
                System.out.println("************************");
                int ch = getInt("Enter your choice :");
                switch(ch){
                    case 1: buyStock(trader);
                    break;

                    case 2: sellStock(trader);
                    break;

                    case 3: portfolio(trader);
                    break;

                    case 4: return;
                }
            } catch (Exception e) {
                System.out.println(e);
            }   
        }
    }

    private static void sellStock(Trader trader) {
        System.out.println("All Holdings :");
        if(showHolding(trader)){
            System.out.println("No Holding available!");
            return;
        }
        int id = getInt("Enter Holding ID : ");
        Holding holding = DbHelper.getHolding(id, trader);
        if(holding == null){
            System.out.println("Invalid Holding id!");
            return;
        }
        System.out.println(holding);
        double profit = (holding.getStock().getPrice()-holding.getPrice())*holding.getQuantity();
        System.out.println("Profit/Loss : " + profit);
        in.nextLine();
        if(!dywtc()){
            return;
        }
        trader.addAmount(holding.getStock().getPrice()*holding.getQuantity());
        DbHelper.removeHolding(holding);
        System.out.println("Stock sold Successfully!");
    }

    private static void portfolio(Trader trader) {
        ArrayList<Holding> holdings = DbHelper.getHoldings(trader);
        System.out.println("####################################");
        System.out.println("              Portfolio             ");
        System.out.println("Name : " + trader.getName());
        System.out.printf("Available Amount : %.2f\n", trader.getAmount());
        System.out.println("------------------------------------");
        double totalProfit = 0;
        for (Holding holding : holdings) {
            System.out.println(holding);
            double profit = (holding.getStock().getPrice()-holding.getPrice())*holding.getQuantity();
            totalProfit += profit;
            System.out.printf("Profit/Loss : %.2f\n", profit);
            System.out.println();
        }
        System.out.println("------------------------------------");
        System.out.printf("Total Profit/Loss : %.2f\n", totalProfit);
        System.out.println("These details might vary in time.");
        System.out.println("####################################");
    }

    private static boolean dywtc() {
        System.out.println("Do you want to continue?(y/n) : ");
        String reply;
        do{
            reply = in.nextLine().trim().toLowerCase();
        }while(!(reply.equals("y") || reply.equals("n")));
        return reply.equals("y");
    }

    private static boolean showHolding(Trader trader) {
        ArrayList<Holding> holdings = DbHelper.getHoldings(trader);
        for (Holding holding : holdings) {
            System.out.println();
            System.out.println(holding);
        }
        return holdings.isEmpty();
    }

    private static void buyStock(Trader trader) {
        System.out.println("All Stocks : ");
        showStocks();
        int id = getInt("Enter Stock id : ");
        Stock stock = DbHelper.getStock(id);
        if(stock.getPrice() == -1){
            System.out.println("Invalid Stock id!");
            return;
        }
        System.out.println(stock);
        int quantity = getPositiveInt("Enter quantity :");
        if(trader.getAmount() < stock.getPrice()*quantity){
            System.out.println("You do not have enough money!");
            return;
        }
        in.nextLine();
        if(!dywtc()){
            return;
        }
        trader.deductAmount(stock.getPrice()*quantity);
        DbHelper.storeData(new Holding(trader, stock, stock.getPrice(), quantity));
        System.out.println("Stock bought Successfully!");
    }

    private static void showStocks() {
        ArrayList<Stock> stocks = DbHelper.getStocks();
        for (Stock stock : stocks) {
            System.out.println(stock);
        }
        System.out.println("\n Price of the stock might vary in time.\n");
    }

    private static int getInt(String string) {
        System.out.println(string);
        return in.nextInt();
    }

    private static int getPositiveInt(String string) {
        int id;
        while((id = getInt(string)) < 1){
            System.out.println("Invalid!");
        }
        return id;
    }

    private static String getPassword() {
        String pass = getString("Set Password :");
        String pass1 = getString("Confirm Password :");
        if(pass.equals(pass1)){
            return pass;
        }
        System.out.println("Passwords did not match");
        return getPassword();
    }

    private static long getMobileNumber() {
        while(true){
            System.out.println("Enter the 10-digit Mobile Number:");
            long mobile = in.nextLong();
            if((mobile+"").length() == 10)
                return mobile;
            else
                System.out.println("Invalid! Try Again");
        } 
    }

    private static String getString(String string) {
        System.out.println(string);
        string = "";
        while(true){
            string = in.nextLine();
            if(!string.isEmpty()) return string;
            else System.out.println("Invalid! Try Again!");
        }
    }

    private static String getEmail() {
        String pattern = "[a-zA-Z0-9][a-zA-Z0-9_.]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+";
        String s = null;
        do {
            System.out.println("Enter Email:");
            s = in.nextLine();
            if(s.matches(pattern)) return s;
            else System.out.println("Invalid Email! Try again!");
        } while (true);
    }
    
    private static Gender getGender() {
        System.out.println("\n1.Male \n2.Female");
        while(true){
            System.out.println("Select gender : ");
            int ch = in.nextInt();
            switch(ch){
                case 1 : return Gender.MALE;
                case 2 : return Gender.FEMALE;
                default : System.out.println("Invalid Input! Try Again!");
            }
        }
    }

    private static long getAadhar() {
        while(true){
            System.out.println("Enter the 12-digit Aadhar Number:");
            long aadhar = in.nextLong();
            if((aadhar+"").length() == 12)
                return aadhar;
            else
                System.out.println("Invalid! Try Again");
        }
    }
}