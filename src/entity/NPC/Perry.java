package entity.NPC;
import entity.Item.Item;

public class Perry extends NPC {
    public Perry() {
        super("Perry");
        lovedItems.add(new Item("Cranberry"));
        lovedItems.add(new Item("Blueberry"));
        likedItems.add(new Item("Wine"));
        // hatedItems segala jenis ikan
    }

    @Override
    public void chat() {
        System.out.println("Perry: Aku sedang menyelesaikan bab terakhir novelku.");
    }

    @Override
    public void reactToGift(Item item) {
        if (lovedItems.contains(item)) {
            addHeartPoints(25);
        } else if (likedItems.contains(item)) {
            addHeartPoints(20);
        } else if (item.getType().equals("Fish")) { 
            addHeartPoints(-25);
        }
    } 
}
