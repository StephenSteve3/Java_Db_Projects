public class Merchant {

    private String name;
    private String email;
    private long phone;
    private Address address;

    public Merchant(String name, long phone, String email, Address address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public long getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void addProduct(Product product, int count) {
        DbHelper.storeData(this, product, count);
    }

}
