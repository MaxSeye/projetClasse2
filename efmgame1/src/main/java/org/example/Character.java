package org.example;

public abstract class Character {
    protected String name;
    protected int hp;
    protected int maxHp;
    protected Location position;
    protected Weapon equippedWeapon;

    public Character(String name, int maxHp) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public void attack(Character target) {
        if (equippedWeapon != null) {
            int damage = equippedWeapon.calculateDamage();
            target.takeDamage(damage);
        }
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }

    // Getters et setters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getMaxHp() { return maxHp; }
    public Location getPosition() { return position; }
    public void setPosition(Location position) { this.position = position; }
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public void setEquippedWeapon(Weapon weapon) { this.equippedWeapon = weapon; }
}
