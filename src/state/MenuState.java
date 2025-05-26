package state;


import entity.Player.Player;
import main.GamePanel;

import java.awt.*;

public class MenuState {
    private GamePanel gp;
    private Player player;

    public MenuState(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
    }

    public void draw(Graphics2D g2) {
        int boxWidth = 400;
        int lineHeight = 32;
        int infoCount = 8; 
        int boxHeight = 60 + infoCount * lineHeight;

        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;
       
        gp.ui.drawPopupWindow(g2, boxX, boxY, boxWidth, boxHeight);

        // HEADER
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        gp.ui.drawCenteredText(g2, "PLAYER STATUS", boxX, boxY + 38, boxWidth);

        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        int textY = boxY + 70;

        String[] lines = {
            "Name     : " + player.getName(),
            "Gender   : " + player.getGender(),
            "Farm     : " + player.getFarmName(),
            "Partner  : " + (player.getPartner() != null ? player.getPartner() : "None"),
            "Status   : " + player.getRelationshipStatus(),
            "Energy   : " + player.getEnergy(),
            "Gold     : " + player.getGold()
        };

        for (String line : lines) {
            gp.ui.drawCenteredText(g2, line, boxX, textY, boxWidth);
            textY += lineHeight;
        }
    }
}