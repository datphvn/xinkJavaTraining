package characters;

import abilities.Ability;
import enums.StatType;

public class Archer extends GameCharacter {
    public Archer(String name) {
        super(name);
        baseStats.put(StatType.STRENGTH, 6);
        baseStats.put(StatType.INTELLIGENCE, 4);
    }

    @Override
    public void performBasicAttack(GameCharacter target) {
        int damage = baseStats.get(StatType.STRENGTH) * 2 + 5;
        System.out.println(name + " shoots an arrow at " + target.getName() + " for " + damage + " damage!");
        target.takeDamage(damage);
    }

    @Override
    public void useAbility(Ability ability, GameCharacter target) {
        System.out.println(name + " uses " + ability.getName() + "!");
        ability.execute(this, target);
    }

    @Override
    public double calculateDamageReduction() {
        return 0.1;
    }

    @Override
    public void levelUp() {
        level++;
        maxHealth += 10;
        health = maxHealth;
        System.out.println(name + " leveled up to " + level + "!");
    }

    @Override
    public void allocateStatPoint(StatType stat) {
        baseStats.put(stat, baseStats.getOrDefault(stat, 0) + 1);
    }
}
