import java.util.Date;

public class Teacher extends Person{
    
    private static int uid=101;
    private int id;
    private Degree degree;

    public Teacher(String name, Date dob, Gender g, BloodGroup bGroup, Degree degree) {
        super(name, dob, g, bGroup);
        this.id = uid++;
        this.degree = degree;
    }
    
    public int getId() {
        return id;
    }

    public Degree getDegree() {
        return degree;
    }

    @Override
    public String toString() {
        return getName() + "  " + degree;
    }
    
}