package org.example;

import java.util.*;

public class GameEngine {
    private Hero hero;
    private Location currentLocation;
    private QuestManager questManager;
    private SaveManager saveManager;
    private CombatSystem combatSystem;
    private boolean running;
    private List<Location> allLocations = new ArrayList<>();
    // or
    private Map<String, Location> locationMap = new HashMap<>();

    public GameEngine() {
        questManager = new QuestManager();
        saveManager = new SaveManager();
        combatSystem = new CombatSystem();
        running = false;
    }
    public void initializeGame() {
        System.out.println("Initialisation du jeu...");

        // Création du monde
        initializeWorld();

        running = true;
    }

    private void initializeWorld() {
        Castle castle = new Castle();
        Cave cave = new Cave();
        Woods woods = new Woods();
        allLocations.add(castle);
        allLocations.add(cave);
        allLocations.add(woods);

        // OR Map approach
        locationMap.put(castle.getName().toLowerCase(), castle);
        locationMap.put(cave.getName().toLowerCase(), cave);
        locationMap.put(woods.getName().toLowerCase(), woods);

        NPC blacksmith = new NPC("Forgeron", "Un artisan robuste qui forge des armes et armures.");
        blacksmith.addDialog("Avez-vous besoin d'une nouvelle arme?");
        blacksmith.addDialog("Je peux améliorer votre armure pour quelques pièces d'or.");

        NPC villager = new NPC("Villageois", "Un habitant local qui connaît bien la région.");
        villager.addDialog("Méfiez-vous des bois la nuit.");
        villager.addDialog("Le château est hanté par des chevaliers fantômes.");

        castle.addNPC(blacksmith);
        woods.addNPC(villager);
        Item letter = new DeliveryItem("Lettre du roi",12,50, "Une lettre importante du roi.");
        DeliveryQuest deliveryQuest = new DeliveryQuest(
                "Livraison royale",
                "Livrer la lettre du roi au forgeron.",
                50, blacksmith, letter
        );
        deliveryQuest.addItemReward(new HealthPotion("Potion de santé majeure", 50));

        HuntingQuest huntingQuest = new HuntingQuest(
                "Chasse dans les bois",
                "Chassez 3 animaux sauvages dans les bois.",
                75, "Wolf", 3
        );
        huntingQuest.addItemReward(new MagicPotion("Potion d'essence sauvage", 30));

        villager.addQuest(huntingQuest);
        blacksmith.addQuest(deliveryQuest);

        // Définir la localisation de départ
        currentLocation = castle;
    }
    public void createHero(String nickname) {
        hero = new Hero(nickname, 100);
        hero.setPosition(currentLocation);

        // Équipement de départ
        Sword startingSword = new Sword("Épée du débutant", 8);
        hero.addToInventory(startingSword);
        hero.setEquippedWeapon(startingSword);

        hero.addToInventory(new HealthPotion("Potion de santé mineure", 20));

        System.out.println("Héros créé: " + nickname);
        System.out.println("Bienvenue dans l'aventure!");
    }
        public void moveToLocation(Location newLocation) {
            currentLocation = newLocation;
            hero.setPosition(newLocation);

            System.out.println("\nVous arrivez à: " + newLocation.getName());
            newLocation.describe();

            // Chance aléatoire de rencontrer un ennemi
            if (Math.random() < 0.3) {
                Enemy enemy = newLocation.spawnRandomEnemy();
                if (enemy != null) {
                    System.out.println("\nVous rencontrez un ennemi: " + enemy.getName());
                    combatSystem.startCombat(hero, enemy);
                }
            }

            // Vérifier si des quêtes peuvent être complétées à cet endroit
            questManager.checkQuestCompletion(hero);
        }
    public void talkToNPC(String npcName) {
        for (NPC npc : currentLocation.getNpcs()) {
            if (npc.getName().equalsIgnoreCase(npcName)) {
                System.out.println("\nVous parlez à " + npc.getName());
                npc.speak();

                // Proposer des quêtes si disponibles
                npc.offerQuests(hero);
                return;
            }
        }
        System.out.println("Il n'y a personne de ce nom ici.");
    }
    public void acceptQuest(String npcName, int questIndex) {
        for (NPC npc : currentLocation.getNpcs()) {
            if (npc.getName().equalsIgnoreCase(npcName)) {
                Quest quest = npc.getQuest(questIndex - 1); // -1 car on affiche à partir de 1
                if (quest != null) {
                    quest.accept(hero);
                    questManager.addQuest(quest);
                } else {
                    System.out.println("Cette quête n'existe pas.");
                }
                return;
            }
        }
        System.out.println("Il n'y a personne de ce nom ici.");
    }
    public void viewInventory() {
        List<Item> inventory = hero.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("Votre inventaire est vide.");
            return;
        }

        System.out.println("\n=== Inventaire de " + hero.getNickname() + " ===");
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            String equipped = "";

            if (item instanceof Weapon && ((Weapon)item).equals(hero.getEquippedWeapon())) {
                equipped = " (équipé)";
            } else if (item instanceof Armor && ((Armor)item).equals(hero.getEquippedArmor())) {
                equipped = " (équipé)";
            }

            System.out.println((i+1) + ". " + item.getName() + equipped);
        }
        System.out.println("Or: " + hero.getGold());
    }
    public void useItem(int index) {
        if (index < 1 || index > hero.getInventory().size()) {
            System.out.println("Objet invalide.");
            return;
        }

        Item item = hero.getInventory().get(index - 1);
        item.use(hero);

        if (item.isConsumable()) {
            hero.removeFromInventory(item);
        }
    }
    public void viewCharacterStatus() {
        System.out.println("\n=== Statut de " + hero.getNickname() + " ===");
        System.out.println("Niveau: " + hero.getLevel());
        System.out.println("Expérience: " + hero.getExperience() + "/" + hero.getExperienceToNextLevel());
        System.out.println("Santé: " + hero.getCurrentHealth() + "/" + hero.getMaxHealth());
        System.out.println("Attaque: " + hero.getAttackPower());
        System.out.println("Défense: " + hero.getDefense());

        Weapon equippedWeapon = hero.getEquippedWeapon();
        if (equippedWeapon != null) {
            System.out.println("Arme équipée: " + equippedWeapon.getName() + " (+" + equippedWeapon.getDamage() + ")");
        }

        Armor equippedArmor = hero.getEquippedArmor();
        if (equippedArmor != null) {
            System.out.println("Armure équipée: " + equippedArmor.getName() + " (+" + equippedArmor.getProtection() + ")");
        }
    }
    public void viewActiveQuests() {
        List<Quest> activeQuests = questManager.getActiveQuests();
        if (activeQuests.isEmpty()) {
            System.out.println("Vous n'avez pas de quêtes actives.");
            return;
        }

        System.out.println("\n=== Quêtes Actives ===");
        for (int i = 0; i < activeQuests.size(); i++) {
            Quest quest = activeQuests.get(i);
            System.out.println((i+1) + ". " + quest.getTitle() + " - " + quest.getDescription());
        }
    }
    public void saveGame() {
        System.out.println("Sauvegarde du jeu...");

        // Création d'un memento pour l'état actuel du héros
        HeroMemento heroMemento = new HeroMemento(
                hero.getHp(),
                hero.getState(),
                hero.getInventory(),
                currentLocation
        );

        // Génération d'un nom de sauvegarde avec date/heure
        String saveName = "save_" + System.currentTimeMillis();

        // Enregistrement via le SaveManager
        saveManager.saveState(saveName, heroMemento);

        System.out.println("Jeu sauvegardé avec succès!");
    }
    public boolean loadGame(String saveName) {
        System.out.println("Chargement du jeu...");

        // Récupération du memento depuis le SaveManager
        HeroMemento heroMemento = saveManager.getState(saveName);

        if (heroMemento != null) {
            // Restauration de l'état du héros à partir du memento
            hero.setHp(heroMemento.getHp());
            hero.setState(heroMemento.getState());

            // Restauration de l'inventaire
            hero.getInventory().clear();
            hero.getInventory().addAll(heroMemento.getInventory());

            // Restauration de la position actuelle
            currentLocation = heroMemento.getLocation();

            System.out.println("Jeu chargé avec succès!");
            System.out.println("Héros: " + hero.getNickname() + ", HP: " + hero.getCurrentHealth() + "/" + hero.getMaxHealth());
            System.out.println("Position: " + currentLocation.getName());

            return true;
        } else {
            System.out.println("Erreur: Sauvegarde '" + saveName + "' introuvable.");
            return false;
        }
    }
    public void quit() {
        System.out.println("Quitter le jeu? (O/N)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("O")) {
            System.out.println("Voulez-vous sauvegarder avant de quitter? (O/N)");
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("O")) {
                saveGame();
            }

            System.out.println("Merci d'avoir joué! Au revoir!");
            running = false;
        }
    }
    public boolean isRunning() {
        return running;
    }
    public void processCommand(String command) {
        String[] parts = command.split(" ", 2);
        String action = parts[0].toLowerCase();
        String argument = parts.length > 1 ? parts[1] : "";

        switch (action) {
            case "aller":
                // Logique pour se déplacer vers un lieu
                Location destination = findLocation(argument);
                if (destination != null) {
                    moveToLocation(destination);
                } else {
                    System.out.println("Destination inconnue.");
                }
                break;

            case "parler":
                // Parler à un PNJ
                talkToNPC(argument);
                break;

            case "accepter":
                String[] questArgs = argument.split(" ", 2);
                if (questArgs.length == 2) {
                    try {
                        int questIndex = Integer.parseInt(questArgs[1]);
                        acceptQuest(questArgs[0], questIndex);
                    } catch (NumberFormatException e) {
                        System.out.println("Format invalide. Utilisez: accepter [nom-pnj] [numéro-quête]");
                    }
                } else {
                    System.out.println("Format invalide. Utilisez: accepter [nom-pnj] [numéro-quête]");
                }
                break;

            case "inventaire":
                viewInventory();
                break;

            case "utiliser":
                try {
                    int itemIndex = Integer.parseInt(argument);
                    useItem(itemIndex);
                } catch (NumberFormatException e) {
                    System.out.println("Format invalide. Utilisez: utiliser [numéro-objet]");
                }
                break;

            case "statut":
                viewCharacterStatus();
                break;

            case "quêtes":
                viewActiveQuests();
                break;

            case "sauvegarder":
                saveGame();
                break;

            case "charger":
                loadGame("save_" + System.currentTimeMillis());
                break;

            case "quitter":
                quit();
                break;

            case "aide":
                showHelp();
                break;

            default:
                System.out.println("Commande non reconnue. Tapez 'aide' pour voir les commandes disponibles.");
        }
    }
    private Location findLocation(String name) {
        for (Location location : allLocations) {
            if (location.getName().equalsIgnoreCase(name)) {
                return location;
            }
        }
        return locationMap.get(name.toLowerCase());
    }
    private void showHelp() {
        System.out.println("\n=== Commandes Disponibles ===");
        System.out.println("aller [lieu] - Se déplacer vers un lieu");
        System.out.println("parler [pnj] - Parler à un personnage");
        System.out.println("accepter [pnj] [numéro] - Accepter une quête");
        System.out.println("inventaire - Afficher votre inventaire");
        System.out.println("utiliser [numéro] - Utiliser ou équiper un objet");
        System.out.println("statut - Afficher vos statistiques");
        System.out.println("quêtes - Afficher vos quêtes actives");
        System.out.println("sauvegarder - Sauvegarder la partie");
        System.out.println("charger - Charger une partie sauvegardée");
        System.out.println("quitter - Quitter le jeu");
        System.out.println("aide - Afficher cette aide");
    }
    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Bienvenue dans l'Aventure ===");
        System.out.println("1. Nouvelle partie");
        System.out.println("2. Charger une partie");
        System.out.println("3. Quitter");
        System.out.print("Votre choix: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne

        switch (choice) {
            case 1:
                gameEngine.initializeGame();
                System.out.print("Entrez le nom de votre héros: ");
                String heroName = scanner.nextLine();
                gameEngine.createHero(heroName);
                break;

            case 2:
                if (!gameEngine.loadGame("save_" + System.currentTimeMillis())) {
                    System.out.println("Création d'une nouvelle partie...");
                    gameEngine.initializeGame();
                    System.out.print("Entrez le nom de votre héros: ");
                    heroName = scanner.nextLine();
                    gameEngine.createHero(heroName);
                }
                break;

            case 3:
                System.out.println("Au revoir!");
                return;

            default:
                System.out.println("Choix invalide. Démarrage d'une nouvelle partie.");
                gameEngine.initializeGame();
                System.out.print("Entrez le nom de votre héros: ");
                heroName = scanner.nextLine();
                gameEngine.createHero(heroName);
        }

        // Boucle principale du jeu
        while (gameEngine.isRunning()) {
            System.out.print("\n> ");
            String command = scanner.nextLine();
            gameEngine.processCommand(command);
        }

        scanner.close();
    }
}
