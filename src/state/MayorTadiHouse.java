package state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import main.GamePanel;
import entity.NPC.MayorTadi;

public class MayorTadiHouse extends InsideHouseState {
    private final GamePanel gp;
    public MayorTadiHouse(GamePanel gp) {
        super(gp);
        this.gp = gp;
        super.loadBackground();
        super.deployFurniture();
        deployNPC();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        gp.npc[0].draw(g2);
        // for (int i = 0; i < gp.npc.length; i++) {
        //     if (gp.npc[i] != null) {
        //         gp.npc[i].draw(g2);
        //     }
        // }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
    }

    protected void deployNPC() {
        MayorTadi mayorTadi = new MayorTadi(gp);
        mayorTadi.worldX = gp.player.houseX + gp.tileSize;
        mayorTadi.worldY = gp.player.houseY + 9 * gp.tileSize;
        gp.npc[0] = mayorTadi;
    }
}
