package org.example;

import java.util.ArrayList;
import java.util.List;

public interface CombatObserver {
    void notifyCombatStarted(Hero hero, Enemy enemy);
    void notifyCombatEnded(Hero hero, Enemy enemy, boolean heroWon);
}

class ContactNotifier implements CombatObserver {
    @Override
    public void notifyCombatStarted(Hero hero, Enemy enemy) {
        for (Contact contact : hero.getContacts()) {
            Notification notification = new Notification(
                    "Combat commencé",
                    hero.getNickname() + " affronte " + enemy.getName(),
                    "HP: " + hero.getHp() + "/" + hero.getMaxHp(),
                    "Localisation: " + hero.getPosition().getName()
            );
            contact.sendNotification(notification);
        }
    }

    @Override
    public void notifyCombatEnded(Hero hero, Enemy enemy, boolean heroWon) {
        String result = heroWon ? "victoire" : "défaite";
        for (Contact contact : hero.getContacts()) {
            Notification notification = new Notification(
                    "Combat terminé",
                    hero.getNickname() + " - " + result + " contre " + enemy.getName(),
                    "HP restants: " + hero.getHp() + "/" + hero.getMaxHp(),
                    "Localisation: " + hero.getPosition().getName()
            );
            contact.sendNotification(notification);
        }
    }
}

// Système de combat
class CombatSystem {
    private List<CombatObserver> observers;

    public CombatSystem() {
        observers = new ArrayList<>();
        observers.add(new ContactNotifier());
    }

    public void addObserver(CombatObserver observer) {
        observers.add(observer);
    }

    public void startCombat(Hero hero, Enemy enemy) {
        System.out.println("Combat entre " + hero.getNickname() + " et " + enemy.getName() + " commencé!");

        // Notifier les observateurs
        for (CombatObserver observer : observers) {
            observer.notifyCombatStarted(hero, enemy);
        }

        // Déterminer la stratégie de combat
        CombatStrategy strategy;
        if (enemy instanceof GhostKnight) {
            strategy = new MagicAttackStrategy();
        } else {
            strategy = new PhysicalAttackStrategy();
        }
        int turn = 1;
        while (hero.isAlive() && enemy.isAlive()) {
            System.out.println("--- Tour " + turn + " ---");

            System.out.println(hero.getNickname() + " attaque!");
            strategy.attack(hero, enemy);

            if (!enemy.isAlive()) {
                System.out.println(enemy.getName() + " a été vaincu!");
                break;
            }

            System.out.println(enemy.getName() + " attaque!");
            enemy.attack(hero);

            turn++;
        }
        boolean heroWon = enemy.isAlive() ? false : true;

        for (CombatObserver observer : observers) {
            observer.notifyCombatEnded(hero, enemy, heroWon);
        }

        if (heroWon) {
            System.out.println(hero.getNickname() + " a gagné le combat!");
            hero.gainExperience(enemy.getExpValue());

            for (Item item : enemy.getDropItems()) {
                hero.addToInventory(item);
            }
        } else {
            System.out.println(hero.getNickname() + " a perdu le combat!");
        }
    }
}

