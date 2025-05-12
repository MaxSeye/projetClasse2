package org.example;

public abstract class Knight extends Enemy {
    public Knight(String name, int maxHp, int strength, int expValue) {
        super(name, maxHp, strength, expValue);
    }
}

class ShovelKnight extends Knight {
    public ShovelKnight() {
        super("Chevalier à la pelle", 80, 15, 50);
        addDropItem(new Sword("Pelle de combat", 12));
    }

    @Override
    protected int calculateDamage() {
        // Les chevaliers à la pelle ont une attaque spéciale
        return (int)(strength * 1.3);
    }
}

class GhostKnight extends Knight {
    public GhostKnight() {
        super("Chevalier fantôme", 60, 20, 70);
        addDropItem(new MagicPotion("Essence spectrale", 15));
    }

    @Override
    public void takeDamage(int damage) {
        // Les chevaliers fantômes sont résistants aux attaques physiques
        super.takeDamage(damage / 2);
    }
}