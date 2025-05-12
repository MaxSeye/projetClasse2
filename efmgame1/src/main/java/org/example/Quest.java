package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Quest {
    protected String title;
    protected String description;
    protected int experienceReward;
    protected List<Item> itemRewards;
    protected boolean completed;
    protected boolean active;

    public Quest(String title, String description, int experienceReward) {
        this.title = title;
        this.description = description;
        this.experienceReward = experienceReward;
        this.itemRewards = new ArrayList<>();
        this.completed = false;
        this.active = false;
    }

    public abstract boolean checkCompletion(Hero hero);

    public void accept(Hero hero) {
        active = true;
        System.out.println(hero.getNickname() + " a accepté la quête: " + title);
    }

    public void complete(Hero hero) {
        if (active && !completed) {
            hero.gainExperience(experienceReward);
            for (Item item : itemRewards) {
                hero.addToInventory(item);
            }
            completed = true;
            System.out.println("Quête terminée: " + title);
            System.out.println("Récompense: " + experienceReward + " points d'expérience");
        }
    }

    public void addItemReward(Item item) {
        itemRewards.add(item);
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public boolean isActive() { return active; }
}

class DeliveryQuest extends Quest {
    private NPC target;
    private Item itemToDeliver;

    public DeliveryQuest(String title, String description, int experienceReward, NPC target, Item itemToDeliver) {
        super(title, description, experienceReward);
        this.target = target;
        this.itemToDeliver = itemToDeliver;
    }

    @Override
    public boolean checkCompletion(Hero hero) {
        // Pour compléter la quête, le héros doit avoir l'objet et être au même endroit que la cible
        return hero.getInventory().contains(itemToDeliver) &&
                hero.getPosition().equals(target.getPosition());
    }

    @Override
    public void complete(Hero hero) {
        if (checkCompletion(hero)) {
            hero.removeFromInventory(itemToDeliver);
            super.complete(hero);
        }
    }
}

class HuntingQuest extends Quest {
    private String animalType;
    private int count;
    private int currentCount;

    public HuntingQuest(String title, String description, int experienceReward, String animalType, int count) {
        super(title, description, experienceReward);
        this.animalType = animalType;
        this.count = count;
        this.currentCount = 0;
    }

    public void incrementCount() {
        currentCount++;
        System.out.println("Progression de la chasse: " + currentCount + "/" + count);
    }

    @Override
    public boolean checkCompletion(Hero hero) {
        return currentCount >= count;
    }
}

// Gestionnaire de quêtes
class QuestManager {
    private List<Quest> activeQuests;

    public QuestManager() {
        activeQuests = new ArrayList<>();
    }

    public void addQuest(Quest quest) {
        activeQuests.add(quest);
    }

    public void checkQuestCompletion(Hero hero) {
        Iterator<Quest> iterator = activeQuests.iterator();
        while (iterator.hasNext()) {
            Quest quest = iterator.next();
            if (quest.isActive() && !quest.isCompleted() && quest.checkCompletion(hero)) {
                quest.complete(hero);
            }

            if (quest.isCompleted()) {
                iterator.remove();
            }
        }
    }

    public void displayActiveQuests() {
        if (activeQuests.isEmpty()) {
            System.out.println("Aucune quête active.");
            return;
        }

        System.out.println("Quêtes actives:");
        for (Quest quest : activeQuests) {
            if (quest.isActive() && !quest.isCompleted()) {
                System.out.println("- " + quest.getTitle() + ": " + quest.getDescription());
            }
        }
    }

    public List<Quest> getActiveQuests() {
        return activeQuests;
    }
}

// Classes pour le système de notification
class Contact {
    private String name;
    private List<Notification> notifications;

    public Contact(String name) {
        this.name = name;
        this.notifications = new ArrayList<>();
    }

    public void sendNotification(Notification notification) {
        notifications.add(notification);
        System.out.println(name + " a reçu une notification: " + notification.getTitle());
    }

    public void readNotifications() {
        if (notifications.isEmpty()) {
            System.out.println(name + " n'a aucune notification.");
            return;
        }

        System.out.println("Notifications de " + name + ":");
        for (Notification notification : notifications) {
            System.out.println("- " + notification.getTitle() + ": " + notification.getMessage());
            System.out.println("  " + notification.getDetails());
        }
    }

    public String getName() { return name; }
}

class Notification {
    private String title;
    private String message;
    private String details;
    private String location;

    public Notification(String title, String message, String details, String location) {
        this.title = title;
        this.message = message;
        this.details = details;
        this.location = location;
    }

    // Getters
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getDetails() { return details; }
    public String getLocation() { return location; }
}

