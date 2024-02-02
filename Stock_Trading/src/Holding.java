public class Holding {
    
    private int id;
    private Trader trader;
    private Stock stock;
    private double price;
    private int quantity;

    public Holding(Trader trader, Stock stock, double price, int quantity) {
        this.trader = trader;
        this.stock = stock;
        this.price = price;
        this.quantity = quantity;
    }

    public Holding(int id, Trader trader, Stock stock, double price, int quantity) {
        this.id = id;
        this.trader = trader;
        this.stock = stock;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public Trader getTrader() {
        return trader;
    }

    public Stock getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return id + "-> " + stock.getName() + "\nBought Price : " + price + "\nCurrent Price : " + stock.getPrice() + "\nQuantity : " + quantity;
    }

}
