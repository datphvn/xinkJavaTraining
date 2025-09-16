package team;

import characters.GameCharacter;

import java.util.*;

public class Team {
    private String name;
    private List<GameCharacter> members;

    public Team(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }

    public void addMember(GameCharacter character) {
        members.add(character);
        System.out.println(character.getName() + " joined team " + name);
    }

    public List<GameCharacter> getMembers() { return members; }
}
