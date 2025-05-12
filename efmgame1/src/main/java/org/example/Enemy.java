package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends Character {
    protected int strength;
    protected List<Item> dropItems;
    protected int expValue;

    public Enemy(String name, int maxHp, int strength, int expValue) {
        super(name, maxHp);
        this.strength = strength;
        this.dropItems = new ArrayList<>();
        this.expValue = expValue;
    }

    @Override
    public void attack(Character target) {
        int damage = calculateDamage();
        target.takeDamage(damage);
        System.out.println(name + " attaque " + target.getName() + " pour " + damage + " dégâts!");
    }

    protected int calculateDamage() {
        // Formule simple pour les dégâts
        return strength;
    }

    public void addDropItem(Item item) {
        dropItems.add(item);
    }

    public List<Item> getDropItems() {
        return new ArrayList<>(dropItems);
    }

    public int getExpValue() { return expValue; }
}


