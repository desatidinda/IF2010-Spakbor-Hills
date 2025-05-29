package entity.House;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import objects.GameObject;
import entity.Player.Player;
import main.GameClock;
import entity.Farm.ShippingBin;

public class KingBed extends GameObject {
    public KingBed() {
        name = "King Bed";
        collision = true;
        widthInTiles = 3;
        heightInTiles = 4;
        solidArea = new Rectangle(0, 0, 96, 120); // sesuai ukuran (misal 2 tile * 48)
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ObjectImage/kingbed.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }

    @Override
    public void playerInteract(Player player) {
        player.sleep();
        GameClock.skipToMorning(); 
        ShippingBin.hasUsedTodayBin = false;
        ShippingBin.processPendingSales();  
    }
}