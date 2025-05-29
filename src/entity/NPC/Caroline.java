package entity.NPC;

import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import entity.Item.*;
import main.GamePanel;

public class Caroline extends NPC {

    private final List<Item> lovedItems = Arrays.asList(
            ItemFactory.createItem("Firewood"),
            ItemFactory.createItem("Coal")
    );

    private final List<Item> likedItems = Arrays.asList(
            ItemFactory.createItem("Potato"),
            ItemFactory.createItem("Wheat")
    );

    private final List<Item> hatedItems = Arrays.asList(
            ItemFactory.createItem("Hot Pepper")
    );

    public Caroline(GamePanel gp) {
        super("Caroline", gp);
        this.gp = gp;
        getImage();
    }

    public void getImage() {
        try {
            imageNPC = ImageIO.read(getClass().getResourceAsStream("/entity/NPC/NPCImage/caroline.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String chat() {
        return "Seni itu adalah daur ulang yang indah!";
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