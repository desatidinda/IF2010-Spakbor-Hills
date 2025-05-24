package entity.NPC;

import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;

import main.GamePanel;

public class Abigail extends NPC {

    private final List<String> lovedItemNames = Arrays.asList(
            "Blueberry", "Melon", "Pumpkin", "Grape", "Cranberry"
    );

    private final List<String> likedItemNames = Arrays.asList(
            "Baguette", "Pumpkin Pie", "Wine"
    );

    private final List<String> hatedItemNames = Arrays.asList(
            "Hot Pepper", "Cauliflower", "Parsnip", "Wheat"
    );

    public Abigail(GamePanel gp) {
        super("Abigail", gp);
    }

    public void getImage() {
        try {
            imageNPC = ImageIO.read(getClass().getResourceAsStream("/entity/NPC/NPCImage/mayortadi.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(imageNPC, worldX, worldY, gp.tileSize * widthInTiles, gp.tileSize * heightInTiles, null);
    }

    @Override
    public void chat() {
        System.out.println("Abigail: Ayo eksplorasi alam, energiku harus penuh!");
    }

    @Override
    public void reactToGift(String itemName) {
        if (lovedItemNames.contains(itemName)) {
            addHeartPoints(25);
        } else if (likedItemNames.contains(itemName)) {
            addHeartPoints(20);
        } else if (hatedItemNames.contains(itemName)) {
            addHeartPoints(-25);
        }
    }
}