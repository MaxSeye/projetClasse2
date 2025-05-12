package org.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hero extends Character {
    private String nickname;
    private HeroState state;
    private List<Item> inventory;
    private List<Contact> contacts;
    private int experience;
    private int level;
    private Armor armor;
    private String gold;
    private Weapon weapon;

    public Hero(String nickname, int maxHp) {
        super(nickname, maxHp);
        this.nickname = nickname;
        this.state = new HealthyState();
        this.inventory = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.experience = 0;
        this.level = 1;
    }

    @Override
    public void takeDamage(int damage) {
        state.handleDamage(this, damage);
        notifyContactsIfInCombat();
    }

    public void addToInventory(Item item) {
        inventory.add(item);
        System.out.println(nickname + " a obtenu: " + item.getName());
    }

    public void removeFromInventory(Item item) {
        inventory.remove(item);
    }

    public void consumeItem(Item item) {
        if (item instanceof Potion) {
            Potion potion = (Potion) item;
            potion.applyEffect(this);
            removeFromInventory(item);
        }
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public MagicPotion findMagicPotion() {
        for (Item item : inventory) {
            if (item instanceof MagicPotion) {
                return (MagicPotion) item;
            }
        }
        return null;
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int expNeeded = level * 100; // Formule simple pour l'XP requise
        if (experience >= expNeeded) {
            level++;
            maxHp += 10;
            hp = maxHp; // Récupération complète lors d'un passage de niveau
            state = new HealthyState(); // Retour à l'état "En pleine forme"
            System.out.println(nickname + " est passé au niveau " + level + "!");
        }
    }

    private void notifyContactsIfInCombat() {
        // Vérification si le héros est en combat
        // Cette méthode serait appelée par le système de combat
        for (Contact contact : contacts) {
            Notification notification = new Notification(
                    "Combat en cours",
                    nickname + " est en combat",
                    "HP: " + hp + "/" + maxHp,
                    "Localisation: " + position.getName()
            );
            contact.sendNotification(notification);
        }
    }

    // Getters et setters spécifiques
    public String getNickname() { return nickname; }
    public HeroState getState() { return state; }
    public void setState(HeroState state) { this.state = state; }
    public List<Item> getInventory() { return inventory; }
    public List<Contact> getContacts() { return contacts; }

    public Armor getEquippedArmor() {
        return armor;
    }

    public String getGold() {
        return gold;
    }

    public void setEquippedArmor(Armor armor1) {
        armor=armor1;
    }

    public String getLevel() {
        return String.valueOf(level);
    }

    public String getExperience() {
        return String.valueOf(experience);
    }

    public String getExperienceToNextLevel() {
        return String.valueOf(level*100);
    }

    public String getCurrentHealth() {
        return String.valueOf(hp);
    }

    public String getMaxHealth() {
        return String.valueOf(maxHp);
    }

    public String getAttackPower() {
        return String.valueOf(weapon.damage);
    }

    public String getDefense() {
        return String.valueOf(armor.protection);
    }
}
class HeroMemento {
    private final int hp;
    private final HeroState state;
    private final List<Item> inventory;
    private final Location location;

    public HeroMemento(int hp, HeroState state, List<Item> inventory, Location location) {
        this.hp = hp;
        this.state = state;
        this.inventory = new ArrayList<>(inventory);
        this.location = location;
    }

    // Getters (pas de setters pour assurer l'immutabilité)
    public int getHp() { return hp; }
    public HeroState getState() { return state; }
    public List<Item> getInventory() { return new ArrayList<>(inventory); }
    public Location getLocation() { return location; }
}

class SaveManager {
    private final Map<String, HeroMemento> savedStates = new HashMap<>();
    private static final String DEFAULT_SAVE = "autosave";

    public void saveState(String saveName, HeroMemento memento) {
        savedStates.put(saveName, memento);
        System.out.println("État sauvegardé sous: " + saveName);
    }

    public HeroMemento getState(String saveName) {
        return savedStates.get(saveName);
    }

    public void listSavedStates() {
        System.out.println("Sauvegardes disponibles:");
        for (String name : savedStates.keySet()) {
            System.out.println("- " + name);
        }
    }
}
