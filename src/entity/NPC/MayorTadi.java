package entity.NPC;

import entity.Item.Item;

public class MayorTadi extends NPC{
   public MayorTadi() {
        super("Mayor Tadi");
        lovedItems.add(new Item("Legend"));
        likedItems.add(new Item("Angler"));
        likedItems.add(new Item("Crimsonfish"));
        likedItems.add(new Item("Glacierfish"));
        // Sisa Item masuk ke hatedItem secara default
    } 

    @Override
    public void chat() {
        System.out.println("Mayor Tadi: Rakyatku, berikan aku barang-barang langka!");
    }

    @Override
    public void reactToGift(Item item) {
        if (lovedItems.contains(item)) {
            addHeartPoints(25);
        } else if (likedItems.contains(item)) {
            addHeartPoints(20);
        } else {
            addHeartPoints(-25);
        }
    }
}
