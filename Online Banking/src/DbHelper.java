import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {

    private static String url;
    private static String username;
    private static String password;
    private static Connection con;
    private static PreparedStatement customerInsertStatement;
    private static PreparedStatement accountInsertStatement;
    private static PreparedStatement accountUpdateStatement;
    private static PreparedStatement transactionUpdateStatement;
    private static PreparedStatement transactionSelectStatement;
    
    static{
        url = "jdbc:postgresql://localhost:5432/bank";
        username = "postgres";
        password = "1234";
        try{
            con = DriverManager.getConnection(url, username, password);
            customerInsertStatement = con.prepareStatement("INSERT INTO CUSTOMER VALUES(?,?,?,?,?)");
            accountInsertStatement = con.prepareStatement("INSERT INTO ACCOUNT VALUES(?,?,?,?,?)");
            accountUpdateStatement = con.prepareStatement("UPDATE ACCOUNT SET AMOUNT = ? WHERE NUMBER = ?");
            transactionUpdateStatement = con.prepareStatement("INSERT INTO TRANSACTION(ID,ACCOUNT_NUMBER,AMOUNT,TYPE,DATE,TIME) VALUES(?,?,?,?,?,?)");
            transactionSelectStatement = con.prepareStatement("SELECT * FROM TRANSACTION WHERE ACCOUNT_NUMBER = ?");
        } catch(SQLException e){
            System.out.println(e);
        }
    }

    public static void storeData(Customer c){
        try {
            customerInsertStatement.setString(1, c.getName());
            customerInsertStatement.setString(2, c.getGender().toString());
            customerInsertStatement.setLong(3, c.getMobile());
            customerInsertStatement.setDate(4, c.getDob());
            customerInsertStatement.setString(5, c.getPanNumber());
            customerInsertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void storeData(Account a, Customer c, String password){
        try {
            accountInsertStatement.setLong(1, a.getAccountNumber());
            accountInsertStatement.setString(2, a.getType().toString());
            accountInsertStatement.setString(3, password);
            accountInsertStatement.setInt(4, a.getAmount());
            accountInsertStatement.setString(5, c.getPanNumber());
            accountInsertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void updateData(Account a) {
        try {
            accountUpdateStatement.setInt(1, a.getAmount());
            accountUpdateStatement.setLong(2, a.getAccountNumber());
            accountUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void storeData(Transaction t) {
        try {
            transactionUpdateStatement.setInt(1, t.getId());
            transactionUpdateStatement.setLong(2, t.getAccountNumber());
            transactionUpdateStatement.setInt(3, t.getAmount());
            transactionUpdateStatement.setString(4, t.getType().toString());
            transactionUpdateStatement.setDate(5, t.getDate());
            transactionUpdateStatement.setTime(6, t.getTime());
            transactionUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void closeResource(){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Transaction> getTransactions(Account account) {
        try {
            transactionSelectStatement.setLong(1, account.getAccountNumber());
            ResultSet resultSet = transactionSelectStatement.executeQuery();
            List<Transaction> transactions = new ArrayList<>();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                long accountNumber = resultSet.getLong("account_number");
                int amount = resultSet.getInt("amount");
                TransactionType type = TransactionType.getTransactionType(resultSet.getString("type").charAt(0));
                LocalDate date = resultSet.getDate("date").toLocalDate();
                LocalTime time = resultSet.getTime("time").toLocalTime();
                transactions.add(new Transaction(id, accountNumber, amount, type, date, time));
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private DbHelper(){}

}