package entity.NPC;

import entity.Item.Item;


public class Dasco extends NPC{
    public Dasco() {
        super("Dasco");
        lovedItems.add(new Item("The Legends of Spakbor"));
        lovedItems.add(new Item("Cooked Pig's Head"));
        lovedItems.add(new Item("Wine"));
        lovedItems.add(new Item("Fugu"));
        lovedItems.add(new Item("Spakbor Salad"));

        likedItems.add(new Item("Fish Sandwich"));
        likedItems.add(new Item("Fish Stew"));
        likedItems.add(new Item("Baguette"));
        likedItems.add(new Item("Fish n’ Chips"));

        hatedItems.add(new Item("Legend"));
        hatedItems.add(new Item("Grape"));
        hatedItems.add(new Item("Cauliflower"));
        hatedItems.add(new Item("Wheat"));
        hatedItems.add(new Item("Pufferfish"));
        hatedItems.add(new Item("Salmon"));
    }

    @Override
    public void chat() {
        System.out.println("Dasco: Aku tidak menerima bahan mentah. Beri aku makanan mahal!");
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
