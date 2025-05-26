package state;

import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import entity.House.KingBed;
import entity.House.Stove;
import entity.House.TV;

public class InsideHouseState implements StateHandler {

    private final GamePanel gp;
    private BufferedImage image;

    public InsideHouseState(GamePanel gp) {
        this.gp = gp;
        loadBackground();
        deployFurniture();
    }

    protected void loadBackground() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/floor.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        gp.player.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, 0, 0, gp.screenWidth, gp.screenHeight, null);

        for (int i = 0; i < gp.furniture.length; i++) {
            if (gp.furniture[i] != null) {
                g2.drawImage(gp.furniture[i].image, gp.furniture[i].worldX, gp.furniture[i].worldY,
                        gp.tileSize * gp.furniture[i].widthInTiles, gp.tileSize * gp.furniture[i].heightInTiles, null);
            }
        }

        gp.player.draw(g2);
        gp.ui.draw(g2);

    }

    protected void deployFurniture() {
        KingBed kingbed = new KingBed();
        kingbed.worldX = 16;
        kingbed.worldY = 8;
        gp.furniture[0] = kingbed;

        Stove stove = new Stove();
        stove.worldX = gp.tileSize * 14 - 16;
        stove.worldY = gp.tileSize * 10 - 16;
        gp.furniture[1] = stove;

        TV tv = new TV();
        tv.worldX = gp.tileSize * 5 - 20;
        tv.worldY = 0;
        gp.furniture[2] = tv;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            gp.keyHandler.upPressed = true;
        } else if (key == KeyEvent.VK_S) {
            gp.keyHandler.downPressed = true;
        } else if (key == KeyEvent.VK_A) {
            gp.keyHandler.leftPressed = true;
        } else if (key == KeyEvent.VK_D) {
            gp.keyHandler.rightPressed = true;
        } else if (key == KeyEvent.VK_ESCAPE) {
            gp.player.teleportOut();
        } else if (key == KeyEvent.VK_SPACE) {
            gp.keyHandler.spacePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            gp.keyHandler.upPressed = false;
        } else if (key == KeyEvent.VK_S) {
            gp.keyHandler.downPressed = false;
        } else if (key == KeyEvent.VK_A) {
            gp.keyHandler.leftPressed = false;
        } else if (key == KeyEvent.VK_D) {
            gp.keyHandler.rightPressed = false;
        } else if (key == KeyEvent.VK_SPACE) {
            gp.keyHandler.spacePressed = false;
        }
    }

    // public GameObject[] getFurniture() {
    //     return furniture;
    // }

}

