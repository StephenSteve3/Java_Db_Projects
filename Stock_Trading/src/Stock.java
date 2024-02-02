public class Stock {

    private int id;
    private String name;
    private double price;

    public Stock(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void updatePrice(double price) {
        this.price += price;
    }

    @Override
    public String toString() {
        return id + " -> " + name + "   -   Price : " + price;
    }
    
}
