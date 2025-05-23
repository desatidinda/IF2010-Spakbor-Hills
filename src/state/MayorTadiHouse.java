package state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import main.GamePanel;

public class MayorTadiHouse extends InsideHouseState {
    private final GamePanel gp;
    private BufferedImage imageNPC;
    public MayorTadiHouse(GamePanel gp) {
        super(gp);
        this.gp = gp;
        super.loadBackground();
        super.deployFurniture();
        loadNPC();
        deployNPC();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
    }

    public void loadNPC() {
        try {
            imageNPC = ImageIO.read(getClass().getResourceAsStream("/entity/NPC/NPCImage/mayortadi.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void deployNPC() {
        // KingBed kingbed = new KingBed();
        // kingbed.worldX = 8;
        // kingbed.worldY = 0;
        // gp.furniture[10] = kingbed;
    }
}
