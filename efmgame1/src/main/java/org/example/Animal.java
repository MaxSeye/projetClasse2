package org.example;

public abstract class Animal {
    protected String name;
    protected int hp;
    protected int maxHp;

    public Animal(String name, int maxHp) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public abstract String getBehavior();

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            System.out.println(name + " a été vaincu!");
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }

    // Getters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
}

class DomesticAnimal extends Animal {
    public DomesticAnimal(String name, int maxHp) {
        super(name, maxHp);
    }

    @Override
    public String getBehavior() {
        return "Docile et amical";
    }
}

class WildAnimal extends Animal {
    public WildAnimal(String name, int maxHp) {
        super(name, maxHp);
    }

    @Override
    public String getBehavior() {
        return "Sauvage et agressif";
    }

    public void attack(Character target) {
        int damage = (int)(maxHp * 0.2); // Les animaux sauvages causent des dégâts proportionnels à leurs HP max
        target.takeDamage(damage);
        System.out.println(name + " attaque " + target.getName() + " pour " + damage + " dégâts!");
    }
}