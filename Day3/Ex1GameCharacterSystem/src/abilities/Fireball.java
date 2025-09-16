package abilities;

import characters.GameCharacter;

public class Fireball extends Ability {
    private int damage;

    public Fireball() {
        super("Fireball", 10, 2);
        this.damage = 30;
    }

    @Override
    public void execute(GameCharacter caster, GameCharacter target) {
        System.out.println(caster.getName() + " hurls a Fireball at " + target.getName());
        target.takeDamage(damage);
    }
}
