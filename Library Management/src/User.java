import java.time.LocalDate;

public class User {

    private int id;
    private String name;
    private Gender gender;
    private String email;
    private LocalDate dob;
    private long phone;

    public User(String name, Gender gender, String email, LocalDate dob, long phone) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
    }

    public User(int id, String name, Gender gender, String email, LocalDate dob, long phone) {
        this(name, gender, email, dob, phone);
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Gender getGender() {
        return gender;
    }
    public String getEmail() {
        return email;
    }
    public LocalDate getDob() {
        return dob;
    }
    public long getPhone() {
        return phone;
    }

}
