package characters;

import abilities.Heal;

public class Healer extends GameCharacter {
    public Healer(String name) {
        super(name);
        abilities.add(new Heal());
    }

    @Override
    public void performBasicAttack(GameCharacter target) {
        System.out.println(name + " smacks " + target.getName() + " with a staff!");
        target.takeDamage(5);
    }

    @Override
    public void useAbility(abilities.Ability ability, GameCharacter target) {
        ability.execute(this, target);
    }

    @Override
    public double calculateDamageReduction() {
        return 0.2;
    }

    @Override
    public void levelUp() {
        level++;
        maxHealth += 15;
        maxMana += 10;
        health = maxHealth;
        mana = maxMana;
    }

    @Override
    public void allocateStatPoint(enums.StatType stat) {
        baseStats.put(stat, baseStats.getOrDefault(stat, 0) + 1);
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) health = maxHealth;
        System.out.println(name + " heals for " + amount + ". Current HP: " + health + "/" + maxHealth);
    }

}
