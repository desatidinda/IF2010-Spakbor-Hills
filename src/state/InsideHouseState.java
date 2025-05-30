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
    private int fuelUsageLeft = 0;
    private Recipe pendingRecipe = null;
    private String pendingFuel = null;
    private String cookMessage = null;
    private int cookMessageTimer = 0;

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
            if (!showRecipeList) {
            interactedFurnitureIndex = furnitureIndex;
            showPopup = true;
            } else {
                showPopup = false;
                interactedFurnitureIndex = -1;
            }
        } else {
            showPopup = false;
            interactedFurnitureIndex = -1;
        }

        if (isCookingWait && pendingRecipe != null && pendingFuel != null) {
            int startTotal = cookingStartHour * 60 + cookingStartMinute;
            int nowTotal = GameClock.getHour() * 60 + GameClock.getMinute();

            if (nowTotal - startTotal >= 60 || nowTotal < startTotal) {
                gp.player.cook(pendingRecipe.getName(), pendingFuel, 1);
                Item cookedFood = ItemFactory.createItem(pendingRecipe.getName());
                gp.player.getInventory().addItem(cookedFood, 1);
                cookMessage = "Your " + pendingRecipe.getName() + " is ready!";
                cookMessageTimer = 180;

                isCookingWait = false;
                pendingRecipe = null;
                pendingFuel = null;
                showRecipeList = false;
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
            gp.ui.drawCenteredText(g2, "Now cooking " + pendingRecipe.getName(), windowX, windowY + height - 70, width);
        }

        if (!allRecipes.isEmpty()) {
            Recipe selected = allRecipes.get(selectedRecipeIndex);
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
            y += 10;
            g2.drawString("Ingredients:", windowX + 20, y);
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
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 14F));
        gp.ui.drawCenteredText(g2, "Press ENTER to cook, R to cancel", windowX, windowY + height - 20, width);
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
                if (!selected.isUnlocked()) {
                    cookMessage = "Recipe not yet learned!";
                    cookMessageTimer = 180;
                    return;
                }

                String fuel = null;
                Item Firewood = ItemFactory.createItem("Firewood");
                Item Coal = ItemFactory.createItem("Coal");
                if (gp.player.getInventory().hasItem(Firewood)) fuel = "Firewood";
                else if (gp.player.getInventory().hasItem(Coal)) fuel = "Coal";

                if (fuel == null) {
                    cookMessage = "You don't have fuel in your inventory!";
                    cookMessageTimer = 180;
                    return;
                }

                for (Map.Entry<String, Integer> entry : selected.getIngredients().entrySet()) {
                    Item item = ItemFactory.createItem(entry.getKey());
                    int qty = entry.getValue();
                    if (!gp.player.getInventory().hasItem(item, qty)) {
                        cookMessage = "Not enough " + item + " to cook " + selected.getName() + "!";
                        cookMessageTimer = 180;
                        return;
                    } 
                }
                
                for (Map.Entry<String, Integer> entry : selected.getIngredients().entrySet()) {
                    Item item = ItemFactory.createItem(entry.getKey());
                    int qty = entry.getValue();
                    gp.player.getInventory().removeItem(item, qty);
                }
                if (fuel.equals("Coal")) {
                    if (fuelUsageLeft <= 0) {
                        Item usedFuel = ItemFactory.createItem("Coal");
                        gp.player.getInventory().removeItem(usedFuel, 1);
                        fuelUsageLeft = 2;
                    }
                    fuelUsageLeft--;
                } else if (fuel.equals("Firewood")) {
                    Item usedFuel = ItemFactory.createItem("Firewood");
                    gp.player.getInventory().removeItem(usedFuel, 1);
                    fuelUsageLeft = 0;
                }

                if (gp.player.getEnergy() < 10) {
                    cookMessage = "You don't have enough energy!";
                    cookMessageTimer = 180;
                    return;
                }
                
                gp.player.performAction(10);
                isCookingWait = true;
                cookingStartHour = GameClock.getHour();
                cookingStartMinute = GameClock.getMinute();
                pendingRecipe = selected;
                pendingFuel = fuel;
                cookMessage = "Cooking " + selected.getName() + "... wait 1 hour for the dish to be ready..";
                cookMessageTimer = 180;
                showPopup = false;
            } else if (key == KeyEvent.VK_R || key == KeyEvent.VK_ESCAPE) {
                showRecipeList = false;
            }
        }
    }
}

    // public GameObject[] getFurniture() {
    //     return furniture;
    // }

