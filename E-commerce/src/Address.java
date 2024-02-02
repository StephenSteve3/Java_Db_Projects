class Address{
	private String doorNo;
	private String street;
	private String city;
	private String district;
	private int pincode;
	
	public Address(String doorNo, String street, String city, String district, int pincode){
		this.doorNo = doorNo;
		this.street = street;
		this.city = city;
		this.district = district;
		this.pincode = pincode;
	}

    public String getDoorNo() {
        return doorNo;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public int getPincode() {
        return pincode;
    }
}