package entity.NPC;

import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;

import main.GamePanel;
import entity.Item.*;
public class Abigail extends NPC {

    private final List<Item> lovedItems = Arrays.asList(
            ItemFactory.createItem("Blueberry"),
            ItemFactory.createItem("Melon"),
            ItemFactory.createItem("Pumpkin"),
            ItemFactory.createItem("Grape"),
            ItemFactory.createItem("Cranberry")
    );

    private final List<Item> likedItems = Arrays.asList(
            ItemFactory.createItem("Baguette"),
            ItemFactory.createItem("Pumpkin Pie"),
            ItemFactory.createItem("Wine")
    );

    private final List<Item> hatedItems = Arrays.asList(
            ItemFactory.createItem("Hot Pepper"),
            ItemFactory.createItem("Cauliflower"),
            ItemFactory.createItem("Parsnip"),
            ItemFactory.createItem("Wheat")
    );

    public Abigail(GamePanel gp) {
        super("Abigail", gp);
        this.gp = gp;
        getImage();
    }

    public void getImage() {
        try {
            imageNPC = ImageIO.read(getClass().getResourceAsStream("/entity/NPC/NPCImage/abigail.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(imageNPC, worldX, worldY, gp.tileSize, gp.tileSize, null);
    }

    @Override
    public String chat() {
        return "Ayo eksplorasi alam, energiku harus penuh!";
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