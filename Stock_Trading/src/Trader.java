public class Trader {

    private int id;
    private String name;
    private Gender gender;
    private String email;
    private long aadhar;
    private long phone;
    private double amount;

    public Trader(String name, Gender gender, String email, long aadhar, long phone, double amount) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.aadhar = aadhar;
        this.phone = phone;
        this.amount = amount;
    }

    public Trader(int id, String name, Gender gender, String email, long aadhar, long phone, double amount) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.aadhar = aadhar;
        this.phone = phone;
        this.amount = amount;
    }

    public Gender getGender() {
        return gender;
    }

    public long getPhone() {
        return phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getAadhar() {
        return aadhar;
    }

    public double getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + " -> " + name + "\nGender : " + gender + "\nE-mail : " + email + "\nAadhar : " + aadhar +
            "\nPhone : " + phone + "\nAmount : " + amount;
    }

    public void deductAmount(double deductAmount) {
        amount -= deductAmount;
    }

    public void addAmount(double addAmount){
        amount += addAmount;
    }

}
