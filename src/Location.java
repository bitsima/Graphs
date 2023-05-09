public class Location {
    public String name;
    public int id;

    public Location(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
