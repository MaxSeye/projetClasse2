package org.example;

public abstract class Weapon extends Item {
    protected int damage;
    protected int durability;

    public Weapon(String name, int damage) {
        super(name, calculateWeight(damage), damage * 10);
        this.damage = damage;
        this.durability = 100;
    }

    private static int calculateWeight(int damage) {
        // Formule simple: plus de dégâts = plus lourd
        return damage / 2 + 1;
    }

    public int calculateDamage() {
        // La formule peut être ajustée selon le type d'arme
        return damage;
    }

    public void reduceDurability() {
        durability -= 1;
        if (durability <= 0) {
            System.out.println(name + " s'est brisée!");
            // Logique pour gérer une arme brisée
        }
    }@Override
    public void use(Hero hero) {
        hero.setEquippedWeapon(this);
        System.out.println("Vous avez équipé: " + getName());
    }

    public int getDamage() { return damage; }
    public int getDurability() { return durability; }
}

class Sword extends Weapon {
    public Sword(String name, int damage) {
        super(name, damage);
    }

    @Override
    public int calculateDamage() {
        // Les épées ont un bonus de dégâts
        return (int)(damage * 1.2);
    }

    @Override
    public boolean isConsumable() {
        return false;
    }
}

class Staff extends Weapon {
    private int magicBonus;

    public Staff(String name, int damage, int magicBonus) {
        super(name, damage);
        this.magicBonus = magicBonus;
    }

    @Override
    public boolean isConsumable() {
        return false;
    }

    @Override
    public int calculateDamage() {
        // Les bâtons font moins de dégâts physiques mais ont un bonus magique
        return damage + magicBonus;
    }

    public int getMagicBonus() { return magicBonus; }
}
