import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DbHelper {

    private static final String url = "jdbc:postgresql://localhost:5432/stock_trading";
    private static final String username = "postgres";
    private static final String password = "1234";
    private static Connection con;
    private static PreparedStatement stockUpdateStatement;
    private static PreparedStatement holdingInsertStatement;
    private static PreparedStatement traderUpdateStatement;
    private static PreparedStatement holdingDeleteStatement;
    private static PreparedStatement getStockStatement;
    private static PreparedStatement getHoldingsStatement;
    private static PreparedStatement getHoldingStatement;

    static{
        try {
            con = DriverManager.getConnection(url,username,password);
            stockUpdateStatement = con.prepareStatement("UPDATE STOCK SET PRICE = ? WHERE ID = ?");
            holdingInsertStatement = con.prepareStatement("INSERT INTO HOLDING(TRADER,STOCK,PRICE,QUANTITY) VALUES(?,?,?,?)");
            traderUpdateStatement = con.prepareStatement("UPDATE TRADER SET AMOUNT = ? WHERE ID = ?");
            holdingDeleteStatement = con.prepareStatement("DELETE FROM HOLDING WHERE ID = ?");
            getStockStatement = con.prepareStatement("SELECT * FROM STOCK WHERE ID = ?");
            getHoldingsStatement = con.prepareStatement("SELECT * FROM HOLDING WHERE TRADER = ?");
            getHoldingStatement = con.prepareStatement("SELECT * FROM HOLDING WHERE TRADER = ? AND ID = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DbHelper(){}

    public static void closeResource() {
        try {
            stockUpdateStatement.close();
            holdingDeleteStatement.close();
            holdingInsertStatement.close();
            traderUpdateStatement.close();
            getStockStatement.close();
            getHoldingsStatement.close();
            getHoldingStatement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateStockValue(Stock stock) {
        try {
            stockUpdateStatement.setDouble(1, stock.getPrice());
            stockUpdateStatement.setInt(2, stock.getId());
            stockUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ArrayList<Stock> getStocks() {
        ArrayList<Stock> stocks = new ArrayList<>();
        try(ResultSet rs = con.prepareStatement("SELECT * FROM STOCK").executeQuery()) {
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                stocks.add(new Stock(id, name, price));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return stocks;
    }

    public static Trader getTrader(String email, String password) {
        try (PreparedStatement getTraderStatement = con.prepareStatement("SELECT * FROM TRADER WHERE EMAIL = ? AND PASSWORD = ?")) {
            getTraderStatement.setString(1, email);
            getTraderStatement.setString(2, password);
            ResultSet rs = getTraderStatement.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Gender gender = Gender.getGender(rs.getString("gender").charAt(0));
                long aadhar = rs.getLong("aadhar");
                long phone = rs.getLong("phone");
                double amount = rs.getDouble("amount");
                rs.close();
                return new Trader(id, name, gender, email, aadhar, phone, amount);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static boolean isNewEmail(String email) {
        try(PreparedStatement checkStatement = con.prepareStatement("SELECT ID FROM TRADER WHERE EMAIL = ?")){
            checkStatement.setString(1, email);
            ResultSet rs = checkStatement.executeQuery();
            return !rs.next();
        }catch(SQLException e){
            System.out.println(e);
        }
        return false;
    }

    public static int storeData(Trader trader, String password) {
        try(PreparedStatement traderInsertStatement = con.prepareStatement("INSERT INTO TRADER (NAME,EMAIL,GENDER,AADHAR,PHONE,AMOUNT,PASSWORD) VALUES (?,?,?,?,?,?,?) RETURNING ID")){
            traderInsertStatement.setString(1, trader.getName());
            traderInsertStatement.setString(2, trader.getEmail());
            traderInsertStatement.setString(3, trader.getGender().toString());
            traderInsertStatement.setLong(4, trader.getAadhar());
            traderInsertStatement.setLong(5, trader.getPhone());
            traderInsertStatement.setDouble(6, trader.getAmount());
            traderInsertStatement.setString(7, password);
            traderInsertStatement.execute();
            ResultSet rs = traderInsertStatement.getResultSet();
            rs.next();
            return rs.getInt("id");
        }catch(SQLException e){
            return -1;
        }
    }

    public static Stock getStock(int id) {
        try {
            getStockStatement.setInt(1, id);
            ResultSet rs = getStockStatement.executeQuery();
            if(!rs.next()) return null;
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            return new Stock(id, name, price);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public static void storeData(Holding holding) {
        try {
            holdingInsertStatement.setInt(1,holding.getTrader().getId());
            holdingInsertStatement.setInt(2,holding.getStock().getId());
            holdingInsertStatement.setDouble(3,holding.getPrice());
            holdingInsertStatement.setInt(4,holding.getQuantity());
            holdingInsertStatement.executeUpdate();
            traderUpdateStatement.setDouble(1,holding.getTrader().getAmount());
            traderUpdateStatement.setInt(2, holding.getTrader().getId());
            traderUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ArrayList<Holding> getHoldings(Trader trader) {
        ArrayList<Holding> holdings = new ArrayList<>();
        try {
            getHoldingsStatement.setInt(1, trader.getId());
            ResultSet rs = getHoldingsStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                Stock stock = getStock(rs.getInt("stock"));
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                holdings.add(new Holding(id, trader, stock, price, quantity));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return holdings;
    }

    public static Holding getHolding(int id, Trader trader) {
        try {
            getHoldingStatement.setInt(1, trader.getId());
            getHoldingStatement.setInt(2, id);
            ResultSet rs = getHoldingStatement.executeQuery();
            if(rs.next()){
                Stock stock = getStock(rs.getInt("stock"));
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                return new Holding(id, trader, stock, price, quantity);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public static void removeHolding(Holding holding) {
        try {
            holdingDeleteStatement.setInt(1,holding.getId());
            holdingDeleteStatement.executeUpdate();
            traderUpdateStatement.setDouble(1,holding.getTrader().getAmount());
            traderUpdateStatement.setInt(2, holding.getTrader().getId());
            traderUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
