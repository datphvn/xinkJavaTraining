package abilities;

import characters.GameCharacter;

public class Heal extends Ability {
    private int healAmount;

    public Heal() {
        super("Heal", 8, 3);
        this.healAmount = 25;
    }

    @Override
    public void execute(GameCharacter caster, GameCharacter target) {
        System.out.println(caster.getName() + " casts Heal on " + target.getName());
        target.takeDamage(-healAmount); // dùng takeDamage(-x) để hồi máu
    }
}
