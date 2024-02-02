import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper {

    private static final String url = "jdbc:postgresql://localhost:5432/e_commerce";
    private static final String username = "postgres";
    private static final String password = "1234";
    private static Connection con;
    private static PreparedStatement findMerchantEmailStatement;
    private static PreparedStatement findCustomerEmailStatement;
    private static PreparedStatement merchantInsertStatement;
    private static PreparedStatement addressInsertStatement;
    private static PreparedStatement findAddressStatement;
    private static PreparedStatement customerInsertStatement;
    private static PreparedStatement merchantSelectStatement;
    private static PreparedStatement addressSelectStatement;
    private static PreparedStatement customerSelectStatement;
    private static PreparedStatement productInsertStatement;
    private static PreparedStatement productSelectStatement;
    private static PreparedStatement stockInsertStatement;
    private static PreparedStatement getProductStatement;
    private static PreparedStatement getProductByIdStatement;
    private static PreparedStatement productUpdateStatement;
    private static PreparedStatement selectProductsStatement;
    private static PreparedStatement productCountStatement;
    private static PreparedStatement allProductSelectStatement;
    private static PreparedStatement ordersInsertStatement;
    private static PreparedStatement stockUpdateStatement;
    private static PreparedStatement getStockStatement;
    private static PreparedStatement getOrderStatement;

    static{
        try {
            con = DriverManager.getConnection(url,username,password);
            findMerchantEmailStatement = con.prepareStatement("SELECT * FROM MERCHANT WHERE EMAIL = ?");
            findCustomerEmailStatement = con.prepareStatement("SELECT * FROM CUSTOMER WHERE EMAIL = ?");
            merchantInsertStatement = con.prepareStatement("INSERT INTO MERCHANT (NAME, EMAIL, PHONE, ADDRESS, PASSWORD) VALUES (?,?,?,?,?)");
            addressInsertStatement = con.prepareStatement("INSERT INTO ADDRESS (DOORNO, STREET, CITY, DISTRICT, PINCODE) VALUES (?,?,?,?,?)");
            findAddressStatement = con.prepareStatement("SELECT ID FROM ADDRESS WHERE DOORNO = ? AND STREET = ? AND CITY = ?");
            customerInsertStatement = con.prepareStatement("INSERT INTO CUSTOMER (NAME, EMAIL, PHONE, ADDRESS, PASSWORD, DOB, GENDER) VALUES (?,?,?,?,?,?,?)");
            merchantSelectStatement = con.prepareStatement("SELECT * FROM MERCHANT WHERE EMAIL = ? AND PASSWORD = ?");
            addressSelectStatement = con.prepareStatement("SELECT * FROM ADDRESS WHERE ID = ?");
            customerSelectStatement = con.prepareStatement("SELECT * FROM CUSTOMER WHERE EMAIL = ? AND PASSWORD = ?");
            productInsertStatement = con.prepareStatement("INSERT INTO PRODUCT (MERCHANT, NAME, PRICE, DESCRIPTION) VALUES(?,?,?,?)");
            productSelectStatement = con.prepareStatement("SELECT ID FROM PRODUCT WHERE MERCHANT = ? AND NAME = ? AND PRICE = ?");
            stockInsertStatement = con.prepareStatement("INSERT INTO STOCK(PRODUCT) VALUES (?)");
            getProductStatement = con.prepareStatement("SELECT * FROM PRODUCT WHERE MERCHANT = ? AND ID = ?");
            getProductByIdStatement = con.prepareStatement("SELECT * FROM PRODUCT WHERE ID = ?");
            productUpdateStatement = con.prepareStatement("UPDATE PRODUCT SET PRICE = ? WHERE ID = ?");
            selectProductsStatement = con.prepareStatement("SELECT * FROM PRODUCT WHERE MERCHANT = ?");
            productCountStatement = con.prepareStatement("SELECT COUNT(*) FROM STOCK WHERE PRODUCT = ? AND IS_SOLD = FALSE");
            getStockStatement = con.prepareStatement("SELECT ID FROM STOCK WHERE PRODUCT = ? AND IS_SOLD = FALSE");
            allProductSelectStatement = con.prepareStatement("SELECT * FROM PRODUCT");
            ordersInsertStatement = con.prepareStatement("INSERT INTO ORDERS(PRODUCT,CUSTOMER,COUNT,AMOUNT,DATE,TIME) VALUES (?,?,?,?,?,?)");
            stockUpdateStatement = con.prepareStatement("UPDATE STOCK SET IS_SOLD = TRUE WHERE ID = ?");
            getOrderStatement = con.prepareStatement("SELECT * FROM ORDERS WHERE CUSTOMER = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DbHelper(){}

    public static void closeResource() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isNewMerchantEmail(String email) {
        try {
            findMerchantEmailStatement.setString(1, email);
            ResultSet resultSet = findMerchantEmailStatement.executeQuery();
            if(resultSet.next()) return false;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean isNewCustomerEmail(String email) {
        try {
            findCustomerEmailStatement.setString(1, email);
            ResultSet resultSet = findCustomerEmailStatement.executeQuery();
            if(resultSet.next()) return false;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void storeData(Merchant merchant, String password) {
        try {
            merchantInsertStatement.setString(1, merchant.getName());
            merchantInsertStatement.setString(2, merchant.getEmail());
            merchantInsertStatement.setLong(3, merchant.getPhone());
            merchantInsertStatement.setInt(4, storeData(merchant.getAddress()));
            merchantInsertStatement.setString(5, password);
            merchantInsertStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static int storeData(Address address) {
        try {
            addressInsertStatement.setString(1, address.getDoorNo());
            addressInsertStatement.setString(2, address.getStreet());
            addressInsertStatement.setString(3, address.getCity());
            addressInsertStatement.setString(4, address.getDistrict());
            addressInsertStatement.setInt(5, address.getPincode());
            addressInsertStatement.executeUpdate();
            findAddressStatement.setString(1, address.getDoorNo());
            findAddressStatement.setString(2, address.getStreet());
            findAddressStatement.setString(3, address.getCity());
            ResultSet rs = findAddressStatement.executeQuery();
            if(rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static Merchant getMerchant(String email, String password) {
        try {
            merchantSelectStatement.setString(1, email);
            merchantSelectStatement.setString(2, password);
            ResultSet rs = merchantSelectStatement.executeQuery();
            if(rs.next()){
                String name = rs.getString("name");
                long phone = rs.getLong("phone");
                Address address = getAddress(rs.getInt("address"));
                return new Merchant(name, phone, email, address);
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }

    private static Address getAddress(int i) {
        try {
            addressSelectStatement.setInt(1, i);
            ResultSet rs = addressSelectStatement.executeQuery();
            if(rs.next()){
                String doorNo = rs.getString("doorno");
                String street = rs.getString("street");
                String city = rs.getString("city");
                String district = rs.getString("district");
                int pincode = rs.getInt("pincode");
                return new Address(doorNo, street, city, district, pincode);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static void storeData(Merchant merchant, Product product, int count) {
        try {
            productInsertStatement.setString(1, merchant.getEmail());
            productInsertStatement.setString(2, product.getName());
            productInsertStatement.setDouble(3, product.getPrice());
            productInsertStatement.setString(4, product.getDescription());
            productInsertStatement.executeUpdate();
            addStock(merchant, product, count);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private static int getId(Product product, Merchant merchant) {
        try {
            productSelectStatement.setString(1, merchant.getEmail());
            productSelectStatement.setString(2, product.getName());
            productSelectStatement.setDouble(3, product.getPrice());
            ResultSet rs = productSelectStatement.executeQuery();
            if(rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public static Product getProduct(int id, Merchant merchant) {
        try {
            getProductStatement.setString(1, merchant.getEmail());
            getProductStatement.setInt(2, id);
            ResultSet rs = getProductStatement.executeQuery();
            if(rs.next()){
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                return new Product(id, name, price, description);
            } 
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public static void updatePrice(int id, double price) {
        try {
            productUpdateStatement.setDouble(1, price);
            productUpdateStatement.setInt(2, id);
            productUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static Customer getCustomer(String email, String password) {
        try {
            customerSelectStatement.setString(1, email);
            customerSelectStatement.setString(2, password);
            ResultSet rs = customerSelectStatement.executeQuery();
            if(rs.next()){
                String name = rs.getString("name");
                long phone = rs.getLong("phone");
                Address address = getAddress(rs.getInt("address"));
                LocalDate dob = rs.getDate("dob").toLocalDate();
                Gender gender = Gender.getGender(rs.getString("gender").charAt(0));
                return new Customer(name, email, phone, gender, dob, address);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static void storeData(Customer customer, String password) {
        try {
            customerInsertStatement.setString(1, customer.getName());
            customerInsertStatement.setString(2, customer.getEmail());
            customerInsertStatement.setLong(3, customer.getPhone());
            customerInsertStatement.setInt(4, storeData(customer.getAddress()));
            customerInsertStatement.setString(5, password);
            customerInsertStatement.setDate(6, Date.valueOf(customer.getDob()));
            customerInsertStatement.setString(7, customer.getGender().toString());
            customerInsertStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void addStock(Merchant merchant, Product product, int count) {    
        try {
            int id = getId(product,merchant);
            stockInsertStatement.setInt(1, id);
            for(int i=0 ; i<count ; i++){
                stockInsertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static HashMap<Product, Integer> viewProducts(Merchant merchant) {
        HashMap<Product, Integer> map = new HashMap<>();
        try {
            selectProductsStatement.setString(1, merchant.getEmail());
            ResultSet rs = selectProductsStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                Product product = new Product(id, name, price, description);
                int count = getCount(id);
                map.put(product, count);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return map;
    }

    private static int getCount(int id) {
        try {
            productCountStatement.setInt(1, id);
            ResultSet rs = productCountStatement.executeQuery();
            if(rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public static HashMap<Product, Integer> getAllProducts() {
        HashMap<Product, Integer> map = new HashMap<>();
        try {
            ResultSet rs = allProductSelectStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                Product product = new Product(id, name, price, description);
                int count = getCount(id);
                map.put(product, count);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return map;
    }

    public static void storeData(Order order) {
        try {
            ordersInsertStatement.setInt(1, order.getProduct().getId());
            ordersInsertStatement.setString(2, order.getCustomerId());
            ordersInsertStatement.setInt(3, order.getCount());
            ordersInsertStatement.setDouble(4, order.getAmount());
            ordersInsertStatement.setDate(5, Date.valueOf(order.getDate()));
            ordersInsertStatement.setTime(6, Time.valueOf(order.getTime()));
            ordersInsertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void updateStock(int id, int count) {
        try {
            getStockStatement.setInt(1, id);
            ResultSet rs = getStockStatement.executeQuery();
            while(rs.next() && count>0){
                int stockId = rs.getInt("id");
                stockUpdateStatement.setInt(1, stockId); 
                stockUpdateStatement.executeUpdate();
                count--;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ArrayList<Order> getOrders(Customer customer) {
        ArrayList<Order> orders = new ArrayList<>();
        try{
            getOrderStatement.setString(1, customer.getEmail());
            ResultSet rs = getOrderStatement.executeQuery();
            while(rs.next()){
                Product product =getProduct(rs.getInt("product"));
                int count = rs.getInt("count");
                double amount = rs.getDouble("amount");
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime time = rs.getTime("time").toLocalTime();
                orders.add(new Order(product, customer.getEmail(), count, amount, date, time));
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return orders;
    }

    private static Product getProduct(int id) {
        try {
            getProductByIdStatement.setInt(1, id);
            ResultSet rs = getProductByIdStatement.executeQuery();
            while(rs.next()){
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                return new Product(name, price, description);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    
}