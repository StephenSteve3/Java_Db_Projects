import java.time.LocalDate;
import java.time.LocalTime;

public class Order {
    private Product product;
    private String customerId;
    private int count;
    private double amount;
    private LocalDate date;
    private LocalTime time;

    public Order(Product product, String customerId, int count, double amount, LocalDate date, LocalTime time) {
        this.product = product;
        this.customerId = customerId;
        this.count = count;
        this.amount = amount;
        this.date = date;
        this.time = time;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Product getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
