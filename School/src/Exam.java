import java.time.Month;
import java.time.Year;

public class Exam {

    private int id;
    private static int uid = 1001;
    private String name;
    private Month month;
    private Year year;

    public Exam(String name, Month month, Year year) {
        this.id = uid++;
        this.month = month;
        this.name = name;
        this.year = year;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Year getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    @Override
    public String toString() {
        return name +" - " + month + " " + year;
    }
    
}