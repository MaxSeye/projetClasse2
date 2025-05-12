package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Location {
    protected String name;
    protected String description;
    protected List<NPC> npcs;
    protected List<Enemy> potentialEnemies;

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
        this.npcs = new ArrayList<>();
        this.potentialEnemies = new ArrayList<>();
    }

    public void addNPC(NPC npc) {
        npcs.add(npc);
        npc.setPosition(this);
    }

    public void addPotentialEnemy(Enemy enemy) {
        potentialEnemies.add(enemy);
    }

    public Enemy spawnRandomEnemy() {
        if (potentialEnemies.isEmpty()) {
            return null;
        }
        int index = (int)(Math.random() * potentialEnemies.size());
        // Création d'une nouvelle instance pour ne pas modifier l'original
        Enemy enemy = EnemyFactory.createEnemyFromType(potentialEnemies.get(index).getClass().getSimpleName());
        enemy.setPosition(this);
        return enemy;
    }

    public void describe() {
        System.out.println(name + " - " + description);
        if (!npcs.isEmpty()) {
            System.out.println("Personnages présents:");
            for (NPC npc : npcs) {
                System.out.println("- " + npc.getName() + ": " + npc.getDescription());
            }
        }
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<NPC> getNpcs() { return npcs; }
}
class Castle extends Location {
    public Castle() {
        super("Château Royal", "Un imposant château aux hautes tours et aux remparts impressionnants.");
        // Ajout des ennemis potentiels spécifiques au château
        addPotentialEnemy(new ShovelKnight());
        addPotentialEnemy(new GhostKnight());
    }

    @Override
    public Enemy spawnRandomEnemy() {
        // Personnalisation du spawn des ennemis dans le château
        // Plus de chance d'avoir des chevaliers fantômes la nuit
        boolean isNight = GameTime.isNight();
        if (isNight && Math.random() < 0.7) {
            Enemy enemy = new GhostKnight();
            enemy.setPosition(this);
            return enemy;
        }
        return super.spawnRandomEnemy();
    }
}
class Cave extends Location {
    private boolean explored;

    public Cave() {
        super("Caverne Sombre", "Une caverne humide et sombre, où l'écho résonne au moindre bruit.");
        explored = false;
        // Ajout des ennemis potentiels spécifiques à la caverne
        addPotentialEnemy(new ShovelKnight());
    }

    @Override
    public void describe() {
        super.describe();
        if (!explored) {
            System.out.println("Cette partie de la caverne n'a pas encore été explorée.");
        }
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
        if (explored) {
            System.out.println("Vous avez exploré cette partie de la caverne.");
        }
    }

    public boolean isExplored() {
        return explored;
    }
}

class Woods extends Location {
    private List<Animal> animals;

    public Woods() {
        super("Bois Mystérieux", "Une forêt dense aux arbres séculaires, traversée de sentiers étroits.");
        animals = new ArrayList<>();
        initializeAnimals();
    }

    private void initializeAnimals() {
        // Ajout d'animaux dans les bois
        animals.add(new DomesticAnimal("Lapin", 10));
        animals.add(new DomesticAnimal("Écureuil", 5));
        animals.add(new WildAnimal("Loup", 30));
        animals.add(new WildAnimal("Sanglier", 40));
    }

    public Animal findRandomAnimal() {
        if (animals.isEmpty()) {
            return null;
        }
        int index = (int)(Math.random() * animals.size());
        return animals.get(index);
    }

    @Override
    public void describe() {
        super.describe();
        System.out.println("Vous entendez des bruits d'animaux autour de vous.");
    }

    public List<Animal> getAnimals() {
        return animals;
    }
}

// Factory pour créer des lieux
class LocationFactory {
    public static Location createLocation(String type) {
        switch (type.toLowerCase()) {
            case "castle":
            case "château":
                return new Castle();
            case "cave":
            case "caverne":
                return new Cave();
            case "woods":
            case "bois":
                return new Woods();
            default:
                throw new IllegalArgumentException("Type de lieu inconnu: " + type);
        }
    }
}
class EnemyFactory {
    public static Enemy createEnemy(String type) {
        switch (type.toLowerCase()) {
            case "shovelknight":
            case "chevalier à la pelle":
                return new ShovelKnight();
            case "ghostknight":
            case "chevalier fantôme":
                return new GhostKnight();
            default:
                throw new IllegalArgumentException("Type d'ennemi inconnu: " + type);
        }
    }

    public static Enemy createEnemyFromType(String className) {
        switch (className) {
            case "ShovelKnight":
                return new ShovelKnight();
            case "GhostKnight":
                return new GhostKnight();
            default:
                throw new IllegalArgumentException("Classe d'ennemi inconnue: " + className);
        }
    }
}
class GameTime {
    private static int hour = 12; // Par défaut midi

    public static boolean isNight() {
        return hour >= 20 || hour < 6;
    }

    public static void advanceTime(int hours) {
        hour = (hour + hours) % 24;
        System.out.println("Il est maintenant " + hour + "h00.");
    }

    public static int getHour() {
        return hour;
    }
}