package org.example;

public abstract class Item {
    protected String name;
    protected int weight;
    protected int value;


    public Item(String name, int weight, int value) {
        this.name = name;
        this.weight = weight;
        this.value = value;
    }

    public String getName() { return name; }
    public int getWeight() { return weight; }
    public int getValue() { return value; }

    public abstract boolean isConsumable();


    public abstract void use(Hero hero);
}