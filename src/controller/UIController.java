package controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;


import main.GamePanel;
import main.GameStates;

public class UIController {
    GamePanel gp;
    Graphics2D  g2;
    Font bradrock, vt323;
    public boolean showMessage = false;
    public String message = "";
    int messageCounter = 0;
    public int commandNum = 0;

    public int initialStep = 0;
    public String inputBuffer = "";
    public String playerName = "";
    public String gender = "";
    public String farmName = "";

    double playTime;
    DecimalFormat df = new DecimalFormat("#0.00");
    BufferedImage gambar;

    public UIController(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream input = getClass().getResourceAsStream("/res/Font/Bradrock.ttf");
            bradrock = Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(Font.PLAIN, 40);
            input = getClass().getResourceAsStream("/res/Font/VT323-Regular.ttf");
            vt323 = Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(Font.PLAIN, 40);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String text) {
        message = text;
        showMessage = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(vt323);
        g2.setColor(Color.WHITE);
        if (gp.gameState == GameStates.INITIAL) {
            drawInitial();
        } else if (gp.gameState == GameStates.MAP) {
            drawMap();
        } else if (gp.gameState == GameStates.INSIDE_HOUSE) {
            
        } else if (gp.gameState == GameStates.MENU) {

        } else if (gp.gameState == GameStates.ITEMLIST) {
            
        } else if (gp.gameState == GameStates.STATISTCS) {
            
        }
    }
    
    public void drawInitial() {
        g2.setFont(vt323);
        if (initialStep == 0) {
            drawInitialMenu();
        } else if (initialStep == 1) {
            drawInputField("Enter Player Name: ", inputBuffer);
        } else if (initialStep == 2) {
            drawGenderSelection();
        } else if (initialStep == 3) {
            drawInputField("Enter Farm Name: ", inputBuffer);
        } else if (initialStep == 4) {
        drawInformation();
    }
    }

    private void drawInitialMenu() {
        // ini buat kalo mau nambahin gambar ya guys
        try {
            gambar = ImageIO.read(getClass().getResourceAsStream("/res/gambar.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2.drawImage(gambar, 0, 0, gp.screenWidth, gp.screenHeight, null);
        
        int x = gp.tileSize * 5;
        int y = gp.tileSize * 8 - 6;
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
        if (commandNum == 0) {
            g2.drawString(">", x, y);
        }

        if (commandNum == 1) {
            g2.drawString(">", x, y += gp.tileSize);
        }

        if (commandNum == 2) {
            g2.drawString(">", x, y += gp.tileSize * 2);
        }
    }

    private void drawInputField(String label, String input) {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        int y = gp.screenHeight / 2;

        String text = label + input + "_";
        int x = getCenterX(text);

        g2.drawString(text, x, y);
    }

    private void drawGenderSelection() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        int y = gp.screenHeight / 2;

        String[] options = {"Male", "Female"};
        for (int i = 0; i < options.length; i++) {
            int x = getCenterX(options[i]);
            g2.drawString(options[i], x, y + i * 40);
            if (commandNum == i) {
                g2.drawString(">", x - gp.tileSize, y + i * 40);
            }
        }
    }

    public void drawInformation() {
        try {
            gambar = ImageIO.read(getClass().getResourceAsStream("/res/gambar1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (gambar != null) {
            g2.drawImage(gambar, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
            g2.setColor(Color.RED);
            g2.drawString("Failed to load image!", 100, 100);
        }
    }


    public void drawMap() {
        String text = "Player's Energy: " + gp.player.getEnergy();
        
        int x = gp.tileSize;
        int y = gp.tileSize;
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
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

