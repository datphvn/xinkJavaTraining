

import characters.*;
import team.Team;

public class Main {
    public static void main(String[] args) {
        // Create characters
        Warrior warrior = new Warrior("Thor");
        Mage mage = new Mage("Merlin");
        Archer archer = new Archer("Robin");
        Healer healer = new Healer("Angela");

        // Create team
        Team heroes = new Team("Avengers");
        heroes.addMember(warrior);
        heroes.addMember(mage);
        heroes.addMember(archer);
        heroes.addMember(healer);

        // Simple combat demo
        warrior.performBasicAttack(mage);
        mage.performBasicAttack(warrior);
        healer.heal(warrior.getName().length() * 5); // test heal
    }
}
