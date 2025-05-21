package entity.NPC;
import entity.Item.Item;
import java.util.ArrayList;

public abstract class NPC {
    protected String name;
    protected int heartPoints = 0;
    protected final int maxHP = 150;
    protected ArrayList<Item> lovedItems = new ArrayList<>();
    protected ArrayList<Item> likedItems = new ArrayList<>();
    protected ArrayList<Item> hatedItems = new ArrayList<>();
    protected String relationshipStatus = "single"; //Semua nya default single dulu
    protected int countChatting = 0;
    protected int countGifting = 0;
    protected int countVisiting = 0;

    public NPC(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getHeartPoints() {
        return heartPoints;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public ArrayList<Item> getLovedItems() {
        return lovedItems;
    }

    public ArrayList<Item> getLikedItems() {
        return likedItems;
    }

    public ArrayList<Item> getHatedItems() {
        return hatedItems;
    }

    public int getCountChatting() {
        return countChatting;
    }

    public int getCountGifting() {
        return countGifting;
    }

    public int getCountVisiting() {
        return countVisiting;
    }

    public void setRelationshipStatus(String status) {
        this.relationshipStatus = status;
    }

    public void incrementChatting() {
        countChatting++;
    }

    public void incrementGifting() {
        countGifting++;
    }

    public void incrementVisiting() {
        countVisiting++;
    }

    public void addHeartPoints(int amount) {
        this.heartPoints = Math.min(this.heartPoints + amount, maxHP);
    }

    // Abstract methods di implementasi di subclass
    public abstract void reactToGift(String itemName);

    public abstract void chat();
}
