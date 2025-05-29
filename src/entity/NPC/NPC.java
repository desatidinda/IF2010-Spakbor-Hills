package entity.NPC;
import entity.Item.Item;
import main.GamePanel;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

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
    protected BufferedImage imageNPC;
    public final boolean collision = true;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 16, 16);
    public int solidAreaDefaultX = solidArea.x;
    public int solidAreaDefaultY = solidArea.y;
    GamePanel gp;

    public NPC(String name, GamePanel gp) {
        this.name = name;
        this.gp = gp;
    }

    public abstract void getImage();

    public void draw(Graphics2D g2) {
        g2.drawImage(imageNPC, worldX, worldY, gp.tileSize, gp.tileSize, null);
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

    // method abstract di implementasinya di subclass ya
    public abstract void reactToGift(Item item);

    public abstract String chat();
}
