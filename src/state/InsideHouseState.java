package state;

import main.GamePanel;
import main.GameClock;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.Map;

import entity.House.KingBed;
import entity.House.Stove;
import entity.House.TV;
import entity.Farm.Weather;
import entity.House.*;
import entity.Item.*;
import entity.Player.Inventory;

public class InsideHouseState implements StateHandler {

    private final GamePanel gp;
    private BufferedImage image;
    private int interactedFurnitureIndex = -1;
    private boolean showPopup = false;
    private boolean watchTV = false;
    private Weather currentWeather;
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
    private Inventory inventory;
    private int cookProgressTimer = 0;
    private boolean showCookProgress = false;

    public InsideHouseState(GamePanel gp) {
        this.gp = gp;
        loadBackground();
        deployFurniture();
        this.inventory = gp.player.getInventory();
    }

    public void setShowRecipeList(boolean value) {
        this.showRecipeList = value;
    }

    protected void loadBackground() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/floor.png"));
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
            int startTotal = cookingStartHour * 60 + cookingStartMinute;
            int nowTotal = GameClock.getHour() * 60 + GameClock.getMinute();

            int elapsed = nowTotal >= startTotal
                    ? nowTotal - startTotal
                    : (24 * 60 - startTotal + nowTotal);

            if (elapsed >= 60) {
                inventory.addItem(pendingRecipe.getName(), 1);
                gp.ui.showMessage("Berhasil memasak " + pendingRecipe.getName() + "!");
                cookedItemReady = true;
                cookedItemName = pendingRecipe.getName();
                cookReadyMessageTimer = 180;

                isCookingWait = false;
                pendingRecipe = null;
                pendingFuel = null;
                showCookProgress = false;
            }
        }

        if (cookReadyMessageTimer > 0) {
            cookReadyMessageTimer--;
            if (cookReadyMessageTimer == 0) {
                cookReadyMessage = null;
            }
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

        if (showPopup && interactedFurnitureIndex >= 0 && interactedFurnitureIndex < gp.furniture.length && gp.furniture[interactedFurnitureIndex] != null) {
            gp.ui.drawPopupWindow(g2, gp.screenWidth / 2 - 400 / 2, 445, 400, 60);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
            g2.setColor(java.awt.Color.WHITE);
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

        if (cookedItemReady && cookedItemName != null && gp.furniture[1] != null) {
            int stoveX = gp.furniture[1].worldX;
            int stoveY = gp.furniture[1].worldY;

            String text = cookedItemName;
            FontMetrics fm = g2.getFontMetrics(g2.getFont().deriveFont(Font.BOLD, 12F));
            int textWidth = fm.stringWidth(text);
            int padding = 10;

            int barWidth = textWidth + padding * 2;
            int barHeight = 24;

            int stoveWidth = gp.tileSize * gp.furniture[1].widthInTiles;
            int x = stoveX + (stoveWidth - barWidth) / 2;
            int y = stoveY - barHeight - 10;

            g2.setColor(new Color(255, 215, 0));
            g2.fillRoundRect(x, y, barWidth, barHeight, 10, 10);

            g2.setColor(Color.BLACK);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12F));
            g2.drawString(text, x + padding, y + 16);
        }
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

        if (cookReadyMessage != null) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRoundRect(20, 20, 400, 40, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawString(cookReadyMessage, 30, 45);
        }

        if (showCookProgress && isCookingWait && pendingRecipe != null) {
            int start = cookingStartHour * 60 + cookingStartMinute;
            int now = GameClock.getHour() * 60 + GameClock.getMinute();
            int elapsed = (now >= start) ? (now - start) : (24 * 60 - start + now);
            int progress = Math.min(100, elapsed * 100 / 60);

            int barWidth = 200;
            int barHeight = 18;
            int x = windowX + (width - barWidth) / 2;
            int barY = windowY + height - 50;

            g2.setColor(new Color(60, 60, 60, 180));
            g2.fillRoundRect(x, y, barWidth, barHeight, 15, 15);

            g2.setColor(new Color(255, 165, 0));
            g2.fillRoundRect(x, y, progress * barWidth / 100, barHeight, 15, 15);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, barWidth, barHeight, 15, 15);
        }

        if (cookMessage != null && cookMessageTimer > 0) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
            g2.setColor(Color.RED);
            gp.ui.drawCenteredText(g2, cookMessage, windowX, windowY + height - 75, width);
            cookMessageTimer--;
        }

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 14F));
        gp.ui.drawCenteredText(g2, "↑↓ untuk navigasi, ENTER untuk masak, R untuk keluar", windowX, windowY + height - 20, width);
    }

    protected void deployFurniture() {
        KingBed kingbed = new KingBed();
        kingbed.worldX = gp.tileSize * 13 - 16;
        kingbed.worldY = 8;
        gp.furniture[0] = kingbed;


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
        } else if (key == KeyEvent.VK_E) {
            if (interactedFurnitureIndex == 1 && cookedItemReady) {
                cookedItemReady = false;
                cookedItemName = null;
            }
        } else if (key == KeyEvent.VK_SPACE) {
            gp.keyHandler.spacePressed = true;
        }

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

        if (key == KeyEvent.VK_W) {
            gp.keyHandler.upPressed = false;
        } else if (key == KeyEvent.VK_S) {
            gp.keyHandler.downPressed = false;
        } else if (key == KeyEvent.VK_A) {
            gp.keyHandler.leftPressed = false;
        } else if (key == KeyEvent.VK_D) {
            gp.keyHandler.rightPressed = false;
        } else if (key == KeyEvent.VK_SPACE) gp.keyHandler.spacePressed = false;

        if (showRecipeList) {
            if (key == KeyEvent.VK_UP) {
                selectedRecipeIndex = Math.max(0, selectedRecipeIndex - 1);
            } else if (key == KeyEvent.VK_DOWN) {
                selectedRecipeIndex = Math.min(RecipeRegistry.getAll().size() - 1, selectedRecipeIndex + 1);
            } else if (key == KeyEvent.VK_ENTER) {
                Recipe selected = RecipeRegistry.getAll().get(selectedRecipeIndex);
                if (isCookingWait) {
                    cookMessage = "Tunggu masakan sebelumnya selesai!";
                    cookMessageTimer = 3;
                    return;
                }
                if (cookedItemReady) {
                    cookMessage = "Ambil dulu masakan yang sudah jadi! Press E";
                    cookMessageTimer = 3;
                    return;
                }

                if (!selected.isUnlocked()) {
                    cookMessage = "Resep belum dipelajari!";
                    cookMessageTimer = 3;
                    return;
                }

                String fuel = null;
                if (gp.player.getInventory().hasItem("Firewood")) fuel = "Firewood";
                else if (gp.player.getInventory().hasItem("Coal")) fuel = "Coal";

                if (fuel == null) {
                    cookMessage = "Tidak ada fuel di inventory!";
                    cookMessageTimer = 3;
                    return;
                }

                for (Map.Entry<String, Integer> entry : selected.getIngredients().entrySet()) {
                    String item = entry.getKey();
                    int qty = entry.getValue();
                    if (!gp.player.getInventory().hasItem(item, qty)) {
                        cookMessage = "Bahan tidak cukup: " + item;
                        cookMessageTimer = 3;
                        return;
                    }
                }

                gp.player.performAction(10);
                isCookingWait = true;
                cookingStartHour = GameClock.getHour();
                cookingStartMinute = GameClock.getMinute();
                pendingRecipe = selected;
                pendingFuel = fuel;
                cookMessageTimer = 180;
                cookProgressTimer = 0;
                showCookProgress = true;
                cookMessage = null;

            } else if (key == KeyEvent.VK_R || key == KeyEvent.VK_ESCAPE) {
                showRecipeList = false;
            }
        }
    }
}

// public GameObject[] getFurniture() {
//     return furniture;
// }
