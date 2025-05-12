package org.example;

public abstract class Armor extends Item {
    protected int protection;
    protected int durability;

    public Armor(String name, int protection) {
        super(name, protection * 2, protection * 15);
        this.protection = protection;
        this.durability = 100;
    }

    public int getProtection() { return protection; }

    public void reduceDurability() {
        durability -= 1;
        if (durability <= 0) {
            System.out.println(name + " est très endommagée et n'offre plus de protection!");
        }
    }

}

class BronzeArmor extends Armor {
    public BronzeArmor() {
        super("Armure de Bronze", 10);
    }

    @Override
    public boolean isConsumable() {
        return false;
    }

    @Override
    public void use(Hero hero) {
        hero.setEquippedArmor(this);
        System.out.println("Vous avez équipé: " + getName());
    }
}

class SilverArmor extends Armor {

    public SilverArmor(String name, int protection) {
        super(name, protection);
    }

    @Override
    public boolean isConsumable() {
        return false;
    }

    @Override
    public void use(Hero hero) {
        hero.setEquippedArmor(this);
        System.out.println("Vous avez équipé: " + getName());
    }
}

class GoldArmor extends Armor {
    public GoldArmor() {
        super("Armure d'Or", 20);
    }

    @Override
    public boolean isConsumable() {
        return false;
    }

    @Override
    public void use(Hero hero) {
        hero.setEquippedArmor(this);
        System.out.println("Vous avez équipé: " + getName());
    }
}