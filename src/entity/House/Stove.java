package entity.House;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import objects.GameObject;
import entity.Player.Player;
import state.InsideHouseState;
import main.GamePanel;
import main.GameStates;

public class Stove extends GameObject {
    private GamePanel gp;
    public Stove(GamePanel gp) {
        this.gp = gp;

        name = "Stove";
        collision = true;
        widthInTiles = 2;
        heightInTiles = 2;
        solidArea = new Rectangle(0, 0, 36, 36); // sesuai ukuran (misal 2 tile * 48)
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ObjectImage/stove.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }

        @Override
        public void playerInteract(Player player) {
            ((InsideHouseState) gp.stateHandlers.get(GameStates.INSIDE_HOUSE)).setShowRecipeList(true);
        }
}