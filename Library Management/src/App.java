import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class App {

    static Scanner in = new Scanner(System.in);
    public static void main(String[] args) {
        if(!checkCredentails()){
            System.out.println("Invalid Credentials!");
            DbHelper.closeResource();
            return;
        }
        System.out.println("Logged In Successfully!");
        while(true){
            try {
                System.out.println("------------------------------------------");
                System.out.println("1.Alter Credentials");
                System.out.println("2.Add Book");
                System.out.println("3.Increase no of Copies");
                System.out.println("4.Add User");
                System.out.println("5.Issue Book");
                System.out.println("6.Return Book");
                System.out.println("7.Show all Books");
                System.out.println("8.User Borrow History");
                System.out.println("9.Show Borrowed Books");
                System.out.println("10.Exit");
                System.out.println("Enter your choice :");
                int ch = in.nextInt();
                System.out.println("------------------------------------------");
                switch(ch){
                    case 1:
                        alterCredentials();
                        break;

                    case 2:
                        addBook();
                        break;
                    
                    case 3:
                        increaseCopies();
                        break;
                        
                    case 4:
                        addUser();
                        break;
                        
                    case 5:
                        issueBook();
                        break;

                    case 6:
                        returnBook();
                        break;
                        
                    case 7:
                        showAllBooks();
                        break;

                    case 8:
                        userBorrowHistory();
                        break;

                    case 9:
                        showBorrowedBooks();
                        break;

                    case 10:
                        DbHelper.closeResource();
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

    private static void userBorrowHistory() {
        int id = getInt("Enter User Id : ");
        User user = DbHelper.getUser(id);
        if(user == null){
            System.out.println("Invalid User Id !");
            return;
        }
        System.out.println("Name : " + user.getName());
        System.out.println("E-mail : " + user.getEmail());
        System.out.println();
        ArrayList<Borrow> borrowList = DbHelper.getBorrows(user);
        System.out.println("Book Copy | Book id | Book name | Borrow date | Return Date ");
        for (Borrow borrow : borrowList) {
            Book book = DbHelper.getBookByCopyId(borrow.getBookCopyId());
            System.out.println(borrow.getBookCopyId() + " | " + book.getId() + " | " + book.getName() + " | " 
            + borrow.getDate() + " | " + borrow.getReturnDate());
        }
        if(borrowList.isEmpty()){
            System.out.println("No Borrows Available!");
        }
    }

    private static void returnBook() {
        int id = getInt("Enter book copy id  to return :");
        Book book = DbHelper.getBookByCopyId(id,false);
        if(book == null){
            System.out.println("Invalid id!");
            return;
        }
        System.out.println(book + "\n");
        System.out.println("Do you want to continue(1 for yes/any number for no) :");
        if(in.nextInt() != 1){
            return;
        }
        DbHelper.returnBook(id);
    }

    private static void showBorrowedBooks() {
        ArrayList<Borrow> borrowList = DbHelper.getBorrows();
        System.out.println("Book Copy | Book id | Book name | User id | User name | Borrow date | Return Date ");
        for (Borrow borrow : borrowList) {
            Book book = DbHelper.getBookByCopyId(borrow.getBookCopyId(),false);
            User user = DbHelper.getUser(borrow.getUserId());
            System.out.println(borrow.getBookCopyId() + " | " + book.getId() + " | " + book.getName() + " | " 
            + user.getId() + " | " + user.getName() + " | " + borrow.getDate() + " | " + borrow.getReturnDate());
        }
    }

    private static void issueBook(){
        int id = getInt("Enter book copy id :");
        Book book = DbHelper.getBookByCopyId(id,true);
        if(book == null){
            System.out.println("Invalid id!");
            return;
        }
        System.out.println(book + "\n");
        in.nextLine();
        String email = getEmail();
        String password = getString("Enter password:");
        int userId = DbHelper.getUserId(email,password);
        if(userId == -1){
            System.out.println("Invalid email or password!");
            return;
        }
        if(DbHelper.borrowedBooks(userId) >= 5){
            System.out.println("No of Books Exceeded!");
            return;
        }
        Borrow borrow = new Borrow(userId, id);
        DbHelper.addBorrow(borrow);
        System.out.println("Book Issued!");
    }

    private static void increaseCopies() {
        showAllBooks();
        int id = getInt("Enter Book ID :");
        int count = getInt("Enter the no of copies to increase :");
        if(DbHelper.populateCopies(id, count)){
            System.out.println("Copies Increased!");
        }
        else{
            System.out.println("Invalid Book Id");
        }
    }

    private static void showAllBooks() {
        HashMap<Book, Integer> allBooks = DbHelper.getAllBooks();
        System.out.println("ALL BOOKS :\n");
        show(allBooks);
    }

    private static void show(HashMap<Book, Integer> books) {
        if(books.size() == 0){
            System.out.println("No Books Available!");
            return;
        }
        books.forEach((book,count) -> {
            System.out.println(book);
            System.out.println("No of Copies : " + count);
            System.out.println();
        });
    }

    private static void addUser() {
        in.nextLine();
        String name = getString("Enter name :");
        String email = getEmail();
        String password = getPassword();
        LocalDate dob = getDate("Enter DOB :");
        Gender gender = getGender();
        long phone = getMobileNumber();
        User user = new User(name, gender, email, dob, phone);
        if(DbHelper.storeData(user,password))
            System.out.println("User Added Successfully!");
        else
            System.out.println("Email already used!");
    }

    private static void addBook() {
        in.nextLine();
        String name = getString("Enter Book name :");
        String author = getAuthor();
        String category = getCategory();
        String publication = getPublication();
        double price = getPrice();
        int count = getInt("Enter the no of copies :");
        Book book = new Book(name, author, category, price, publication);
        DbHelper.storeData(book, count);
        System.out.println("Book Added Succesfully!");
    }

    private static String getPublication() {
        ArrayList<String> publications = DbHelper.getPublications();
        System.out.println("All Publications:");
        System.out.println("0 -> Add new Publication");
        for(int i=0 ; i<publications.size() ; i++){
            System.out.println( (i+1) + " -> " + publications.get(i));
        }
        while(true){
            System.out.println("Enter choice : ");
            int ch = in.nextInt();
            if(ch == 0){
                return createPublication();
            }
            try {
                return publications.get(ch-1);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid Choice!");
            }
        }
    }

    private static String createPublication() {
        in.nextLine();
        String publication = getString("Enter Publication name :").toUpperCase();
        if(!DbHelper.storePublication(publication)) {
            System.out.println("Publication already available not added!");
        }
        return publication;
    }

    private static String getCategory() {
        ArrayList<String> categories = DbHelper.getCategories();
        System.out.println("All Categories:");
        System.out.println("0 -> Add new Category");
        for(int i=0 ; i<categories.size() ; i++){
            System.out.println( (i+1) + " -> " + categories.get(i));
        }
        while(true){
            System.out.println("Enter choice : ");
            int ch = in.nextInt();
            if(ch == 0){
                return createCategory();
            }
            try {
                return categories.get(ch-1);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid Choice!");
            }
        }
    }

    private static String createCategory() {
        in.nextLine();
        String category = getString("Enter Category name :").toUpperCase();
        if(!DbHelper.storeCategory(category)) {
            System.out.println("Category already available not added!");
        }
        return category;
    }

    private static String getAuthor() {
        ArrayList<String> authors = DbHelper.getAuthors();
        System.out.println("All Authors:");
        System.out.println("0 -> Add new Author");
        for(int i=0 ; i<authors.size() ; i++){
            System.out.println( (i+1) + " -> " + authors.get(i));
        }
        while(true){
            System.out.println("Enter choice : ");
            int ch = in.nextInt();
            if(ch == 0){
                return createAuthor();
            }
            try {
                return authors.get(ch-1);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid Choice!");
            }
        }
    }

    private static String createAuthor() {
        in.nextLine();
        String author = getString("Enter Author name :").toUpperCase();
        if(!DbHelper.storeAuthor(author)) {
            System.out.println("Author already available not added!");
        }
        return author;
    }

    private static void alterCredentials() {
        in.nextLine();
        if(!checkCredentails()){
            System.out.println("Invalid Credentials!");
            return;
        }
        String username = getString("Enter new username :");
        String password = getPassword();
        DbHelper.setCredentials(username,password);
        System.out.println("Username and Password Updated!");
    }

    private static boolean checkCredentails() {
        String username = getString("Enter username :");
        String password = getString("Enter password :");
        return DbHelper.checkCredentails(username, password);
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

    private static int getInt(String string) {
        while(true){
            System.out.println(string);
            int i = in.nextInt();
            if(i>0) return i;
            else System.out.println("Invalid! Try Again");
        }
    }

    private static String getString(String string) {
        System.out.println(string);
        string = "";
        while(true){
            string = in.nextLine().trim();
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
    
    private static double getPrice() {
        while(true){
            System.out.println("Enter Price:");
            double price = in.nextDouble();
            if(price >= 1)
                return price;
            else
                System.out.println("Invalid! Try Again");
        }
    }
}