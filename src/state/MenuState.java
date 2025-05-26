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
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));

        int x = 50;
        int y = 60;
        int lineHeight = 40;

        g2.drawString("PLAYER STATUS", x, y); y += lineHeight;
        g2.drawString("Name     : " + player.getName(), x, y); y += lineHeight;
        g2.drawString("Gender   : " + player.getGender(), x, y); y += lineHeight;
        g2.drawString("Farm     : " + player.getFarmName(), x, y); y += lineHeight;
        g2.drawString("Partner  : " + (player.getPartner() != null ? player.getPartner() : "None"), x, y); y += lineHeight;
        g2.drawString("Status   : " + player.getRelationshipStatus(), x, y); y += lineHeight;
        g2.drawString("Energy   : " + player.getEnergy(), x, y); y += lineHeight;
        g2.drawString("Gold     : " + player.getGold(), x, y); y += lineHeight;
    }
}