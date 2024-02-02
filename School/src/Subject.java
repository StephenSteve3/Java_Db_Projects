public class Subject {
    
    private int id;
    private static int uid = 101;
    private String name;

    public Subject(String name) {
        this.id = uid++;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}