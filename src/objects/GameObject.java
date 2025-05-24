package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class GameObject {
    public BufferedImage image;
    protected String name;
    public boolean collision;
    public int worldX, worldY;
    public int houseX, houseY;
    public int widthInTiles;
    public int heightInTiles;
    public Rectangle solidArea = new Rectangle(0,0, 48, 48);
    public int solidAreaDefaultX = 0, solidAreaDefaultY = 0;
    public int solidAreaDefaultWidth = 48, solidAreaDefaultHeight = 48;

    public void draw(Graphics2D g2, GamePanel gp) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        int objectWidth = gp.tileSize * widthInTiles;
        int objectHeight = gp.tileSize * heightInTiles;

        if (worldX + objectWidth > gp.player.worldX - gp.player.screenX &&
            worldX < gp.player.worldX + gp.player.screenX + gp.tileSize &&
            worldY + objectHeight > gp.player.worldY - gp.player.screenY &&
            worldY < gp.player.worldY + gp.player.screenY + gp.tileSize) {

            g2.drawImage(image, screenX, screenY, objectWidth, objectHeight, null);
        }

    }

    public String getName() {
        return name;
    }
}
