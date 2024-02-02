public class Section {

    private int id;
    private static int uid = 1;
    private char name;
    private static char uname = 'A';
    
    private Section() {
        this.name = uname++;
        id = uid++;
    }

    public static Section generateSection(){
        return new Section();
    }

    public char getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name+"";
    }

}