package effects;

public class Debuff extends StatusEffect {
    public Debuff(String name, int duration) {
        super(name, duration);
    }

    @Override
    public void applyEffect() {
        System.out.println("Debuff applied: " + getName() + " for " + getDuration() + " turns.");
    }
}
