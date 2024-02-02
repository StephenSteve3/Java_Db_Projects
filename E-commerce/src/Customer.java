import java.time.LocalDate;

public class Customer {

    private String name;
    private String email;
    private long phone;
    private Gender gender;
    private LocalDate dob;
    private Address address;

    public Customer(String name, String email, long phone, Gender gender, LocalDate dob, Address address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public long getPhone() {
        return phone;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

}
