package entity.NPC;

import entity.Item.Item;

public class Emily extends NPC{
    public Emily() {
        super("Emily");
        likedItems.add(new Item("Catfish"));
        likedItems.add(new Item("Salmon"));
        likedItems.add(new Item("Sardine"));

        hatedItems.add(new Item("Coal"));
        hatedItems.add(new Item("Wood"));
        //lovedItems segala jenis seed 
    }

    @Override
    public void chat() {
        System.out.println("Emily: Aku senang memasak dengan bahan segar dari kebun!");
    }

    @Override
    public void reactToGift(Item item) {
        if (item.getType().equals("Seed")) {
            addHeartPoints(25); 
        } else if (likedItems.contains(item)) {
            addHeartPoints(20);
        } else if (hatedItems.contains(item)) {
            addHeartPoints(-25);
        }
    }
}
