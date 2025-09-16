package equipment;

public class Armor {
    private String name;
    private int defense;
    private double blockChance;

    public Armor(String name, int defense, double blockChance) {
        this.name = name;
        this.defense = defense;
        this.blockChance = blockChance;
    }

    public String getName() { return name; }
    public int getDefense() { return defense; }
    public double getBlockChance() { return blockChance; }

    @Override
    public String toString() {
        return "Armor{name='" + name + "', defense=" + defense +
                ", blockChance=" + blockChance + "}";
    }
}
