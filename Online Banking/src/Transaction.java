import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {

    private int id;
    private long accountNumber;
    private int amount;
    private TransactionType type;
    private LocalDate date;
    private LocalTime time;

    public Transaction(int id, long accountNumber, int amount, TransactionType type, LocalDate date, LocalTime time) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.type = type;
        this.time = time;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public int getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public Date getDate(){
        return Date.valueOf(date);
    }

    public Time getTime(){
        return Time.valueOf(time);
    }

}