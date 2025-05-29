package entity.NPC;

import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import main.GamePanel;
import entity.Item.*;

public class Dasco extends NPC {

    private final List<Item> lovedItems = Arrays.asList(
            ItemFactory.createItem("The Legends of Spakbor"),
            ItemFactory.createItem("Cooked Pig's Head"),
            ItemFactory.createItem("Wine"),
            ItemFactory.createItem("Fugu"),
            ItemFactory.createItem("Spakbor Salad")
    );

    private final List<Item> likedItems = Arrays.asList(
            ItemFactory.createItem("Fish Sandwich"),
            ItemFactory.createItem("Fish Stew"),
            ItemFactory.createItem("Baguette"),
            ItemFactory.createItem("Fish n’ Chips")
    );

    private final List<Item> hatedItems = Arrays.asList(
            ItemFactory.createItem("Legend"),
            ItemFactory.createItem("Grape"),
            ItemFactory.createItem("Cauliflower"),
            ItemFactory.createItem("Wheat"),
            ItemFactory.createItem("Pufferfish"),
            ItemFactory.createItem("Salmon")
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
    public String chat() {
        return "Aku tidak menerima bahan mentah. Beri aku makanan mahal!";
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