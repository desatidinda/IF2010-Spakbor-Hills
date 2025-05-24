package entity.NPC;

import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import main.GamePanel;

public class Perry extends NPC {

    private final List<String> lovedItemNames = Arrays.asList(
            "Cranberry", "Blueberry"
    );

    private final List<String> likedItemNames = Arrays.asList(
            "Wine"
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
    public void reactToGift(String itemName) {
        String lowerName = itemName.toLowerCase();

        if (lovedItemNames.contains(itemName)) {
            addHeartPoints(25);
        } else if (likedItemNames.contains(itemName)) {
            addHeartPoints(20);
        } else if (lowerName.contains("fish") || lowerName.contains("salmon") || lowerName.contains("catfish") || lowerName.contains("sardine")) {
            addHeartPoints(-25);
        }
    }
}