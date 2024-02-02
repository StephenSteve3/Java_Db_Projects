import java.time.LocalDate;

public class Borrow {
    
    private int id;
    private int userId;
    private int bookCopyId;
    private LocalDate date;
    private LocalDate returnDate;

    public Borrow(int userId, int bookCopyId) {
        this.userId = userId;
        this.bookCopyId = bookCopyId;
        this.date = LocalDate.now();
    }

    public Borrow(int id, int userId, int bookCopyId, LocalDate date, LocalDate returnDate) {
        this.id = id;
        this.userId = userId;
        this.bookCopyId = bookCopyId;
        this.date = date;
        this.returnDate = returnDate;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookCopyId() {
        return bookCopyId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }
}
