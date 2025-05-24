package entity.NPC;

import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import main.GamePanel;

public class MayorTadi extends NPC {
    GamePanel gp;

    private final List<String> lovedItemNames = Arrays.asList(
            "Legend"
    );

    private final List<String> likedItemNames = Arrays.asList(
            "Angler", "Crimsonfish", "Glacierfish"
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
    public void chat() {
        System.out.println("Mayor Tadi: Rakyatku, berikan aku barang-barang langka!");
    }

    @Override
    public void reactToGift(String itemName) {
        if (lovedItemNames.contains(itemName)) {
            addHeartPoints(25);
        } else if (likedItemNames.contains(itemName)) {
            addHeartPoints(20);
        } else {
            addHeartPoints(-25);
        }
    }
}