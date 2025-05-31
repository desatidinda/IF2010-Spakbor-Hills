package state;
import entity.Player.Player;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import controller.UIController;
import main.GamePanel;
import main.GameStates;

public class MenuState implements StateHandler {
    private final GamePanel gp;
    private final Player player;
    private final Font vt323;
    private BufferedImage backgroundImage;
    private BufferedImage creditImage;
    private UIController ui;
    private boolean showInformation = false;
    private boolean showCredits = false; 
    
    public MenuState(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
        this.vt323 = new Font("VT323", Font.PLAIN, 24);
        this.ui = gp.ui; 
        getBackgroundImage();
        getCreditImage();
    }
    
    public void getBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/res/menu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void getCreditImage() {
        try {
            creditImage = ImageIO.read(getClass().getResourceAsStream("/res/credit.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
        
        if (showCredits && creditImage != null) {
            g2.drawImage(creditImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
            return; 
        }
    
        if (showInformation) {
            ui.drawInformation(g2);
            return; 
        }

        List<String> lines = new ArrayList<>();
        lines.add("Name     : " + player.getName());
        lines.add("Gender   : " + player.getGender());
        lines.add("Farm     : " + player.getFarmName());
        lines.add("Energy   : " + player.getEnergy());
        lines.add("Partner  : " + (player.getPartner() != null ? player.getPartner() : "None"));
        //lines.add("Status   : " + player.getRelationshipStatus());
        lines.add("Favorite Item : " + player.getFavoriteItem().getItemName());
        lines.add("Gold     : " + player.getGold());

        if (player.hasReachedEndgame()) {
            lines.add("");
            lines.add("Press S to view End Game Statistics");
        }

        lines.add("Press Shift to show information about the game");
        lines.add("Press I to show item list");
        lines.add("Press A to show credits");
        lines.add("Press Q to exit game");
        lines.add("Press ESC to exit menu");

        int boxWidth = 500; 
        int lineHeight = 32;
        int boxHeight = 60 + lines.size() * lineHeight;
        int cornerRadius = 20; 
        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;
        
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, cornerRadius, cornerRadius);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, cornerRadius, cornerRadius);
        
        // HEADER
        g2.setFont(vt323.deriveFont(Font.BOLD, 24F));
        g2.setColor(Color.WHITE);
        gp.ui.drawCenteredText(g2, "PLAYER STATUS", boxX, boxY + 38, boxWidth);
        g2.setFont(vt323.deriveFont(Font.PLAIN, 18F));
        int textY = boxY + 70;

        for (String line : lines) {
            g2.drawString(line, boxX + 30, textY); 
            textY += lineHeight;
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (showCredits) {
            showCredits = false;
            return;
        }
        
        if (showInformation) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                showInformation = false;
            }
            return; 
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gp.gameState = GameStates.MAP;
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            showInformation = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            showCredits = true; 
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_S && player.hasReachedEndgame()) {
            gp.gameState = GameStates.STATISTICS;
        } else if (e.getKeyCode() == KeyEvent.VK_I) {
            gp.gameState = GameStates.ITEMLIST;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}