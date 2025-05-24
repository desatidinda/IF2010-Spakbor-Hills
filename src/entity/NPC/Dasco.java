package entity.NPC;

import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import main.GamePanel;

public class Dasco extends NPC {

    private final List<String> lovedItemNames = Arrays.asList(
            "The Legends of Spakbor", "Cooked Pig's Head", "Wine", "Fugu", "Spakbor Salad"
    );

    private final List<String> likedItemNames = Arrays.asList(
            "Fish Sandwich", "Fish Stew", "Baguette", "Fish n’ Chips"
    );

    private final List<String> hatedItemNames = Arrays.asList(
            "Legend", "Grape", "Cauliflower", "Wheat", "Pufferfish", "Salmon"
    );

    public Dasco(GamePanel gp) {
        super("Dasco", gp);
        this.gp = gp;
        getImage();
    }

    public void getImage() {
        try {
            imageNPC = ImageIO.read(getClass().getResourceAsStream("/entity/NPC/NPCImage/dasco.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void chat() {
        System.out.println("Dasco: Aku tidak menerima bahan mentah. Beri aku makanan mahal!");
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