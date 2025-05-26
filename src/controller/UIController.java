package controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;


import main.GamePanel;
import main.GameStates;
import main.GameClock;

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

    public void drawPopupWindow(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRoundRect(x, y, width, height, 25, 25);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x + 3, y + 3, width - 6, height - 6, 18, 18);
    }

    public void drawCenteredText(Graphics2D g2, String text, int startX, int y, int width) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = startX + width / 2 - length / 2;
        g2.drawString(text, x, y);
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
        drawCenteredText(g2, text, 0, y, gp.screenWidth);
    }

    private void drawGenderSelection() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        int y = gp.screenHeight / 2;

        String[] options = {"Male", "Female"};
        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            int optionWidth = g2.getFontMetrics().stringWidth(option);
            int centerX = gp.screenWidth / 2 - optionWidth / 2;
            int optionY = y + i * 40;

            if (commandNum == i) {
                g2.drawString(">", centerX - 30, optionY);
            }
            g2.drawString(option, centerX, optionY);
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
        
        int boxWidth = 180;
        int boxHeight = 96;
        int boxX = 16;
        int boxY = 16;

        gp.ui.drawPopupWindow(g2, boxX, boxY, boxWidth, boxHeight);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.setColor(Color.WHITE);

        int textX = boxX + 16;
        int textY = boxY + 30;
        int lineHeight = 24;

        g2.drawString("Time: " + GameClock.getFormattedTime(), textX, textY);
        g2.drawString("Season: " + GameClock.getCurrentSeason(), textX, textY + lineHeight);
        g2.drawString("Day: " + GameClock.getDay(), textX, textY + lineHeight * 2);

        // ini energy bar
        int barWidth = 150;
        int barHeight = 16;
        int barMargin = 20;
        int barBoxWidth = barWidth + 90;
        int barBoxHeight = 72;
        int barBoxX = gp.screenWidth - barBoxWidth - barMargin;
        int barBoxY = barMargin;

        gp.ui.drawPopupWindow(g2, barBoxX, barBoxY, barBoxWidth, barBoxHeight);

        int energy = gp.player != null ? gp.player.getEnergy() : 0;
        int maxEnergy = 100;
        int filledWidth = (int) ((double) energy / maxEnergy * barWidth);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18F));
        g2.setColor(Color.WHITE);
        g2.drawString("Energy", barBoxX + 20, barBoxY + 26);

        int barX = barBoxX + 20;
        int barY = barBoxY + 36;
        g2.setColor(Color.GRAY);
        g2.fillRoundRect(barX, barY, barWidth, barHeight, 10, 10);
        g2.setColor(new Color(50, 220, 80));
        g2.fillRoundRect(barX, barY, filledWidth, barHeight, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(barX, barY, barWidth, barHeight, 10, 10);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
        g2.drawString(energy + " / " + maxEnergy, barX + barWidth + 8, barY + barHeight - 4);
    }

    public void drawMenu() {
        String text = "MENU";

        int y = gp.screenHeight / 2;
        drawCenteredText(g2, text, 0, y, gp.screenWidth);

        String subText = "Press Enter to continue";

        drawCenteredText(g2, subText, 0, y + 40, gp.screenWidth);
    }
}

