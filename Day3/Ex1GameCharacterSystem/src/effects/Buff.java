package effects;

public class Buff extends StatusEffect {
    public Buff(String name, int duration) {
        super(name, duration);
    }

    @Override
    public void applyEffect() {
        System.out.println("Buff applied: " + getName() + " for " + getDuration() + " turns.");
    }
}
