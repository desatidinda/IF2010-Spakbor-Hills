package state;

import entity.Item.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import java.util.Queue;
import java.util.LinkedList;

import main.GameClock;
import main.GamePanel;
import main.GameStates;

public class FishingState implements StateHandler {

    private final GamePanel gp;
    private BufferedImage image;
    private String currentLocation = "Pond";
    private boolean showFishInfo = false;
    private boolean showFishingGame = false;
    private boolean showInteractPopup = true;
    private boolean showChoicePopup = false;
    private List<Fish> availableFish;
    private int targetNumber, playerGuess = 50;
    private int minRange = 1, maxRange = 100;
    private int attempts = 0, maxAttempts = 10;
    private boolean fishCaught = false, gameOver = false;
    private String resultMessage = "";
    private Random random = new Random();
    private final int WINDOW_WIDTH = 500;
    private final int WINDOW_HEIGHT = 350;
    private Fish targetFish;
    private boolean recipeSashimiShown = false;
    private boolean recipeLegendaryShown = false;
    private boolean recipeFuguShown = false;
    private long recipePopupTime = 0;
    private String recipePopupMessage = null;
    private static final long RECIPE_POPUP_DURATION = 2000;
    private final Queue<String> recipePopupQueue = new LinkedList<>();


    public FishingState(GamePanel gp) {
        this.gp = gp;
        loadBackground();

    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    private void loadBackground() {


        try {
            String backgroundPath = switch (currentLocation) {
                case "Mountain Lake" -> "/res/mountainLake.png";
                case "Forest River" -> "/res/forestRiver.png";
                case "Ocean" -> "/res/ocean.png";
                case "Pond" -> "/res/pond.png";
                default -> "/res/pond.png";
            };
            image = ImageIO.read(getClass().getResourceAsStream(backgroundPath));
            // kalo misal gambarnya ga kedetect pake default
            // if (image == null) {
            //     image = ImageIO.read(getClass().getResourceAsStream("/res/seaside.jpg"));
            // }
        } catch (Exception e) {
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/res/seaside.jpg"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setFishingLocation(String location) {
        this.currentLocation = location;
        loadBackground();
        showInteractPopup = true;
    }

    private void startFishing() {
        if (gp.player.getEnergy() <= 0) {
            gp.ui.showMessage("Not enough energy to fish! You need at least some energy.");
            return;
        }

        showFishInfo = true;
        showFishingGame = false;
        availableFish = FishData.ALL_FISH.stream().filter(f -> f.getLocations().contains(currentLocation)).toList();
    }

    private void startFishingGame() {
        if (gp.player.getEnergy() <= 0) {
            gp.ui.showMessage("Not enough energy to fish! You need at least some energy.");
            showFishInfo = false;
            showFishingGame = false;
            return;
        }
        showFishInfo = false;
        showFishingGame = true;

        targetFish = getRandomFishByLocation(currentLocation);
        if (targetFish != null) {
            maxRange = getMaxRange(targetFish.getType());
            maxAttempts = getMaxAttempts(targetFish.getType());
            targetNumber = random.nextInt(maxRange) + 1;
            playerGuess = maxRange / 2;
            minRange = 1;
            attempts = 0;
            fishCaught = false;
            gameOver = false;
            resultMessage = "";
        } else {
            showFishingGame = false;
            showFishInfo = false;
            gp.ui.showMessage("No fish available here!");
        }
    }

    private void processGuess() {
        attempts++;
        if (playerGuess == targetNumber) {
            fishCaught = true;
            gameOver = true;
            catchFish();
        } else if (attempts >= maxAttempts) {
            gameOver = true;
            resultMessage = "The fish got away!";
            gp.player.performAction(5);
        } else {
            if (playerGuess < targetNumber) {
                minRange = playerGuess + 1;
                resultMessage = "Higher! (" + (maxAttempts - attempts) + " attempts left)";
            } else {
                maxRange = playerGuess - 1;
                resultMessage = "Lower! (" + (maxAttempts - attempts) + " attempts left)";
            }
            playerGuess = (minRange + maxRange) / 2;
        }
    }

    private void drawRecipePopup(Graphics2D g2) {
        if (recipePopupMessage == null && !recipePopupQueue.isEmpty()) {
            recipePopupMessage = recipePopupQueue.poll();
            recipePopupTime = GameClock.getGameTime();
        }

        if (recipePopupMessage != null) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(recipePopupMessage);
            int padding = 20;

            int w = textWidth + padding * 2;
            int h = 50;
            int x = 40;
            int y = gp.screenHeight - h - 40;

            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRoundRect(x, y, w, h, 15, 15);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, w, h, 15, 15);

            g2.drawString(recipePopupMessage, x + padding, y + 30);

            if (GameClock.getGameTime() - recipePopupTime > RECIPE_POPUP_DURATION) {
                recipePopupMessage = null;
            }
        }
    }

    private void catchFish() {
        if (targetFish == null) {
            resultMessage = "No fish available here!";
            return;
        }

        gp.player.performAction(5);

        Item fishItem = ItemFactory.createItem(targetFish.getName());
        gp.player.getInventory().addItem(fishItem, 1);
        resultMessage = "You caught a " + targetFish.getName() + "!";

        if (RecipeUnlocker.checkFishUnlock(gp.player.getInventory())) {
            // Recipe unlock logic
        }

        String unlocked = RecipeUnlocker.checkItemUnlock(fishItem.getItemName());
        if (unlocked != null) {
            // Item unlock logic
        }

        if (targetFish.getName().equals("Pufferfish") && !recipeFuguShown) {
            recipePopupQueue.offer("Fugu Recipe unlocked!");
            recipeFuguShown = true;
        }

        if (targetFish.getName().equals("Legend") && !recipeLegendaryShown) {
            recipePopupQueue.offer("The Legends of Spakbor Recipe unlocked!");
            recipeLegendaryShown = true;
        }

        int total = RecipeUnlocker.getTotalFishCount(gp.player.getInventory());
        if (total >= 10 && !recipeSashimiShown) {
            recipePopupQueue.offer("Sashimi Recipe unlocked!");
            recipeSashimiShown = true;
        }

        ((EndGameStatistics) gp.stateHandlers.get(GameStates.STATISTICS)).updateFishStatistics(targetFish.getType());
        ((EndGameStatistics) gp.stateHandlers.get(GameStates.STATISTICS)).incrementTotalFishCaught();
    }

    @Override
    public void update() {
        if (GameClock.isPingsan()) {
            gp.player.pingsan();
            gp.ui.showPopupMessage("It's past midnight! You will be taken to your house.");
        }

        gp.player.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        loadBackground();
        g2.drawImage(image, 0, 0, gp.screenWidth, gp.screenHeight, null);
        gp.player.draw(g2);
        gp.ui.draw(g2);
        if (showFishInfo) drawFishInfo(g2);
        if (showFishingGame) drawFishingGame(g2);

        int popupWidth = 400;
        int popupHeight = showChoicePopup ? 180 : 80;
        int popupX = gp.screenWidth / 2 - popupWidth / 2;
        int popupY = gp.screenHeight - popupHeight - 40;
        if (showInteractPopup && !showChoicePopup) {
            gp.ui.drawPopupWindow(g2, popupX, popupY, popupWidth, popupHeight);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
            
            if (gp.player.getEnergy() <= 0) {
                g2.setColor(Color.RED);
                gp.ui.drawCenteredText(g2, "Not enough energy to fish! (Need some energy)", popupX, popupY + 45, popupWidth);
            } else {
                g2.setColor(Color.WHITE);
                gp.ui.drawCenteredText(g2, "Press SPACE to fishing", popupX, popupY + 45, popupWidth);
            }
        }
        drawRecipePopup(g2);
    }
    
    public static int getMaxAttempts(FishType fishType) {
        return fishType == FishType.LEGENDARY ? 7 : 10;
    }

    public static int getMaxRange(FishType fishType) {
        return switch (fishType) {
            case COMMON -> 10;
            case REGULAR -> 100;
            case LEGENDARY -> 500;
        };
    }

    public static Fish getRandomFishByLocation(String location) {
        List<Fish> availableFish = FishData.ALL_FISH.stream().filter(f -> f.getLocations().contains(location)).toList();
        
        if (availableFish.isEmpty()) {
            return null;
        }
        
        return availableFish.get(new Random().nextInt(availableFish.size()));
    }

    //######################### GUI SEBELUM TEBAK ANGKA DIMULAI #########################
    private void drawFishInfo(Graphics2D g2) {
        int windowX = gp.screenWidth/2 - WINDOW_WIDTH/2;
        int windowY = gp.screenHeight/2 - WINDOW_HEIGHT/2;
        drawPopupWindow(g2, windowX, windowY);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        String text = "Fishing at " + currentLocation;
        drawCenteredText(g2, text, windowX, windowY + 50, WINDOW_WIDTH);
        drawConditionsSection(g2, windowX, windowY);
        drawAvailableFishSection(g2, windowX, windowY);
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
        text = "Press ENTER to start fishing or ESC to cancel";
        drawCenteredText(g2, text, windowX, windowY + WINDOW_HEIGHT - 30, WINDOW_WIDTH);
    }
    
    private void drawConditionsSection(Graphics2D g2, int windowX, int windowY) {
        int textX = windowX + 30;
        int textY = windowY + 90;
        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.drawString("Current Conditions:", textX, textY);

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
        
        g2.drawString("Weather: " + GameClock.getCurrentWeather().toString(), textX + 20, textY + 25);
        g2.drawString("Season: " + GameClock.getCurrentSeason().toString(), textX + 20, textY + 50);
        g2.drawString("Time: " + GameClock.getFormattedTime(), textX + 20, textY + 75);
    }
    
    private void drawAvailableFishSection(Graphics2D g2, int windowX, int windowY) {
        int textX = windowX + 30;
        int textY = windowY + 200;
        
        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.drawString("Fish Available:", textX, textY);
        
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
        
        int fishY = textY + 25;
        int availableHeight = (windowY + WINDOW_HEIGHT - 50) - fishY;
        int maxRowsPerColumn = availableHeight / 25;
        
        if (availableFish.isEmpty()) {
            g2.drawString("No fish available at this time and location", textX + 20, fishY);
        } else {
            int totalFish = availableFish.size(); // jadi si viewnya itu bentuknya kolom dan galebih dari 3 kolom
            int numColumns = (int) Math.ceil((double) totalFish / maxRowsPerColumn);
            numColumns = Math.max(1, Math.min(numColumns, 3)); 
            int columnWidth = (WINDOW_WIDTH - 60) / numColumns;
            int fishPerColumn = (int) Math.ceil((double) totalFish / numColumns);
            
            for (int i = 0; i < totalFish; i++) {
                int column = i / fishPerColumn;
                int row = i % fishPerColumn;
                
                g2.drawString("- " + availableFish.get(i).getName(), 
                             textX + 20 + (column * columnWidth), 
                             fishY + (row * 25));
            }
        }
    }
    
    private void drawFishingGame(Graphics2D g2) {
        int windowX = gp.screenWidth/2 - WINDOW_WIDTH/2;
        int windowY = gp.screenHeight/2 - WINDOW_HEIGHT/2;
        
        drawPopupWindow(g2, windowX, windowY);
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        drawCenteredText(g2, "Fishing at " + currentLocation, windowX, windowY + 50, WINDOW_WIDTH);
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        
        if (!gameOver) {
            drawGameInterface(g2, windowX, windowY);
        } else {
            drawGameOverScreen(g2, windowX, windowY);
        }
    }

    //######################### GUI SAAT TEBAK ANGKA DIMULAI #########################
    private void drawGameInterface(Graphics2D g2, int windowX, int windowY) {
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 16F));
        g2.setColor(new Color(200, 200, 255));
        drawCenteredText(g2, "Trying to catch: " + (targetFish != null ? targetFish.getType().toString() : "Unknown") + " fish", 
                      windowX, windowY + 80, WINDOW_WIDTH);
        g2.setColor(Color.WHITE);
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        drawCenteredText(g2, "Current guess: " + playerGuess, windowX, windowY + 110, WINDOW_WIDTH);
        drawCenteredText(g2, "Range: " + minRange + " - " + maxRange, windowX, windowY + 140, WINDOW_WIDTH);
        drawCenteredText(g2, "Attempts: " + attempts + "/" + maxAttempts, windowX, windowY + 170, WINDOW_WIDTH);
    
        if (!resultMessage.isEmpty()) {
            g2.setColor(Color.YELLOW);
            drawCenteredText(g2, resultMessage, windowX, windowY + 210, WINDOW_WIDTH);
            g2.setColor(Color.WHITE);
        }
        
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
        drawCenteredText(g2, "Use LEFT/RIGHT to adjust guess, ENTER to submit", windowX, windowY + 240, WINDOW_WIDTH);
        drawGuessSlider(g2, windowX, windowY);
    }
    
    private void drawGuessSlider(Graphics2D g2, int windowX, int windowY) { //ini GUI buat yang geser-geser/slider 
        int sliderWidth = 300;
        int sliderX = windowX + (WINDOW_WIDTH - sliderWidth) / 2;
        int sliderY = windowY + 280;
        int sliderHeight = 20;
        
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(sliderX, sliderY, sliderWidth, sliderHeight);
        
        int markerPosition = sliderX + (int)(((double)playerGuess - 1) / maxRange * sliderWidth);
        g2.setColor(Color.YELLOW);
        g2.fillRect(markerPosition - 5, sliderY - 5, 10, sliderHeight + 10);
    }
    
    //######################### GUI SAAT TEBAK ANGKA SELESAI #########################
    private void drawGameOverScreen(Graphics2D g2, int windowX, int windowY) {
        g2.setColor(fishCaught ? Color.GREEN : Color.RED);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        drawCenteredText(g2, resultMessage, windowX, windowY + 150, WINDOW_WIDTH);
        
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        drawCenteredText(g2, "The fish's number was: " + targetNumber, windowX, windowY + 190, WINDOW_WIDTH);
        
        if (fishCaught) {
            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
            drawCenteredText(g2, "Check your inventory to see what you caught!", 
                        windowX, windowY + 230, WINDOW_WIDTH);
        }
        
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
        drawCenteredText(g2, "Press ESC to exit or SPACE to try again", windowX, windowY + 270, WINDOW_WIDTH);
    }

    //######################### GUI SETTING BACKGROUND POP UP #########################
    private void drawPopupWindow(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRoundRect(x, y, WINDOW_WIDTH, WINDOW_HEIGHT, 35, 35);
        
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, WINDOW_WIDTH-10, WINDOW_HEIGHT-10, 25, 25);
    }
    
    private void drawCenteredText(Graphics2D g2, String text, int startX, int y, int width) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = startX + width/2 - length/2;
        g2.drawString(text, x, y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (showFishInfo) {
            handleFishInfoControls(key);
        } else if (showFishingGame) {
            handleFishingGameControls(key);
        } else {
            handleMovementControls(key);
        }
    }
    
    private void handleFishInfoControls(int key) {
        if (key == KeyEvent.VK_ENTER) {
            startFishingGame();
        } else if (key == KeyEvent.VK_ESCAPE) {
            showFishInfo = false;
            showInteractPopup = true;
        }
    }
    
    private void handleFishingGameControls(int key) {
        if (!gameOver) {
            if (key == KeyEvent.VK_LEFT) {
                playerGuess = Math.max(minRange, playerGuess - 1);
            } else if (key == KeyEvent.VK_RIGHT) {
                playerGuess = Math.min(maxRange, playerGuess + 1);
            } else if (key == KeyEvent.VK_ENTER) {
                processGuess();
            } else if (key == KeyEvent.VK_ESCAPE) {
                showFishingGame = false;
            }
        } else {
            if (key == KeyEvent.VK_ESCAPE) {
                showFishingGame = false;
                showInteractPopup = true;
            } else if (key == KeyEvent.VK_SPACE) {
                startFishing();
            }
        }
    }
    
    private void handleMovementControls(int key) {
        if (key == KeyEvent.VK_W) {
            gp.keyHandler.upPressed = true;
        } else if (key == KeyEvent.VK_S) {
            gp.keyHandler.downPressed = true;
        } else if (key == KeyEvent.VK_A) {
            gp.keyHandler.leftPressed = true;
        } else if (key == KeyEvent.VK_D) {
            gp.keyHandler.rightPressed = true;
        } else if (key == KeyEvent.VK_SPACE) {
            showInteractPopup = false;
            startFishing();
        } else if (key == KeyEvent.VK_ESCAPE) {
            gp.player.teleportOut();
            gp.keyHandler.spacePressed = true;
            showInteractPopup = false;
            GameClock.setPaused(false);

            if (gp.player.getEnergy() <= 0) {
                gp.ui.showMessage("Not enough energy to fish! You need at least some energy.");
                return;
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) gp.keyHandler.upPressed = false;
        else if (key == KeyEvent.VK_S) gp.keyHandler.downPressed = false;
        else if (key == KeyEvent.VK_A) gp.keyHandler.leftPressed = false;
        else if (key == KeyEvent.VK_D) gp.keyHandler.rightPressed = false;
        else if (key == KeyEvent.VK_SPACE) {
            gp.keyHandler.spacePressed = false;
            // if (!showFishInfo && !showFishingGame) {
            //     showInteractPopup = true;
            // }
        }
        else if (key == KeyEvent.VK_ESCAPE) {
            if (showFishInfo || showFishingGame) {
                showFishInfo = false;
                showFishingGame = false;
                showInteractPopup = true;
            }
        }
    }

}
