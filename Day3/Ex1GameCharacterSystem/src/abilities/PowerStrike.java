package abilities;

import characters.GameCharacter;

public class PowerStrike extends Ability {
    private int damage;

    public PowerStrike() {
        super("Power Strike", 5, 1);
        this.damage = 20;
    }

    @Override
    public void execute(GameCharacter caster, GameCharacter target) {
        System.out.println(caster.getName() + " performs a mighty Power Strike on " + target.getName());
        target.takeDamage(damage);
    }
}
