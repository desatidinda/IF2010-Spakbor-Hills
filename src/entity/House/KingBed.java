package entity.House;

import java.io.IOException;
import javax.imageio.ImageIO;
import objects.GameObject;

public class KingBed extends GameObject {
    public KingBed() {
        name = "King Bed";
        collision = true;
        widthInTiles = 3;
        heightInTiles = 4;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ObjectImage/kingbed.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}