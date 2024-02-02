import java.util.Date;

public class Student extends Person{

    private int id;
    private static int uid = 10001;

    public Student(String name, Date dob, Gender g, BloodGroup bGroup) {
        super(name, dob, g, bGroup);
        this.id = uid++;
    }

    public int getId() {
        return id;
    } 

    @Override
    public String toString() {
        return getName() + getDob();
    }

}