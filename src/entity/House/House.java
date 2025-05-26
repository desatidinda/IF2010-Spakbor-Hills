package entity.House;

import java.io.IOException;
import javax.imageio.ImageIO;

import entity.Player.Player;
import main.GameClock;
import objects.GameObject;

public class House extends GameObject {

    public House() {
        name = "House";
        collision = true;
        widthInTiles = 6;
        heightInTiles = 6;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ObjectImage/house.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "House (size: " + widthInTiles + "x" + heightInTiles + ")";
    }
}
