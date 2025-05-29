package entity.NPC;

import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import entity.Item.*;
import main.GamePanel;

public class MayorTadi extends NPC {
    GamePanel gp;

    private final List<Item> lovedItems = Arrays.asList(
            ItemFactory.createItem("Legend")
    );

    private final List<Item> likedItems = Arrays.asList(
            ItemFactory.createItem("Angler"),
            ItemFactory.createItem("Crimsonfish"),
            ItemFactory.createItem("Glacierfish")
    );

    public MayorTadi(GamePanel gp) {
        super("Mayor Tadi", gp);
        this.gp = gp;
        getImage();
    }
    
    public void getImage() {
        try {
            imageNPC = ImageIO.read(getClass().getResourceAsStream("/entity/NPC/NPCImage/mayortadi.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String chat() {
        return "Rakyatku, berikan aku barang-barang langka!";
    }

    @Override
    public void reactToGift(Item item) {
        if (lovedItems.contains(item)) {
            addHeartPoints(25);
        } else if (likedItems.contains(item)) {
            addHeartPoints(20);
        } else {
            addHeartPoints(-25);
        }
    }
}