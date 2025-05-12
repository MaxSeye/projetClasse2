package org.example;

import java.util.function.Consumer;

public class ConsumableItem extends Item {
    private Consumer<Hero> effect;

    public ConsumableItem(String name,int weight,int value, Consumer<Hero> effect) {
        super(name,weight,value);
        this.effect = effect;
    }

    @Override
    public boolean isConsumable() {
        return true;
    }

    @Override
    public void use(Hero hero) {
        effect.accept(hero);
        System.out.println("Vous avez utilisé: " + getName());
    }
}