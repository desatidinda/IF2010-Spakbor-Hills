package state;

import entity.Item.*;
import main.GamePanel;
import main.GameStates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class ItemListState<T extends Item> implements StateHandler {
    private GamePanel gp;
    private final Font vt323;
    private BufferedImage background;
    private int scrollOffset = 0;
    
    private List<T> currentItems;
    private String categoryName;
    private boolean showCategorySelector = true;

    public ItemListState(GamePanel gp) {
        this.gp = gp;
        this.vt323 = new Font("VT323", Font.PLAIN, 24);
        getBackgroundImage();
    }

    @SuppressWarnings("unchecked")
    public void loadItemsByType(Class<? extends Item> itemType) {
        if (itemType == Seeds.class) {
            this.currentItems = (List<T>) new ArrayList<>(SeedsData.ALL_SEEDS);
            this.categoryName = "SEEDS";
        } else if (itemType == Crops.class) {
            this.currentItems = (List<T>) new ArrayList<>(CropsData.ALL_CROPS);
            this.categoryName = "CROPS";
        } else if (itemType == Fish.class) {
            this.currentItems = (List<T>) new ArrayList<>(FishData.ALL_FISH);
            this.categoryName = "FISH";
        } else if (itemType == Food.class) {
            this.currentItems = (List<T>) new ArrayList<>(FoodData.ALL_FOOD);
            this.categoryName = "FOOD";
        } else if (itemType == Misc.class) {
            this.currentItems = (List<T>) new ArrayList<>(MiscData.ALL_MISC);
            this.categoryName = "MISC";
        }
        this.scrollOffset = 0;
        this.showCategorySelector = false;
    }

    public void getBackgroundImage() {
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/res/menu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        // gadipake
    }

    @Override
    public void draw(Graphics2D g2) {
        if (background != null) {
            g2.drawImage(background, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
        
        if (showCategorySelector) {
            drawCategorySelector(g2);
        } else {
            drawItemList(g2);
        }
    }

    private void drawCategorySelector(Graphics2D g2) {
        try {
            BufferedImage itemListImage = ImageIO.read(getClass().getResourceAsStream("/res/itemlist.png"));
            if (itemListImage != null) {
                g2.drawImage(itemListImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawItemList(Graphics2D g2) {
        List<String> displayLines = displayLine();
        int totalLines = displayLines.size();
        
        int boxWidth = 600;
        int lineHeight = 28;
        int boxHeight = 450;
        int maxVisibleLines = 12;

        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;
        
        g2.setColor(new Color(0, 0, 0, 180));
        gp.ui.drawPopupWindow(g2, boxX, boxY, boxWidth, boxHeight);
        
        g2.setFont(vt323.deriveFont(Font.BOLD, 24F));
        g2.setColor(Color.WHITE);
        gp.ui.drawCenteredText(g2, categoryName + " CATALOG", boxX, boxY + 38, boxWidth);
        
        g2.setFont(vt323.deriveFont(Font.PLAIN, 12F));
        gp.ui.drawCenteredText(g2, "Press C to change category", boxX, boxY + 55, boxWidth);
        
        g2.setFont(vt323.deriveFont(Font.PLAIN, 16F));
        int y = boxY + 80;

        int end = Math.min(scrollOffset + maxVisibleLines, totalLines);
        for (int i = scrollOffset; i < end; i++) {
            if (y < boxY + boxHeight - 40) { 
                g2.drawString(displayLines.get(i), boxX + 40, y);
                y += lineHeight;
            }
        }
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(vt323.deriveFont(Font.PLAIN, 12F));
        gp.ui.drawCenteredText(g2, "Press ESC to go back, UP/DOWN to scroll", boxX, boxY + boxHeight - 20, boxWidth);
    }

    private List<String> displayLine() {
        List<String> lines = new ArrayList<>();
        
        if (currentItems != null) {
            int itemNumber = 1;
            for (T item : currentItems) {
                List<String> itemLines = formatItemDetails(item, itemNumber);
                lines.addAll(itemLines);
                lines.add("");
                itemNumber++;
            }
        }
        
        return lines;
    }

    private <U extends Item> List<String> formatItemDetails(U item, int number) {
        List<String> lines = new ArrayList<>();
        
        if (item instanceof Seeds) {
            Seeds seed = (Seeds) item;
            lines.add(String.format("%d. %s", number, seed.getItemName().toUpperCase()));
            lines.add("    - Buy Price: $" + seed.getBuyPrice().intValue());
            lines.add("    - Season: " + seed.getSeason());
            lines.add("    - Growth Time: " + seed.getDaysToHarvest() + " days");
            
        } else if (item instanceof Crops) {
            Crops crop = (Crops) item;
            lines.add(String.format("%d. %s", number, crop.getItemName().toUpperCase()));
            lines.add("    - Sell Price: $" + (int)crop.getSellPrice());
            lines.add("    - Season: " + crop.getSeason());
            lines.add("    - Growth Time: " + crop.getdayToHarvest() + " days");
            
        } else if (item instanceof Fish) {
            Fish fish = (Fish) item;
            lines.add(String.format("%d. %s", number, fish.getName().toUpperCase()));
            lines.add("    - Type: " + fish.getType());
            lines.add("    - Seasons: " + fish.getSeasons());
            lines.add("    - Time: " + fish.getTimeRange());
            lines.add("    - Weather: " + fish.getWeathers());
            lines.add("    - Sell Price: $" + fish.getType().getBasePrice());
            
        } else if (item instanceof Food) {
            Food food = (Food) item;
            lines.add(String.format("%d. %s", number, food.getItemName().toUpperCase()));
            lines.add("    - Energy Restored: +" + food.getEnergyRestored());
            lines.add("    - Buy Price: $" + Math.round(food.getBuyPrice()));
            lines.add("    - Sell Price: $" + Math.round(food.getSellPrice()));
            lines.add("    - Type: Consumable");
            
        } else if (item instanceof Coal) {
            Coal coal = (Coal) item;
            lines.add(String.format("%d. %s", number, "COAL"));
            lines.add("    - Buy Price: $" + (int)coal.getBuyPrice());
            lines.add("    - Sell Price: $" + (int)coal.getSellPrice());
            lines.add("    - Fuel Efficiency: 2x burn time");
            lines.add("    - Type: Fuel");
            
        } else if (item instanceof Firewood) {
            Firewood firewood = (Firewood) item;
            lines.add(String.format("%d. %s", number, "FIREWOOD"));
            lines.add("    - Buy Price: $" + (int)firewood.getBuyPrice());
            lines.add("    - Sell Price: $" + (int)firewood.getSellPrice());
            lines.add("    - Fuel Efficiency: 1x burn time");
            lines.add("    - Type: Fuel");
            
        } else {
            lines.add(String.format("%d. %s", number, item.getItemName().toUpperCase()));
            lines.add("    - Type: Misc Item");
            lines.add("    - Description: General purpose item");
        }
        
        return lines;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (showCategorySelector) {
            // select category
            if (e.getKeyCode() == KeyEvent.VK_1) {
                loadItemsByType(Seeds.class);
            } else if (e.getKeyCode() == KeyEvent.VK_2) {
                loadItemsByType(Crops.class);
            } else if (e.getKeyCode() == KeyEvent.VK_3) {
                loadItemsByType(Fish.class);
            } else if (e.getKeyCode() == KeyEvent.VK_4) {
                loadItemsByType(Food.class);
            } else if (e.getKeyCode() == KeyEvent.VK_5) {
                loadItemsByType(Misc.class);
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                gp.gameState = GameStates.MENU;
            }
        } else {
            List<String> lines = displayLine();
            int totalLines = lines.size();
            int maxVisibleLines = 12;
            
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (scrollOffset > 0) scrollOffset--;
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (scrollOffset < Math.max(0, totalLines - maxVisibleLines)) scrollOffset++;
            } else if (e.getKeyCode() == KeyEvent.VK_C) {
                showCategorySelector = true;
                currentItems = null;
                scrollOffset = 0;
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                gp.gameState = GameStates.MENU;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // gadipake
    }
}