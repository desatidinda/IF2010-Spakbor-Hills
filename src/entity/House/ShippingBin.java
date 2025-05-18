package entity.House;

import java.io.IOException;
import javax.imageio.ImageIO;

import objects.GameObject;

public class ShippingBin extends GameObject {
    public ShippingBin() {
        name = "Shipping Bin";
        collision = true;
        widthInTiles = 3;
        heightInTiles = 2;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ObjectImage/shippingbin.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
}
