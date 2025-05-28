package state;

import entity.Farm.Weather;
import main.GamePanel;
<<<<<<< HEAD
=======
import main.GameClock;

>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.Map;
<<<<<<< HEAD
import entity.House.*;
import entity.Item.*;
import entity.House.TV;
import main.GameClock;
=======

import entity.House.KingBed;
import entity.House.Stove;
import entity.House.TV;
import entity.Farm.Weather;
import entity.House.*;
import entity.Item.*;
>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c

public class InsideHouseState implements StateHandler {

    private final GamePanel gp;
    private BufferedImage image;
    private int interactedFurnitureIndex = -1;
    private boolean showPopup = false;
    private boolean showRecipeList = false;
    private int selectedRecipeIndex = 0;
    private boolean isCookingWait = false;
    private int cookingStartHour;
    private int cookingStartMinute;
    private Recipe pendingRecipe = null;
    private String pendingFuel = null;
    private boolean watchTV = false;
    private Weather currentWeather;
<<<<<<< HEAD
    private String cookMessage = null;
    private int cookMessageTimer = 0;
=======
    private boolean showRecipeList = false;
    private int selectedRecipeIndex = 0;
    private boolean isCookingWait = false;
    private int cookingStartHour;
    private int cookingStartMinute;
    private Recipe pendingRecipe = null;
    private String pendingFuel = null;
    private String cookMessage = null;
    private int cookMessageTimer = 0;
    private String cookReadyMessage = null;
    private int cookReadyMessageTimer = 0;
    private boolean cookedItemReady = false;
    private String cookedItemName = null;

>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c

    public InsideHouseState(GamePanel gp) {
        this.gp = gp;
        loadBackground();
        deployFurniture();
    }

    public void setShowRecipeList(boolean value) {
        this.showRecipeList = value;
    }

    protected void loadBackground() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/floor.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        gp.player.update();
        int furnitureIndex = gp.cm.checkIndoorObject(gp.player, true);
        if (furnitureIndex != -1) {
            interactedFurnitureIndex = furnitureIndex;
            showPopup = true;
        } else {
            showPopup = false;
            interactedFurnitureIndex = -1;
        }

        if (isCookingWait && pendingRecipe != null && pendingFuel != null) {
<<<<<<< HEAD
            int nowHour = GameClock.getHour();
            int nowMinute = GameClock.getMinute();
            int startTotal = cookingStartHour * 60 + cookingStartMinute;
            int nowTotal = nowHour * 60 + nowMinute;

            if (nowTotal - startTotal >= 60 || nowTotal < startTotal) {
                gp.player.cook(pendingRecipe.getName(), pendingFuel, 1);
                cookMessage = "Masakan siap: " + pendingRecipe.getName();
                cookMessageTimer = 180;
=======
            int startTotal = cookingStartHour * 60 + cookingStartMinute;
            int nowTotal = GameClock.getHour() * 60 + GameClock.getMinute();

            int elapsed = nowTotal >= startTotal
                    ? nowTotal - startTotal
                    : (24 * 60 - startTotal + nowTotal);

            if (elapsed >= 60) {
                gp.player.cook(pendingRecipe.getName(), pendingFuel, 1);
                cookedItemReady = true;
                cookedItemName = pendingRecipe.getName();
                cookReadyMessageTimer = 180;
>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c

                isCookingWait = false;
                pendingRecipe = null;
                pendingFuel = null;
                showRecipeList = false;
            }
        }
        if (cookReadyMessageTimer > 0) {
            cookReadyMessageTimer--;
            if (cookReadyMessageTimer == 0) {
                cookReadyMessage = null;
            }
        }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, 0, 0, gp.screenWidth, gp.screenHeight, null);

        for (int i = 0; i < gp.furniture.length; i++) {
            if (gp.furniture[i] != null) {
                g2.drawImage(gp.furniture[i].image, gp.furniture[i].worldX, gp.furniture[i].worldY,
                        gp.tileSize * gp.furniture[i].widthInTiles, gp.tileSize * gp.furniture[i].heightInTiles, null);
            }
        }

        gp.player.draw(g2);
        gp.ui.draw(g2);

<<<<<<< HEAD
        if (showPopup && interactedFurnitureIndex >= 0 && interactedFurnitureIndex < gp.furniture.length) {
=======
        if (showPopup && interactedFurnitureIndex >= 0 && interactedFurnitureIndex < gp.furniture.length && gp.furniture[interactedFurnitureIndex] != null) {
>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c
            gp.ui.drawPopupWindow(g2, gp.screenWidth / 2 - 400 / 2, 445, 400, 60);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
            g2.setColor(Color.WHITE);
            gp.ui.drawCenteredText(g2, "Press SPACE to interact with " + gp.furniture[interactedFurnitureIndex].getName(), 0, 480, gp.screenWidth);
        }

        if (watchTV) {
            BufferedImage weatherImg = null;
            if (currentWeather == Weather.SUNNY) {
                weatherImg = ((TV) gp.furniture[2]).getTvsunny();
            } else if (currentWeather == Weather.RAINY) {
                weatherImg = ((TV) gp.furniture[2]).getTvrainy();
            }
            g2.drawImage(weatherImg, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }

        if (showRecipeList) {
            drawRecipeList(g2);
        }
<<<<<<< HEAD
=======

        if (cookedItemReady && cookedItemName != null && gp.furniture[1] != null) {
            int x = gp.furniture[1].worldX + gp.tileSize / 4;
            int y = gp.furniture[1].worldY - 20;

            g2.setColor(new Color(255, 215, 0));
            g2.fillRoundRect(x, y, gp.tileSize, 20, 10, 10);

            g2.setColor(Color.BLACK);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12F));
            g2.drawString("🍲 " + cookedItemName, x + 5, y + 15);
        }
>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c
    }

    private void drawRecipeList(Graphics2D g2) {
        int width = 500;
        int height = 400;
        int windowX = gp.screenWidth / 2 - width / 2;
        int windowY = gp.screenHeight / 2 - height / 2;

        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRoundRect(windowX, windowY, width, height, 35, 35);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(windowX + 5, windowY + 5, width - 10, height - 10, 25, 25);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));
        gp.ui.drawCenteredText(g2, "All Recipes", windowX, windowY + 40, width);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16F));
        List<Recipe> allRecipes = RecipeRegistry.getAll();
        int y = windowY + 80;
        int lineHeight = 20;

        for (int i = 0; i < allRecipes.size(); i++) {
            Recipe recipe = allRecipes.get(i);

            if (y + lineHeight > windowY + height - 60) break;

            if (i == selectedRecipeIndex) {
                g2.setColor(new Color(255, 255, 100, 180));
                g2.fillRoundRect(windowX + 10, y - 16, width - 20, lineHeight + 4, 10, 10);
            }

            g2.setColor(recipe.isUnlocked() ? Color.WHITE : Color.GRAY);
            g2.drawString("\u2022 " + recipe.getName(), windowX + 20, y);
            y += lineHeight;
        }

        if (isCookingWait && pendingRecipe != null) {
            g2.setColor(Color.ORANGE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
            gp.ui.drawCenteredText(g2, "Sedang memasak " + pendingRecipe.getName(), windowX, windowY + height - 70, width);
        }

        if (!allRecipes.isEmpty()) {
            Recipe selected = allRecipes.get(selectedRecipeIndex);
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
            y += 10;
            g2.drawString("Bahan:", windowX + 20, y);
            y += 18;
            for (Map.Entry<String, Integer> entry : selected.getIngredients().entrySet()) {
                g2.drawString("- " + entry.getValue() + "x " + entry.getKey(), windowX + 40, y);
                y += 16;
            }
        }

        if (cookMessage != null && cookMessageTimer > 0) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
            g2.setColor(Color.GREEN);
            gp.ui.drawCenteredText(g2, cookMessage, windowX, windowY + height - 45, width);
            cookMessageTimer--;
        }

<<<<<<< HEAD
=======
        if (cookReadyMessage != null) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRoundRect(20, 20, 400, 40, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawString(cookReadyMessage, 30, 45);
        }

>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 14F));
        gp.ui.drawCenteredText(g2, "↑↓ untuk navigasi, ENTER untuk masak, R untuk keluar", windowX, windowY + height - 20, width);
    }

    protected void deployFurniture() {
        KingBed kingbed = new KingBed();
        kingbed.worldX = gp.tileSize * 13 - 16;
        kingbed.worldY = 8;
        gp.furniture[0] = kingbed;

<<<<<<< HEAD
=======

>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c
        Stove stove = new Stove(gp);
        stove.worldX = 0;
        stove.worldY = gp.tileSize * 10 - 16;
        gp.furniture[1] = stove;

        TV tv = new TV();
        tv.worldX = gp.tileSize * 9 - 20;
        tv.worldY = 0;
        gp.furniture[2] = tv;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            gp.keyHandler.upPressed = true;
        } else if (key == KeyEvent.VK_S) {
            gp.keyHandler.downPressed = true;
        } else if (key == KeyEvent.VK_A) {
            gp.keyHandler.leftPressed = true;
        } else if (key == KeyEvent.VK_D) {
            gp.keyHandler.rightPressed = true;
        } else if (key == KeyEvent.VK_ESCAPE) {
            gp.player.teleportOut();
        } else if (key == KeyEvent.VK_SPACE) {
            gp.keyHandler.spacePressed = true;
<<<<<<< HEAD
        } else if (key == KeyEvent.VK_R) showRecipeList = !showRecipeList;

=======
            if (interactedFurnitureIndex == 1 && cookedItemReady) {
                cookedItemReady = false;
                cookedItemName = null;
                gp.ui.showMessage("Kamu mengambil masakan dari kompor.");
            }
        }
>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c

        if (showPopup && interactedFurnitureIndex != 999 && key == KeyEvent.VK_SPACE) {
            if (gp.furniture[interactedFurnitureIndex] instanceof TV) {
                watchTV = true;
                currentWeather = GameClock.getCurrentWeather();
            }
            gp.furniture[interactedFurnitureIndex].playerInteract(gp.player);
            showPopup = false;
        }

        if (watchTV && key == KeyEvent.VK_ENTER) {
            watchTV = false;
            currentWeather = null;
        }

        else if (key == KeyEvent.VK_R) {
            showRecipeList = !showRecipeList;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

<<<<<<< HEAD
        if (key == KeyEvent.VK_W) gp.keyHandler.upPressed = false;
        else if (key == KeyEvent.VK_S) gp.keyHandler.downPressed = false;
        else if (key == KeyEvent.VK_A) gp.keyHandler.leftPressed = false;
        else if (key == KeyEvent.VK_D) gp.keyHandler.rightPressed = false;
        else if (key == KeyEvent.VK_SPACE) gp.keyHandler.spacePressed = false;
=======
        if (key == KeyEvent.VK_W) {
            gp.keyHandler.upPressed = false;
        } else if (key == KeyEvent.VK_S) {
            gp.keyHandler.downPressed = false;
        } else if (key == KeyEvent.VK_A) {
            gp.keyHandler.leftPressed = false;
        } else if (key == KeyEvent.VK_D) {
            gp.keyHandler.rightPressed = false;
        } else if (key == KeyEvent.VK_SPACE) gp.keyHandler.spacePressed = false;
>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c

        if (showRecipeList) {
            if (key == KeyEvent.VK_UP) {
                selectedRecipeIndex = Math.max(0, selectedRecipeIndex - 1);
            } else if (key == KeyEvent.VK_DOWN) {
                selectedRecipeIndex = Math.min(RecipeRegistry.getAll().size() - 1, selectedRecipeIndex + 1);
            } else if (key == KeyEvent.VK_ENTER) {
<<<<<<< HEAD
=======
                if (isCookingWait) {
                    gp.ui.showMessage("Tunggu masakan sebelumnya selesai!");
                    return;
                }
>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c
                Recipe selected = RecipeRegistry.getAll().get(selectedRecipeIndex);
                if (!selected.isUnlocked()) {
                    cookMessage = "Resep belum dipelajari!";
                    cookMessageTimer = 180;
                    return;
                }

                String fuel = null;
                if (gp.player.getInventory().hasItem("Firewood")) fuel = "Firewood";
                else if (gp.player.getInventory().hasItem("Coal")) fuel = "Coal";

                if (fuel == null) {
                    cookMessage = "Tidak ada fuel di inventory!";
                    cookMessageTimer = 180;
                    return;
                }

                for (Map.Entry<String, Integer> entry : selected.getIngredients().entrySet()) {
                    String item = entry.getKey();
                    int qty = entry.getValue();
                    if (!gp.player.getInventory().hasItem(item, qty)) {
                        cookMessage = "Bahan tidak cukup: " + item;
                        cookMessageTimer = 180;
                        return;
                    }
                }

                isCookingWait = true;
                cookingStartHour = GameClock.getHour();
                cookingStartMinute = GameClock.getMinute();
                pendingRecipe = selected;
                pendingFuel = fuel;
<<<<<<< HEAD
                cookMessage = "Memasak " + selected.getName() + "... tunggu 1 jam in-game.";
=======
                cookMessage = "Memasak " + selected.getName() + "... tunggu 1 jam.";
>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c
                cookMessageTimer = 180;
            } else if (key == KeyEvent.VK_R || key == KeyEvent.VK_ESCAPE) {
                showRecipeList = false;
            }
        }
    }
<<<<<<< HEAD
}
=======
}

    // public GameObject[] getFurniture() {
    //     return furniture;
    // }

>>>>>>> 9652e6987ac23aab301315679424d80606e12c0c
