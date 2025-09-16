package effects;

public class StatusEffect {
    private String name;
    private int duration;

    public StatusEffect(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public void applyEffect() {
        System.out.println("Applying " + name + " for " + duration + " turns.");
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }
}
