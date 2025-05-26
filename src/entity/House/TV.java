package entity.House;

import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entity.Player.Player;
import main.GameClock;
import objects.GameObject;
import entity.Farm.Weather;

public class TV extends GameObject {
    BufferedImage tvsunny, tvrainy;
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
            tvsunny = ImageIO.read(getClass().getResourceAsStream("/res/tvsunny.png"));
            tvrainy = ImageIO.read(getClass().getResourceAsStream("/res/tvrainy.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }

    @Override
    public void playerInteract(Player player) {
        //TODO: Implement TV interaction logic
    }

    public BufferedImage getTvsunny() {
        return tvsunny;
    }

    public BufferedImage getTvrainy() {
        return tvrainy;
    }

}