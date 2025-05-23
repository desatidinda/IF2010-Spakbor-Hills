package entity.Farm;

import java.io.IOException;
import javax.imageio.ImageIO;

import objects.GameObject;

public class Pond extends GameObject {
    public Pond() {
        name = "Pond";
        collision = true;
        widthInTiles = 3;
        heightInTiles = 4;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ObjectImage/pond.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
