package characters;

import abilities.Ability;
import enums.StatType;

public class Mage extends GameCharacter {
    public Mage(String name) {
        super(name);
        baseStats.put(StatType.STRENGTH, 2);
        baseStats.put(StatType.INTELLIGENCE, 10);
    }

    @Override
    public void performBasicAttack(GameCharacter target) {
        int damage = baseStats.get(StatType.INTELLIGENCE) * 3;
        System.out.println(name + " casts a firebolt at " + target.getName() + " for " + damage + " damage!");
        target.takeDamage(damage);
    }

    @Override
    public void useAbility(Ability ability, GameCharacter target) {
        System.out.println(name + " uses " + ability.getName() + "!");
        ability.execute(this, target);
    }

    @Override
    public double calculateDamageReduction() {
        return 0.05;
    }

    @Override
    public void levelUp() {
        level++;
        maxMana += 20;
        mana = maxMana;
        System.out.println(name + " leveled up to " + level + "!");
    }

    @Override
    public void allocateStatPoint(StatType stat) {
        baseStats.put(stat, baseStats.getOrDefault(stat, 0) + 1);
    }
}
