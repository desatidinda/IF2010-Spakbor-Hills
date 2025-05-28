package state;


import entity.Player.Player;
import main.GamePanel;
import main.GameStates;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState implements StateHandler {
    private final GamePanel gp;
    private final Player player;
    private final Font vt323;

    public MenuState(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
        this.vt323 = new Font("VT323", Font.PLAIN, 24);
    }

    public void draw(Graphics2D g2) {

        String[] lines = {
            "Name     : " + player.getName(),
            "Gender   : " + player.getGender(),
            "Farm     : " + player.getFarmName(),
            "Partner  : " + (player.getPartner() != null ? player.getPartner() : "None"),
            "Status   : " + player.getRelationshipStatus(),
            "Energy   : " + player.getEnergy(),
            "Gold     : " + player.getGold()
        };

        int boxWidth = 400;
        int lineHeight = 32;
        int boxHeight = 60 + lines.length * lineHeight;

        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;
       
        gp.ui.drawPopupWindow(g2, boxX, boxY, boxWidth, boxHeight);

        // HEADER
        g2.setFont(vt323.deriveFont(Font.BOLD, 24F));
        gp.ui.drawCenteredText(g2, "PLAYER STATUS", boxX, boxY + 38, boxWidth);

        g2.setFont(vt323.deriveFont(Font.PLAIN, 18F));
        int textY = boxY + 70;

        for (String line : lines) {
            gp.ui.drawCenteredText(g2, line, boxX, textY, boxWidth);
            textY += lineHeight;
        }
    }

    @Override
    public void update() {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gp.gameState = GameStates.MAP; 
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}