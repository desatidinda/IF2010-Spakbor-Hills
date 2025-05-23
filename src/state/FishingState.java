package state;

import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class FishingState implements StateHandler {

    private final GamePanel gp;
    private BufferedImage image;

    public FishingState(GamePanel gp) {
        this.gp = gp;
        loadBackground();
    }

    private void loadBackground() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/seaside.jpg"));
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

        gp.player.draw(g2);
        gp.ui.draw(g2);
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
}

