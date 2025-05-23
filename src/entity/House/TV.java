package entity.House;

import java.io.IOException;
import javax.imageio.ImageIO;
import objects.GameObject;

public class TV extends GameObject {
    public TV() {
        name = "TV";
        collision = true;
        widthInTiles = 2;
        heightInTiles = 2;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ObjectImage/tv.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}