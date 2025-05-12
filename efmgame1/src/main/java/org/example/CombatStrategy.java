package org.example;

public interface CombatStrategy {
void attack(Character attacker, Character target);
}

class PhysicalAttackStrategy implements CombatStrategy {
    @Override
    public void attack(Character attacker, Character target) {
        Weapon weapon = attacker.getEquippedWeapon();
        if (weapon != null) {
            int damage = weapon.calculateDamage();
            target.takeDamage(damage);
            System.out.println(attacker.getName() + " inflige " + damage + " dégâts à " + target.getName() +
                    " avec " + weapon.getName());

            // Réduire la durabilité de l'arme
            weapon.reduceDurability();
        } else {
            // Attaque à mains nues
            int damage = 2;  // Dégâts de base sans arme
            target.takeDamage(damage);
            System.out.println(attacker.getName() + " inflige " + damage + " dégâts à " + target.getName() +
                    " à mains nues");
        }
    }
}

class MagicAttackStrategy implements CombatStrategy {
    @Override
    public void attack(Character attacker, Character target) {
        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            MagicPotion potion = hero.findMagicPotion();

            if (potion != null) {
                int damage = potion.calculateEffect();
                target.takeDamage(damage);
                System.out.println(hero.getNickname() + " utilise " + potion.getName() +
                        " et inflige " + damage + " dégâts magiques à " + target.getName());
                hero.consumeItem(potion);
            } else {
                // Revenir à une attaque physique si pas de potion
                new PhysicalAttackStrategy().attack(attacker, target);
            }
        } else {
            // Pour les personnages non-héros qui utilisent cette stratégie
            int damage = 5;  // Dégâts magiques de base
            target.takeDamage(damage);
            System.out.println(attacker.getName() + " lance un sort et inflige " +
                    damage + " dégâts magiques à " + target.getName());
        }
    }
}
