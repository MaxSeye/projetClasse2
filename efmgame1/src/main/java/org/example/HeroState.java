package org.example;

public interface HeroState {
    void handleDamage(Hero hero, int damage);
    String getStateDescription();
}
class HealthyState implements HeroState {
    @Override
    public void handleDamage(Hero hero, int damage) {
        hero.setHp(hero.getHp() - damage);
        if (hero.getHp() < hero.getMaxHp() * 0.7) {
            hero.setState(new WoundedState());
        }
    }

    @Override
    public String getStateDescription() {
        return "En pleine forme";
    }
}

class WoundedState implements HeroState {
    @Override
    public void handleDamage(Hero hero, int damage) {
        // Les dégâts sont légèrement réduits quand on est blessé
        int actualDamage = (int)(damage * 0.9);
        hero.setHp(hero.getHp() - actualDamage);
        if (hero.getHp() < hero.getMaxHp() * 0.3) {
            hero.setState(new WeakenedState());
        }
    }

    @Override
    public String getStateDescription() {
        return "Blessé";
    }
}

class WeakenedState implements HeroState {
    @Override
    public void handleDamage(Hero hero, int damage) {
        // Les dégâts sont plus importants quand on est affaibli
        int actualDamage = (int)(damage * 1.2);
        hero.setHp(hero.getHp() - actualDamage);
        if (hero.getHp() <= 0) {
            // Logique pour la défaite du héros
            System.out.println(hero.getName() + " a été vaincu!");
        }
    }

    @Override
    public String getStateDescription() {
        return "Affaibli";
    }
}
