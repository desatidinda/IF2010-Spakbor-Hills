package controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;
import main.GameClock;
import main.GameObserver;
import main.GamePanel;
import main.GameStates;
import entity.NPC.NPC;
import state.NPCHouseState;
import state.StateHandler;

public class UIController implements GameObserver{
    GamePanel gp;
    Graphics2D  g2;
    Font bradrock, vt323;
    public boolean showMessage = false;
    public String message = "";
    int messageCounter = 0;
    public int commandNum = 0;

    private String popupMessage = null;
    private long popupMessageTime = 0;
    private static final long popupDuration = 2000;

    private String recipeUnlockMessage = null;
    private long recipeUnlockTime = 0;
    private static final long recipeUnlockDuration = 2000;

    private boolean showFadeEffect = false;
    private int fadeAlpha = 0;
    private String fadeMessage = "";
    private boolean fadeIn = true;
    private static final int fadeSpeed = 30;
    private boolean pendingSleepSkip = false;
    private boolean pendingPingsanSkip = false;

    public int initialStep = 0;
    public String inputBuffer = "";
    public String playerName = "";
    public String gender = "";
    public String farmName = "";


    double playTime;
    DecimalFormat df = new DecimalFormat("#0.00");
    BufferedImage gambar, bgName, bgGender, bgFarmGirl, bgFarmBoy;
    private BufferedImage informationImage; 
    

    public UIController(GamePanel gp) {
        this.gp = gp;
        
        try {
            InputStream input = getClass().getResourceAsStream("/res/Font/Bradrock.ttf");
            bradrock = Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(Font.PLAIN, 40);
            input = getClass().getResourceAsStream("/res/Font/VT323-Regular.ttf");
            vt323 = Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(Font.PLAIN, 40);

            bgName = ImageIO.read(getClass().getResourceAsStream("/res/entername.png"));
            bgGender = ImageIO.read(getClass().getResourceAsStream("/res/choosegender.png"));
            bgFarmGirl = ImageIO.read(getClass().getResourceAsStream("/res/farmnamegirl.png"));
            bgFarmBoy = ImageIO.read(getClass().getResourceAsStream("/res/farmnameboy.png"));
            informationImage = ImageIO.read(getClass().getResourceAsStream("/res/information.png"));

        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayerExhausted() {
        showPopupMessage("Warning: Your energy is low!");
    }

    @Override
    public void onSeasonChanged() {
        showPopupMessage("Season changed to " + GameClock.getCurrentSeason() + "!");
    }

    public void showRecipeUnlockMessage(String text) {
        this.recipeUnlockMessage = text;
        this.recipeUnlockTime = GameClock.getGameTime();
    }

    public void showMessage(String text) {
        message = text;
        showMessage = true;
    }

    public void showPopupMessage(String text) {
        popupMessage = text;
        popupMessageTime = GameClock.getGameTime();
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(vt323);
        g2.setColor(Color.WHITE);
        if (gp.gameState == GameStates.INITIAL) {
            drawInitial();
        } else if (gp.gameState == GameStates.MAP) {
            drawMap();
        } else if (gp.gameState == GameStates.INSIDE_HOUSE || gp.gameState == GameStates.NPC_HOUSE) {
            drawMap();
        } else if (gp.gameState == GameStates.FISHING) {
            drawPopupMessage(g2);
        }
        if (recipeUnlockMessage != null) {
            long elapsed = GameClock.getGameTime() - recipeUnlockTime;
            if (elapsed < recipeUnlockDuration) {
                int x = 30;
                int y = gp.screenHeight - 60;
                int w = 280;
                int h = 30;

                drawPopupWindow(g2, x, y, w, h);

                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 13F));
                g2.setColor(Color.WHITE);

                drawCenteredText(g2, recipeUnlockMessage, x, y + h / 2 + 4, w);
            } else {
                recipeUnlockMessage = null;
            }
        }

        if (showFadeEffect) {
            drawFadeEffect(g2);
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

    public void drawPopupMessage(Graphics2D g2) {
        if (popupMessage != null) {
            int w = 400, h = 50;
            int x = (gp.screenWidth - w) / 2;
            int y = gp.screenHeight - 150;

            drawPopupWindow(g2, x, y, w, h);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
            g2.setColor(Color.WHITE);
            drawCenteredText(g2, popupMessage, x, y + h/2 + 4, w);

            if (GameClock.getGameTime() - popupMessageTime > popupDuration) {
                popupMessage = null;
            }
        }
    }
    
    public void drawInitial() {
        g2.setFont(vt323);
        if (initialStep == 0) {
            drawInitialMenu();
        } else if (initialStep == 1) {
            drawInputField(inputBuffer, bgName);
        } else if (initialStep == 2) {
            drawGenderSelection(bgGender);
        } else if (initialStep == 3) {
            if (gender.equals("Male")) {
                drawInputField(inputBuffer, bgFarmBoy);
            } else if (gender.equals("Female")) {
                drawInputField(inputBuffer, bgFarmGirl);
            }
        } else if (initialStep == 4) {
            drawInformation(g2);
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
        
        String[] menuOptions = {"Start New Game", "Information", "Quit Game"};
        int x = gp.tileSize * 4;
        int y = gp.tileSize * 8 - 6;

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
        for (int i = 0; i < menuOptions.length; i++) {
            gp.ui.drawCenteredText(g2, menuOptions[i], 0, y + i * gp.tileSize, gp.screenWidth);
            if (commandNum == i) {
                g2.drawString(">", x, y + i * gp.tileSize);
            }
        }
    }

    private void drawInputField(String input, BufferedImage bgImage) {
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
        int y = gp.screenHeight / 2 + 40;
        String text = input + "_";
        drawCenteredText(g2, text, 0, y, gp.screenWidth);
    }

    private void drawGenderSelection(BufferedImage bgImage) {
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        int y = gp.screenHeight / 2 + 50;
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

    public void drawInformation(Graphics2D g2) {
        if (informationImage != null) {
            g2.drawImage(informationImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
            g2.setColor(Color.RED);
            g2.drawString("Failed to load information image!", 100, 100);
        }
    }


    public void drawMap() {
        
        int boxWidth = 180;
        int boxHeight = 72;
        int boxX = 16;
        int boxY = 16;

        gp.ui.drawPopupWindow(g2, boxX, boxY, boxWidth, boxHeight);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
        g2.setColor(Color.WHITE);

        int textX = boxX + 16;
        int textY = boxY + 24;
        int lineHeight = 16;

        g2.drawString("Time: " + GameClock.getFormattedTime(), textX, textY);
        g2.drawString("Season: " + GameClock.getCurrentSeason(), textX, textY + lineHeight);
        g2.drawString("Day: " + GameClock.getDay(), textX, textY + lineHeight * 2);

        // ini energy bar
        int barWidth = 100;
        int barHeight = 12;
        int barBoxWidth = barWidth + 80;
        int barBoxHeight = 64;

        gp.ui.drawPopupWindow(g2, boxX, boxY + boxHeight + 8, barBoxWidth, barBoxHeight);

        int energy = gp.player != null ? gp.player.getEnergy() : 0;
        int maxEnergy = 100;
        int filledWidth = (int) ((double) energy / maxEnergy * barWidth);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12F));
        g2.setColor(Color.WHITE);
        g2.drawString("Energy", boxX + 16, boxY + boxHeight + 30);

        int barX = boxX + 20;
        int barY = boxY +  boxHeight + 44;
        g2.setColor(Color.GRAY);
        g2.fillRoundRect(barX, barY, barWidth, barHeight, 5, 5);
        g2.setColor(new Color(50, 220, 80));
        g2.fillRoundRect(barX, barY, filledWidth, barHeight, 5, 5);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(barX, barY, barWidth, barHeight, 5, 5);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 10F));
        g2.drawString(energy + " / " + maxEnergy, barX + barWidth + 8, barY + barHeight - 4);
        
        // ini button inventory yah
        int btnW = 131;
        int btnH = 69;
        int btnX = gp.screenWidth - btnW - 20;
        int btnY = gp.screenHeight - btnH - 20;
        try {
            BufferedImage btnImg = ImageIO.read(getClass().getResourceAsStream("/res/buttonInventory.png"));
            g2.drawImage(btnImg, btnX, btnY, btnW, btnH, null);
        } catch (IOException e) {
            
        }

        // ini button menu yyyy
        int btnMenuW = 131;
        int btnMenuH = 69;
        int btnMenuX = gp.screenWidth - btnMenuW - 20;
        int btnMenuY = gp.screenHeight - btnMenuH - 80;
        try {
            BufferedImage btnMenuImg = ImageIO.read(getClass().getResourceAsStream("/res/buttonmenu.png"));
            g2.drawImage(btnMenuImg, btnMenuX, btnMenuY, btnMenuW, btnMenuH, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ini ada di npc house doang
        String npcName = null;
        NPC currentNPC = null;
        if (gp.gameState == GameStates.NPC_HOUSE) {
            StateHandler handler = gp.stateHandlers.get(GameStates.NPC_HOUSE);
            if (handler instanceof NPCHouseState) {
                NPCHouseState npcState = (NPCHouseState) handler;
                currentNPC = npcState.getNpcInHouse();
                if (currentNPC != null) {
                    npcName = currentNPC.getName();
                }
            }
        }
        if (npcName != null) {
            int houseBoxWidth = barBoxWidth;
            int houseBoxHeight = 40;
            int houseBoxX = boxX;
            int houseBoxY = boxY + boxHeight + barBoxHeight + 16;
            gp.ui.drawPopupWindow(g2, houseBoxX, houseBoxY, houseBoxWidth, houseBoxHeight);
            
            if (currentNPC != null) {
                try {
                    BufferedImage heartImg = ImageIO.read(getClass().getResourceAsStream("/res/heartpoint.png"));
                    int heartImgSize = 20;
                    String houseText2 = "Heart Points: " + currentNPC.getHeartPoints();
                    int startX = houseBoxX + 15;
                    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12F));
                    g2.setColor(Color.WHITE);
                    
                    int heartY = houseBoxY + 10;
                    g2.drawImage(heartImg, startX, heartY, heartImgSize, heartImgSize, null);
                    g2.drawString(houseText2, startX + heartImgSize + 10, houseBoxY + (houseBoxHeight / 2) + 3);
                } catch (IOException e) {
    
                }
            }
            drawPopupMessage(g2);
        }
    }

    public void startSleepFade() {
        String message = "Good morning, " + gp.player.getName() + "!";
        startFadeEffect(message);
        pendingSleepSkip = true;
    }

    public void startPingsanFade(boolean isLowEnergy) {
        String message;
        if (isLowEnergy) {
            message = "You collapsed from exhaustion!";
        } else {
            message = "You stayed up too late!";
        }
        startFadeEffect(message);
        pendingPingsanSkip = true;
    }

    private void startFadeEffect(String message) {
        this.fadeMessage = message;
        this.showFadeEffect = true;
        this.fadeAlpha = 0;
        this.fadeIn = true;
    }

    private void drawFadeEffect(Graphics2D g2) {
        if (fadeIn) {
            fadeAlpha += fadeSpeed;
            if (fadeAlpha >= 255) {
                fadeAlpha = 255;
                fadeIn = false;
            
                if (pendingSleepSkip) {
                    GameClock.skipToMorning();
                    pendingSleepSkip = false;
                } else if (pendingPingsanSkip) {
                    if (GameClock.isPingsan() || GameClock.getHour() < 6) {
                        GameClock.skipUntilMorning();
                    } else {
                        GameClock.skipToMorning();
                    }
                    pendingPingsanSkip = false;
                }
            }
        } else {
            fadeAlpha -= fadeSpeed;
            if (fadeAlpha <= 0) {
                fadeAlpha = 0;
                showFadeEffect = false;
                fadeMessage = "";
            }
        }
        
        g2.setColor(new Color(0, 0, 0, fadeAlpha));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        if (fadeAlpha >= 200 && !fadeMessage.isEmpty()) {
            g2.setFont(vt323.deriveFont(Font.BOLD, 48F));
            g2.setColor(new Color(255, 255, 255, fadeAlpha));
            drawCenteredText(g2, fadeMessage, 0, gp.screenHeight / 2, gp.screenWidth);
        }
    }
}

