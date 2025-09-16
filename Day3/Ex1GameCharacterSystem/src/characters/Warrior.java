package characters;

import abilities.Ability;
import enums.StatType;

public class Warrior extends GameCharacter {
    public Warrior(String name) {
        super(name);
        baseStats.put(StatType.STRENGTH, 10);
        baseStats.put(StatType.INTELLIGENCE, 2);
    }

    @Override
    public void performBasicAttack(GameCharacter target) {
        int damage = baseStats.get(StatType.STRENGTH) * 2;
        System.out.println(name + " slashes " + target.getName() + " for " + damage + " damage!");
        target.takeDamage(damage);
    }

    @Override
    public void useAbility(Ability ability, GameCharacter target) {
        System.out.println(name + " uses " + ability.getName() + " on " + target.getName());
        ability.execute(this, target);
    }

    @Override
    public double calculateDamageReduction() {
        return 0.2;
    }

    @Override
    public void levelUp() {
        level++;
        maxHealth += 20;
        health = maxHealth;
        System.out.println(name + " leveled up to " + level + "!");
    }

    @Override
    public void allocateStatPoint(StatType stat) {
        baseStats.put(stat, baseStats.getOrDefault(stat, 0) + 1);
    }
}
