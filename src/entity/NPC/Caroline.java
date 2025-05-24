package entity.NPC;

import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import main.GamePanel;

public class Caroline extends NPC {

    private final List<String> lovedItemNames = Arrays.asList(
            "Firewood", "Coal"
    );

    private final List<String> likedItemNames = Arrays.asList(
            "Potato", "Wheat"
    );

    private final List<String> hatedItemNames = Arrays.asList(
            "Hot Pepper"
    );

    public Caroline(GamePanel gp) {
        super("Caroline", gp);
    }

    public void getImage() {
        try {
            imageNPC = ImageIO.read(getClass().getResourceAsStream("/entity/NPC/NPCImage/mayortadi.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void chat() {
        System.out.println("Caroline: Seni itu adalah daur ulang yang indah!");
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