package entity.House;

import java.io.IOException;
import javax.imageio.ImageIO;
import objects.GameObject;
import java.awt.Rectangle;

public class TV extends GameObject {
    public TV() {
        name = "TV";
        collision = true;
        widthInTiles = 2;
        heightInTiles = 2;
        solidArea = new Rectangle(0, 0, 54, 48); // sesuai ukuran (misal 2 tile * 48)
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ObjectImage/tv.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}