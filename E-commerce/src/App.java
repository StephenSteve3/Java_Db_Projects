import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    static Scanner in = new Scanner(System.in);
    public static void main(String[] args) {
        while(true){
            try {
                System.out.println("-------------------------------------------------");
                System.out.println("LOGIN AS ");
                System.out.println("1. MERCHANT \n2. CUSTOMER \n3.CLOSE APPLICAION");
                System.out.println("Enter your choice : ");
                int ch = in.nextInt();
                switch(ch){
                    case 1:
                        merchantOptions();
                        break;
                    
                    case 2:
                        customerOptions();
                        break;
                    
                    case 3:
                        DbHelper.closeResource();
                        System.exit(0);
    
                    default:
                        System.out.println("Invalid Choice !");
                }
            } catch (Exception e) {
                in.nextLine();
                System.out.println(e);
            }
        }
    }

    private static void merchantOptions() {
        while(true){
            try {
                System.out.println("------------------------------------");
                System.out.println("1. LOGIN AS MERCHANT \n2. SIGN UP AS MERCHANT \n3.EXIT");
                System.out.println("Enter your choice : ");
                int ch = in.nextInt();
                switch(ch){
                    case 1:
                        merchantLogin();
                        break;
                    
                    case 2:
                        merchantSignUp();
                        break;
                    
                    case 3:
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

    private static void merchantLogin() {
        in.nextLine();
        String email = getEmail();
        String password = getString("Enter Password :");
        Merchant merchant = DbHelper.getMerchant(email,password);
        if(merchant == null){
            System.out.println("Invalid Credentials!");
            return;
        } 
        System.out.println("Logged In Successfully!");
        merchantMenu(merchant);
    }

    private static void merchantSignUp() {
        in.nextLine();
        String email = getEmail();
        if(!DbHelper.isNewMerchantEmail(email)){
            System.out.println("Email already exists!");
            return;
        }
        String password = getPassword();
        String name = getString("Enter name : ");
        long phone = getMobileNumber();
        Address address = getAddress();
        Merchant merchant = new Merchant(name, phone, email, address);
        DbHelper.storeData(merchant, password);
        System.out.println("Signed Up Successfully!");
        merchantMenu(merchant);
    }

    private static void merchantMenu(Merchant merchant) {
        while(true){
            try {
                System.out.println("------------------------------------");
                System.out.println("1. Add Product \n2. View Products \n3.Update Price \n4.Increase Stock \n5.Log out");
                System.out.println("Enter your choice : ");
                int ch = in.nextInt();
                switch(ch){
                    case 1:
                        addProduct(merchant);
                        break;
                    
                    case 2:
                        viewProducts(merchant);
                        break;

                    case 3:
                        updatePrice(merchant);
                        break;
                    
                    case 4:
                        increaseStock(merchant);
                        break;
                        
                    case 5:
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

    private static void increaseStock(Merchant merchant) {
        viewProducts(merchant);
        int id = getInt("Enter Product id :");
        Product p = DbHelper.getProduct(id,merchant);
        if(p == null){
            System.out.println("Invalid Product Id!");
            return;
        }
        int count = getInt("Enter no of items :");
        DbHelper.addStock(merchant,p,count);
        System.out.println("Stock Increased Successfully!");
    }

    private static void updatePrice(Merchant merchant) {
        viewProducts(merchant);
        int id = getInt("Enter Product id :");
        Product p = DbHelper.getProduct(id,merchant);
        if(p == null){
            System.out.println("Invalid Product Id!");
            return;
        }
        System.out.println(p);
        double price = getPrice("Enter Price :");
        DbHelper.updatePrice(id,price);
        System.out.println("Price Updated Successfully!");
    }

    private static void viewProducts(Merchant merchant) {
        HashMap<Product,Integer> map = DbHelper.viewProducts(merchant);
        System.out.println("Your Products :");
        if(map.size() == 0) System.out.println("No Products Registered");
        else map.forEach((product,i) -> {
            System.out.println();
            System.out.println(product);
            System.out.println("Available Stocks : " + i);
        });
    }

    private static void addProduct(Merchant merchant) {
        in.nextLine();
        String name = getString("Enter Product name : ");
        double price = getPrice("Enter price");
        String description = getString("Enter Description :");
        Product product = new Product(name, price, description);
        int count = getInt("Enter no of items :");
        merchant.addProduct(product,count);
        System.out.println("Product added!");
    } 

    private static void customerOptions() {
        while(true){
            try {
                System.out.println("------------------------------------");
                System.out.println("1. LOGIN AS CUSTOMER \n2. SIGN UP AS CUSTOMER \n3.EXIT");
                System.out.println("Enter your choice : ");
                int ch = in.nextInt();
                switch(ch){
                    case 1:
                        customerLogin();
                        break;
                    
                    case 2:
                        customerSignUp();
                        break;
                    
                    case 3:
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

    private static void customerSignUp() {
        in.nextLine();
        String email = getEmail();
        if(!DbHelper.isNewCustomerEmail(email)){
            System.out.println("Email already exists!");
            return;
        }
        String password = getPassword();
        String name = getString("Enter name : ");
        long phone = getMobileNumber();
        Address address = getAddress();
        in.nextLine();
        LocalDate date = getDate("Enter Date of birth(dd-mm-yyyy) :");
        Gender gender = getGender();
        Customer customer = new Customer(name, email, phone, gender, date, address);
        DbHelper.storeData(customer, password);
        System.out.println("Signed Up Successfully!");
        customerMenu(customer);
    }

    private static void customerMenu(Customer customer) {
        while(true){
            try {
                System.out.println("------------------------------------");
                System.out.println("1. Start Shopping \n2. Show Orders \n3.Log out");
                System.out.println("Enter your choice : ");
                int ch = in.nextInt();
                switch(ch){
                    case 1:
                        shopping(customer);
                        break;
                    
                    case 2:
                        showOrders(customer);
                        break;
                    
                    case 3:
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

    private static void showOrders(Customer customer) {
        ArrayList<Order> orders = DbHelper.getOrders(customer);
        System.out.println("\nYour Orders:");
        if(orders.isEmpty()){
            System.out.println("No orders available!\n");
            return;
        }
        for (Order order : orders) {
            System.out.println();
            Product product = order.getProduct();
            System.out.println("Name : " + product.getName());
            System.out.println("Price : " + product.getPrice());
            System.out.println("Description : " + product.getDescription());
            System.out.println("Count : " + order.getCount());
            System.out.println("Amount : " + order.getAmount());
            System.out.println("Date and Time : " + order.getDate() + " " + order.getTime());
        }
        System.out.println();
    }

    private static void shopping(Customer customer) {
        HashMap<Product,Integer> map = DbHelper.getAllProducts();
        showProducts(map);
        int id = getInt("Enter Product id :");
        Product product = getProduct(map,id);
        if(product == null){
            System.out.println("Invalid Product Id!");
            return;
        }
        int var = map.get(product);
        if(var == 0){
            System.out.println("No Stocks Available!");
            return;
        }
        int count = getInt("Enter no of items :");
        while (count > var){
            System.out.println("Stocks are less!");
            count = getInt("Enter no of items :");
        }
        DbHelper.storeData(new Order(product, customer.getEmail(), count, count*product.getPrice(), LocalDate.now(), LocalTime.now()));
        DbHelper.updateStock(id, count);
        System.out.println("Your order will be delivered shortly!");
    }

    private static Product getProduct(HashMap<Product, Integer> map, int id) {
        for (Product product : map.keySet()) {
            if(product.getId() == id) return product;
        }
        return null;
    }

    private static void showProducts(HashMap<Product,Integer> map) {
        System.out.println("\nAll Products :");
        if(map.size() == 0) System.out.println("No Products Registered");
        else map.forEach((product,i) -> {
            System.out.println();
            System.out.println(product);
            System.out.println("Available Stocks : " + i);
        });
        System.out.println();
    }

    private static void customerLogin() {
        in.nextLine();
        String email = getEmail();
        String password = getString("Enter Password :");
        Customer customer = DbHelper.getCustomer(email,password);
        if(customer == null){
            System.out.println("Invalid Credentials!");
            return;
        }
        System.out.println("Logged In Successfully!");
        customerMenu(customer);
    }
    
    private static int getInt(String string) {
        while(true){
            System.out.println(string);
            int i = in.nextInt();
            if(i>0) return i;
            else System.out.println("Invalid! Try Again");
        }
    }

    private static double getPrice(String string) {
        while(true){
            System.out.println("Enter Price:");
            double price = in.nextDouble();
            if(price >= 1)
                return price;
            else
                System.out.println("Invalid! Try Again");
        }
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

    private static Address getAddress() {
        String doorno = getString("Enter door no :");
        String street = getString("Enter Street name :");
        String city = getString("Enter city :");
        String district = getString("Enter district :");
        int pincode = getPincode();
        return new Address(doorno, street, city, district, pincode);
    }

    private static int getPincode() {
        while(true){
            System.out.println("Enter Pincode:");
            int pincode = in.nextInt();
            if((pincode+"").length() == 6)
                return pincode;
            else
                System.out.println("Invalid! Try Again");
        }
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

    private static LocalDate getDate(String string) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = null;
        while(date == null){
            System.out.println(string);
            String s = in.nextLine();
            try {
                date = LocalDate.from(dtf.parse(s));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid Format! Try Again!");
            }
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
                default : System.out.println("Invalid Input! Try Again!");
            }
        }
    }
}