import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper {

    private static final String url = "jdbc:postgresql://localhost:5432/library";
    private static final String username = "postgres";
    private static final String password = "1234";
    private static Connection con;
    private static PreparedStatement credentialCheckStatement;
    private static PreparedStatement setCredentialStatement;
    private static PreparedStatement bookInsertStatement;
    private static PreparedStatement userInsertStatement;
    private static PreparedStatement authorInsertStatement;
    private static PreparedStatement categoryInsertStatement;
    private static PreparedStatement publicationInsertStatement;
    private static PreparedStatement getAllPublicationsStatement;
    private static PreparedStatement getAllCategoriesStatement;
    private static PreparedStatement getAllAuthorsStatement;
    private static PreparedStatement selectAuthorStatement;
    private static PreparedStatement selectCategoryStatement;
    private static PreparedStatement selectPublicationStatement;
    private static PreparedStatement bookCopyInsertStatement;
    private static PreparedStatement bookCopyUpdateStatement;
    private static PreparedStatement getAllBooksStatement;
    private static PreparedStatement getBookByCopyIdStatement;
    private static PreparedStatement getUserIdStatement;
    private static PreparedStatement borrowInsertStatement;
    private static PreparedStatement borrowUpdateStatement;
    private static PreparedStatement userBorrowCountStatement;
    private static PreparedStatement getBorrowStatement;
    private static PreparedStatement getUserStatement;
    private static PreparedStatement getBorrowByUserStatement;
    private static PreparedStatement getBookStatement;

    static{
        try {
            con = DriverManager.getConnection(url,username,password);
            credentialCheckStatement = con.prepareStatement("SELECT * FROM CREDENTIAL WHERE USERNAME = ? AND PASSWORD = ?");
            setCredentialStatement = con.prepareStatement("UPDATE CREDENTIAL SET USERNAME = ?, PASSWORD = ?");
            bookInsertStatement = con.prepareStatement("INSERT INTO BOOK(NAME, AUTHOR, CATEGORY, PUBLICATION, PRICE) VALUES(?,?,?,?,?) RETURNING ID");
            userInsertStatement = con.prepareStatement("INSERT INTO USERS(NAME,GENDER,EMAIL,DOB,PHONE,PASSWORD) VALUES(?,?,?,?,?,?)");
            authorInsertStatement = con.prepareStatement("INSERT INTO AUTHOR(NAME) VALUES(?)");
            categoryInsertStatement = con.prepareStatement("INSERT INTO CATEGORY(NAME) VALUES(?)");
            publicationInsertStatement = con.prepareStatement("INSERT INTO PUBLICATION(NAME) VALUES(?)");
            getAllPublicationsStatement = con.prepareStatement("SELECT * FROM PUBLICATION");
            getAllCategoriesStatement = con.prepareStatement("SELECT * FROM CATEGORY");
            getAllAuthorsStatement = con.prepareStatement("SELECT * FROM AUTHOR");
            getAllBooksStatement = con.prepareStatement("SELECT B.ID,B.NAME,A.NAME AS AUTHOR,C.NAME AS CATEGORY,P.NAME AS PUBLICATION,B.PRICE,(SELECT COUNT(*) FROM BOOKCOPY WHERE BOOK=B.ID) FROM BOOK B JOIN AUTHOR A ON B.AUTHOR=A.ID JOIN CATEGORY C ON B.CATEGORY=C.ID JOIN PUBLICATION P ON B.PUBLICATION=P.ID");
            selectAuthorStatement = con.prepareStatement("SELECT ID FROM AUTHOR WHERE NAME = ?");
            selectCategoryStatement = con.prepareStatement("SELECT ID FROM CATEGORY WHERE NAME = ?");
            selectPublicationStatement = con.prepareStatement("SELECT ID FROM PUBLICATION WHERE NAME = ?");
            bookCopyInsertStatement = con.prepareStatement("INSERT INTO BOOKCOPY(BOOK) VALUES(?)");
            getUserIdStatement = con.prepareStatement("SELECT ID FROM USERS WHERE EMAIL = ? AND PASSWORD = ?");
            bookCopyUpdateStatement = con.prepareStatement("UPDATE BOOKCOPY SET IS_AVAILABLE = ? WHERE ID = ?");
            userBorrowCountStatement = con.prepareStatement("SELECT COUNT(*) FROM BORROWS WHERE USERS = ? AND RETURN IS NULL");
            borrowInsertStatement = con.prepareStatement("INSERT INTO BORROWS(USERS, BOOKCOPY, DATE) VALUES(?,?,?)");
            borrowUpdateStatement = con.prepareStatement("UPDATE BORROWS SET RETURN = ? WHERE BOOKCOPY = ? AND RETURN IS NULL");
            getBorrowStatement = con.prepareStatement("SELECT * FROM BORROWS WHERE RETURN IS NULL");
            getBorrowByUserStatement = con.prepareStatement("SELECT * FROM BORROWS WHERE USERS = ?");
            getUserStatement = con.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
            getBookByCopyIdStatement = con.prepareStatement("SELECT B.ID,B.NAME,A.NAME AS AUTHOR,C.NAME AS CATEGORY,P.NAME AS PUBLICATION,B.PRICE FROM BOOK B JOIN BOOKCOPY BC ON BC.BOOK=B.ID JOIN AUTHOR A ON B.AUTHOR=A.ID JOIN CATEGORY C ON B.CATEGORY=C.ID JOIN PUBLICATION P ON B.PUBLICATION=P.ID WHERE BC.ID = ? AND IS_AVAILABLE = ?");
            getBookStatement = con.prepareStatement("SELECT B.ID,B.NAME,A.NAME AS AUTHOR,C.NAME AS CATEGORY,P.NAME AS PUBLICATION,B.PRICE FROM BOOK B JOIN BOOKCOPY BC ON BC.BOOK=B.ID JOIN AUTHOR A ON B.AUTHOR=A.ID JOIN CATEGORY C ON B.CATEGORY=C.ID JOIN PUBLICATION P ON B.PUBLICATION=P.ID WHERE BC.ID = ?");
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

    public static boolean checkCredentails(String username, String password) {
        try {
            credentialCheckStatement.setString(1, username);
            credentialCheckStatement.setString(2, password);
            ResultSet rs = credentialCheckStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public static void setCredentials(String username, String password) {
        try {
            setCredentialStatement.setString(1, username);
            setCredentialStatement.setString(2, password);
            setCredentialStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void storeData(Book book, int count) {
        try {
            bookInsertStatement.setString(1, book.getName());
            bookInsertStatement.setInt(2, getAuthorId(book.getAuthor()));
            bookInsertStatement.setInt(3, getCategoryId(book.getCategory()));
            bookInsertStatement.setInt(4, getPublicationId(book.getPublication()));
            bookInsertStatement.setDouble(5, book.getPrice());
            bookInsertStatement.execute();
            ResultSet resultSet = bookInsertStatement.getResultSet();
            if(resultSet.next()){
                int id = resultSet.getInt(1);
                populateCopies(id,count);
            }        
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static boolean populateCopies(int id, int count) {
        try {
            bookCopyInsertStatement.setInt(1, id);
            for(int i=0 ; i<count ; i++){
                bookCopyInsertStatement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private static int getPublicationId(String publication) {
        try {
            selectPublicationStatement.setString(1, publication);
            ResultSet rs = selectPublicationStatement.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return -1;
    }

    private static int getCategoryId(String category) {
        try {
            selectCategoryStatement.setString(1, category);
            ResultSet rs = selectCategoryStatement.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return -1;
    }

    private static int getAuthorId(String author) {
        try {
            selectAuthorStatement.setString(1, author);
            ResultSet rs = selectAuthorStatement.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return -1;
    }

    public static ArrayList<String> getPublications() {
        ArrayList<String> arr = new ArrayList<>();
        try {
            ResultSet rs = getAllPublicationsStatement.executeQuery();
            while(rs.next()){
                arr.add(rs.getString("name"));
            }  
        } catch (SQLException e) {
            System.out.println(e);
        }
        return arr;
    }

    public static boolean storePublication(String publication) {
        try {
            publicationInsertStatement.setString(1, publication);
            publicationInsertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static ArrayList<String> getCategories() {
        ArrayList<String> arr = new ArrayList<>();
        try {
            ResultSet rs = getAllCategoriesStatement.executeQuery();
            while(rs.next()){
                arr.add(rs.getString("name"));
            }  
        } catch (SQLException e) {
            System.out.println(e);
        }
        return arr;
    }

    public static boolean storeCategory(String category) {
        try {
            categoryInsertStatement.setString(1, category);
            categoryInsertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static ArrayList<String> getAuthors() {
        ArrayList<String> arr = new ArrayList<>();
        try {
            ResultSet rs = getAllAuthorsStatement.executeQuery();
            while(rs.next()){
                arr.add(rs.getString("name"));
            }  
        } catch (SQLException e) {
            System.out.println(e);
        }
        return arr;
    }

    public static HashMap<Book,Integer> getAllBooks() {
        HashMap<Book,Integer> map = new HashMap<>();
        try {
            ResultSet rs = getAllBooksStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id"); 
                String name = rs.getString("name"); 
                String author = rs.getString("author");
                String category = rs.getString("category");
                double price = rs.getDouble("price"); 
                String publication = rs.getString("publication");
                int count = rs.getInt("count");
                map.put(new Book(id, name, author, category, price, publication), count);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return map;
    }

    public static boolean storeAuthor(String author) {
        try {
            authorInsertStatement.setString(1, author);
            authorInsertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean storeData(User user, String password) {
        try {
            userInsertStatement.setString(1, user.getName());
            userInsertStatement.setString(2, user.getGender().toString());
            userInsertStatement.setString(3, user.getEmail());
            userInsertStatement.setDate(4, Date.valueOf(user.getDob()));
            userInsertStatement.setLong(5, user.getPhone());
            userInsertStatement.setString(6, password);
            userInsertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static Book getBookByCopyId(int id, boolean isAvailable) {
        try {
            getBookByCopyIdStatement.setInt(1, id);
            getBookByCopyIdStatement.setBoolean(2, isAvailable);
            ResultSet rs = getBookByCopyIdStatement.executeQuery();
            if(rs.next()){
                int bookId = rs.getInt("id");
                String name = rs.getString("name"); 
                String author = rs.getString("author");
                String category = rs.getString("category");
                double price = rs.getDouble("price"); 
                String publication = rs.getString("publication");
                return new Book(bookId, name, author, category, price, publication);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public static int getUserId(String email, String password) {
        try {
            getUserIdStatement.setString(1, email);
            getUserIdStatement.setString(2, password);
            ResultSet rs = getUserIdStatement.executeQuery();
            if(rs.next()){
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return -1;
    }

    private static void updateBookCopy(int id, boolean isAvailable){
        try {
            bookCopyUpdateStatement.setBoolean(1, isAvailable);
            bookCopyUpdateStatement.setInt(2, id);
            bookCopyUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void addBorrow(Borrow borrow) {
        try {
            borrowInsertStatement.setInt(1, borrow.getUserId());
            borrowInsertStatement.setInt(2, borrow.getBookCopyId());
            borrowInsertStatement.setDate(3, Date.valueOf(borrow.getDate()));
            borrowInsertStatement.executeUpdate();
            updateBookCopy(borrow.getBookCopyId(), false);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static int borrowedBooks(int userId) {
        try {
            userBorrowCountStatement.setInt(1, userId);
            ResultSet rs = userBorrowCountStatement.executeQuery();
            if(rs.next()){
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public static void returnBook(int id) {
        try {
            borrowUpdateStatement.setDate(1, Date.valueOf(LocalDate.now()));
            borrowUpdateStatement.setInt(2, id);
            borrowUpdateStatement.executeUpdate();
            updateBookCopy(id, true);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ArrayList<Borrow> getBorrows() {
        ArrayList<Borrow> borrowList = new ArrayList<>();
        try {
            ResultSet rs = getBorrowStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                int userId = rs.getInt("users"); 
                int bookCopyId = rs.getInt("bookcopy"); 
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalDate returnDate = null;
                Date date1 = rs.getDate("return");
                if(date1 != null){
                    returnDate = date1.toLocalDate();
                }
                borrowList.add(new Borrow(id, userId, bookCopyId, date, returnDate));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return borrowList;
    }

    public static Book getBookByCopyId(int bookCopyId) {
        try {
            getBookStatement.setInt(1, bookCopyId);
            ResultSet rs = getBookStatement.executeQuery();
            if(rs.next()){
                int bookId = rs.getInt("id");
                String name = rs.getString("name"); 
                String author = rs.getString("author");
                String category = rs.getString("category");
                double price = rs.getDouble("price"); 
                String publication = rs.getString("publication");
                return new Book(bookId, name, author, category, price, publication);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public static User getUser(int userId) {
        try{
            getUserStatement.setInt(1, userId);
            ResultSet rs = getUserStatement.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name"); 
                Gender gender = Gender.getGender(rs.getString("gender").charAt(0));
                String email = rs.getString("email"); 
                LocalDate dob = rs.getDate("dob").toLocalDate(); 
                long phone = rs.getLong("phone");
                return new User(id, name, gender, email, dob, phone);
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }

    public static ArrayList<Borrow> getBorrows(User user) {
        ArrayList<Borrow> borrowList = new ArrayList<>();
        try {
            getBorrowByUserStatement.setInt(1,user.getId());
            ResultSet rs = getBorrowByUserStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                int userId = rs.getInt("users"); 
                int bookCopyId = rs.getInt("bookcopy"); 
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalDate returnDate = null;
                Date date1 = rs.getDate("return");
                if(date1 != null){
                    returnDate = date1.toLocalDate();
                }
                borrowList.add(new Borrow(id, userId, bookCopyId, date, returnDate));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return borrowList;
    }

}