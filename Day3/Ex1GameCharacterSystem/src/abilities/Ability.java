package abilities;

import characters.GameCharacter;

public class Ability {
    private String name;
    private int manaCost;
    private int cooldown;

    public Ability(String name, int manaCost, int cooldown) {
        this.name = name;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
    }

    public String getName() { return name; }

    public void execute(GameCharacter caster, GameCharacter target) {
        System.out.println(caster.getName() + " casts " + name + " on " + target.getName());
    }
}
