package entity.NPC;

import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import main.GamePanel;
import entity.Item.*;

public class Perry extends NPC {

    private final List<Item> lovedItems = Arrays.asList(
            ItemFactory.createItem("Cranberry"),
            ItemFactory.createItem("Blueberry")
    );

    private final List<Item> likedItems = Arrays.asList(
            ItemFactory.createItem("Wine")
    );

    public Perry(GamePanel gp) {
        super("Perry", gp);
        this.gp = gp;
        getImage();
    }

    public void getImage() {
        try {
            imageNPC = ImageIO.read(getClass().getResourceAsStream("/entity/NPC/NPCImage/perry.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void chat() {
        System.out.println("Perry: Aku sedang menyelesaikan bab terakhir novelku.");
    }

    @Override
    public void reactToGift(Item item) {
        String lowerName = item.getItemName().toLowerCase();

        if (lovedItems.contains(item)) {
            addHeartPoints(25);
        } else if (likedItems.contains(item)) {
            addHeartPoints(20);
        } else if (lowerName.contains("fish") || lowerName.contains("salmon") || lowerName.contains("catfish") || lowerName.contains("sardine")) {
            addHeartPoints(-25);
        }
    }
}