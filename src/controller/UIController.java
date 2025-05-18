package controller;

import java.awt.*;
import java.text.DecimalFormat;
// import java.awt.Color;
// import java.awt.Font;

import main.GamePanel;
import main.GameStates;

public class UIController {
    GamePanel gp;
    Graphics2D  g2;
    Font arial_40, arial_80B;
    public boolean showMessage = false;
    public String message = "";
    int messageCounter = 0;
    public int commandNum = 0;

    double playTime;
    DecimalFormat df = new DecimalFormat("#0.00");

    public UIController(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
    }

    public void showMessage(String text) {
        message = text;
        showMessage = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);
        if (gp.gameState == GameStates.INITIAL) {
            drawInitial();
        } else if (gp.gameState == GameStates.MAP) {
            //drawMap();
        } else if (gp.gameState == GameStates.MENU) {
            //drawMenu();
        } else if (gp.gameState == GameStates.ITEMLIST) {
            
        } else if (gp.gameState == GameStates.STATISTCS) {
            
        }
    }

    public void drawInitial() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        String text = "SPAKBOR HILL'S KOCAK!!!";
        int x = getCenterX(text);
        int y = gp.tileSize * 2;

        g2.drawString(text, x, y);

        // ini buat kalo mau nambahin gambar ya guys
        x = gp.screenWidth / 2 - (gp.tileSize*2) / 2;
        y += gp.tileSize * 2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        // ini buat kalo mau nambahin tulisan
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        text = "Start Game";
        x = getCenterX(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Enter Player Name";
        x = getCenterX(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Enter Farm Name";
        x = getCenterX(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
        }
    }

    public void drawMap() {
        String text = "KOCAK";
        
        int x = getCenterX(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public void drawMenu() {
        String text = "MENU";
        
        int x = getCenterX(text);
        int y = gp.screenHeight / 2;

        String subText = "Press Enter to continue";

        g2.drawString(text, x, y);
        g2.drawString(subText, x, y + 40);
    }

    public int getCenterX(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }
}

