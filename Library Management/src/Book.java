public class Book {
    
    private int id;
    private String name;
    private String author;
    private String category;
    private double price;
    private String publication;

    public Book(String name, String author, String category, double price, String publication) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.price = price;
        this.publication = publication;
    }

    public Book(int id, String name, String author, String category, double price, String publication) {
        this(name, author, category, price, publication);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getPublication() {
        return publication;
    }

    @Override
    public String toString() {
        return id + " -> " + name + "\nAuthor : " + author + "\nCategory : " + category + "\nPublication : " + publication + "\nPrice : " + price;
    }

}
