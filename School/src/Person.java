import java.util.Date;

public abstract class Person {
    
    private String name;
    private Date dob;
    private Gender gender;
    private BloodGroup bloodGroup;
    
    public Person(String name, Date dob, Gender g, BloodGroup bGroup) {
        this.name = name;
        this.dob = dob;
        this.gender = g;
        this.bloodGroup = bGroup;
    }
    
    public String getName() {
        return name;
    }

    public java.sql.Date getDob() {
        return new java.sql.Date(dob.getTime());
    }

    public Gender getGender() {
        return gender;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }
    
}