package equipment;

public class Weapon {
    private String name;
    private int attackPower;
    private double critChance;

    public Weapon(String name, int attackPower, double critChance) {
        this.name = name;
        this.attackPower = attackPower;
        this.critChance = critChance;
    }

    public String getName() { return name; }
    public int getAttackPower() { return attackPower; }
    public double getCritChance() { return critChance; }

    @Override
    public String toString() {
        return "Weapon{name='" + name + "', attackPower=" + attackPower +
                ", critChance=" + critChance + "}";
    }
}
