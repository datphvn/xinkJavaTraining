package characters;

import abilities.Ability;
import enums.StatType;
import effects.StatusEffect;
import equipment.Weapon;
import equipment.Armor;

import java.util.*;

public abstract class GameCharacter {
    protected String name;
    protected int level;
    protected int health;
    protected int maxHealth;
    protected int mana;
    protected int maxMana;
    protected Map<StatType, Integer> baseStats;
    public List<Ability> abilities;
    protected List<StatusEffect> activeEffects;
    protected Weapon weapon;
    protected Armor armor;

    public GameCharacter(String name) {
        this.name = name;
        this.level = 1;
        this.maxHealth = 100;
        this.health = maxHealth;
        this.maxMana = 50;
        this.mana = maxMana;
        this.baseStats = new HashMap<>();
        this.abilities = new ArrayList<>();
        this.activeEffects = new ArrayList<>();
    }

    public String getName() { return name; }
    public boolean isAlive() { return health > 0; }

    public void takeDamage(int amount) {
        health -= amount;
        if (health > maxHealth) health = maxHealth;
        if (health < 0) health = 0;
        System.out.println(name + " now has " + health + "/" + maxHealth + " HP");
    }

    public void equipWeapon(Weapon weapon) {
        this.weapon = weapon;
        System.out.println(name + " equipped weapon: " + weapon.getName());
    }

    public void equipArmor(Armor armor) {
        this.armor = armor;
        System.out.println(name + " equipped armor: " + armor.getName());
    }

    public int getAttackPower() {
        int baseAttack = baseStats.getOrDefault(StatType.STRENGTH, 10);
        int weaponBonus = (weapon != null) ? weapon.getAttackPower() : 0;
        return baseAttack + weaponBonus;
    }

    public int getDefense() {
        int baseDefense = baseStats.getOrDefault(StatType.DEFENSE, 5);
        int armorBonus = (armor != null) ? armor.getDefense() : 0;
        return baseDefense + armorBonus;
    }

    public abstract void performBasicAttack(GameCharacter target);
    public abstract void useAbility(Ability ability, GameCharacter target);
    public abstract double calculateDamageReduction();
    public abstract void levelUp();
    public abstract void allocateStatPoint(StatType stat);
}
