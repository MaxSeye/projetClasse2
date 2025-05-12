package org.example;

public abstract class Potion extends Item {
    protected int potency;

    public Potion(String name, int potency) {
        super(name, 1, potency * 5);
        this.potency = potency;
    }

    public abstract void applyEffect(Character character);

    public int calculateEffect() {
        return potency;
    }
    @Override
    public boolean isConsumable() {
        return true;
    }
}

class HealthPotion extends Potion {
    public HealthPotion(String name, int potency) {
        super(name, potency);
    }

    @Override
    public void applyEffect(Character character) {
        int healAmount = calculateEffect();
        int newHp = Math.min(character.getHp() + healAmount, character.getMaxHp());
        character.setHp(newHp);
        System.out.println(character.getName() + " a récupéré " + (newHp - character.getHp()) + " points de vie!");
    }

    @Override
    public void use(Hero hero) {
        applyEffect(hero);
    }
}

class MagicPotion extends Potion {
    public MagicPotion(String name, int potency) {
        super(name, potency);
    }

    @Override
    public void applyEffect(Character character) {
        // Pour un système plus complexe avec de la magie
        System.out.println(character.getName() + " utilise une potion magique!");
    }

    @Override
    public int calculateEffect() {
        // Les potions magiques sont très efficaces contre les fantômes
        return potency * 3;
    }


    @Override
    public void use(Hero hero) {
        applyEffect(hero);
    }
}