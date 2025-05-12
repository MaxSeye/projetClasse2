package org.example;

public class DeliveryItem extends Item {
    private String description;
    public DeliveryItem(String name,int weight,int value, String description) {
        super(name,weight,value);
        this.description=description;
    }

    @Override
    public boolean isConsumable() {
        return false;
    }
    @Override
    public void use(Hero hero) {
        System.out.println("Vous ne pouvez pas utiliser cet objet.");
    }
}
