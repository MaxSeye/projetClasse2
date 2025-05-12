package org.example;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Hero hero;
    private final Location currentLocation;
    private final List<Quest> activeQuests;
    private final long timestamp;

    /**
     * Creates a new GameState with the current game information.
     *
     * @param hero The player's hero
     * @param currentLocation The hero's current location
     * @param activeQuests List of quests that are currently active
     */
    public GameState(Hero hero, Location currentLocation, List<Quest> activeQuests) {
        this.hero = hero;
        this.currentLocation = currentLocation;
        this.activeQuests = activeQuests;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public Hero getHero() {
        return hero;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public List<Quest> getActiveQuests() {
        return activeQuests;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a summary of this game state.
     */
    public String getSummary() {
        return "Sauvegarde du " + new java.util.Date(timestamp) + "\n" +
                "Héros: " + hero.getNickname() + " (Niveau " + hero.getLevel() + ")\n" +
                "Lieu: " + currentLocation.getName() + "\n" +
                "Quêtes actives: " + activeQuests.size();
    }
}