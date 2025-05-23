package entity.House;

import java.io.IOException;
import javax.imageio.ImageIO;
import objects.GameObject;

public class Stove extends GameObject {
    public Stove() {
        name = "Stove";
        collision = true;
        widthInTiles = 2;
        heightInTiles = 2;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ObjectImage/stove.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}