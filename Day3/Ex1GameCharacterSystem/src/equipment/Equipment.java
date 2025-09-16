package equipment;

public class Equipment {
    private String name;
    private String type;

    public Equipment(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }
    public String getType() { return type; }
}
