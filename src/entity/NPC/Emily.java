package entity.NPC;

import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import main.GamePanel;
import entity.Item.*;

public class Emily extends NPC {

    private final List<Item> likedItems = Arrays.asList(
            ItemFactory.createItem("Catfish"),
            ItemFactory.createItem("Salmon"),
            ItemFactory.createItem("Sardine")
    );

    private final List<Item> hatedItems = Arrays.asList(
            ItemFactory.createItem("Coal"),
            ItemFactory.createItem("Wood")
    );

    public Emily(GamePanel gp) {
        super("Emily", gp);
        this.gp = gp;
        getImage();
    }

    public void getImage() {
        try {
            imageNPC = ImageIO.read(getClass().getResourceAsStream("/entity/NPC/NPCImage/emily.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void chat() {
        System.out.println("Emily: Aku senang memasak dengan bahan segar dari kebun!");
    }

    @Override
    public void reactToGift(Item item) {
        if (item.getItemName().endsWith("Seeds")) {
            addHeartPoints(25);
        } else if (likedItems.contains(item)) {
            addHeartPoints(20);
        } else if (hatedItems.contains(item)) {
            addHeartPoints(-25);
        }
    }
}