package entity.NPC;

import entity.Item.Item;

public class Caroline extends NPC{
    public Caroline() {
        super("Caroline");
        lovedItems.add(new Item("Firewood"));
        lovedItems.add(new Item("Coal"));
        likedItems.add(new Item("Potato"));
        likedItems.add(new Item("Wheat"));
        hatedItems.add(new Item("Hot Pepper"));
    }

    @Override
    public void chat() {
        System.out.println("Caroline: Seni itu adalah daur ulang yang indah!");
    }

    @Override
    public void reactToGift(Item item) {
        if (lovedItems.contains(item)) {
            addHeartPoints(25);
        } else if (likedItems.contains(item)) {
            addHeartPoints(20);
        } else if (hatedItems.contains(item)) {
            addHeartPoints(-25);
        }
    }
}