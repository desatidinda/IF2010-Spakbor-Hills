package state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import main.GamePanel;
import entity.NPC.*;

public class NPCHouseState extends InsideHouseState {
    private final GamePanel gp;
    private NPC npcInHouse;

    public NPCHouseState(GamePanel gp, NPC npc) {
        super(gp);
        this.gp = gp;
        this.npcInHouse = npc;
        npcInHouse.worldX = gp.player.houseX + 7 * gp.tileSize;
        npcInHouse.worldY = gp.player.houseY - 5 * gp.tileSize;
        super.loadBackground();
        super.deployFurniture();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        if (npcInHouse != null) {
            npcInHouse.draw(g2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
    }

    public NPC getNpcInHouse() {
        return npcInHouse;
    }
}
