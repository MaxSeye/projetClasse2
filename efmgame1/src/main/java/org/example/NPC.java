package org.example;

import java.util.ArrayList;
import java.util.List;

public class NPC extends Character {
    private String description;
    private List<String> dialogs;
    private List<Quest> availableQuests;

    public NPC(String name, String description) {
        super(name, 50); // Les PNJ ont généralement moins de HP que les héros
        this.description = description;
        this.dialogs = new ArrayList<>();
        this.availableQuests = new ArrayList<>();
    }

    public void addDialog(String dialog) {
        dialogs.add(dialog);
    }

    public void addQuest(Quest quest) {
        availableQuests.add(quest);
    }

    public String getRandomDialog() {
        if (dialogs.isEmpty()) {
            return "Bonjour, aventurier!";
        }
        int index = (int)(Math.random() * dialogs.size());
        return dialogs.get(index);
    }

    public void speak() {
        System.out.println(name + ": \"" + getRandomDialog() + "\"");
    }

    public void offerQuests(Hero hero) {
        if (availableQuests.isEmpty()) {
            System.out.println(name + ": \"Je n'ai pas de quête à vous proposer pour le moment.\"");
            return;
        }

        System.out.println(name + ": \"J'ai des quêtes pour vous:\"");
        for (int i = 0; i < availableQuests.size(); i++) {
            Quest quest = availableQuests.get(i);
            System.out.println((i + 1) + ". " + quest.getTitle() + " - " + quest.getDescription());
        }
    }

    public Quest getQuest(int index) {
        if (index >= 0 && index < availableQuests.size()) {
            return availableQuests.get(index);
        }
        return null;
    }

    public String getDescription() {
        return description;
    }
}

