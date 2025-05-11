package entity.NPC;

import entity.Item.Item;

public class Abigail {
    public Abigail() {
        super("Abigail");
        lovedItems.add(new Item("Blueberry"));
        lovedItems.add(new Item("Melon"));
        lovedItems.add(new Item("Pumpkin"));
        lovedItems.add(new Item("Grape"));
        lovedItems.add(new Item("Cranberry"));

        likedItems.add(new Item("Baguette"));
        likedItems.add(new Item("Pumpkin Pie"));
        likedItems.add(new Item("Wine"));

        hatedItems.add(new Item("Hot Pepper"));
        hatedItems.add(new Item("Cauliflower"));
        hatedItems.add(new Item("Parsnip"));
        hatedItems.add(new Item("Wheat"));
    } 

    @Override
    public void chat() {
        System.out.println("Abigail: Ayo eksplorasi alam, energiku harus penuh!");
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
