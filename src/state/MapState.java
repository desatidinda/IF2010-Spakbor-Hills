package state;

import entity.NPC.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.GamePanel;
import main.GameStates;
import main.GameClock;
import map.Point;
import map.Tile;
import map.TileType;
import entity.Player.Inventory;
import entity.Farm.ShippingBin;
import entity.Item.Item;
import entity.Item.Seeds;
import entity.Farm.Season;
import state.EndGameStatistics;

public class MapState implements StateHandler, MouseListener {

    private final GamePanel gp;
    private int selectedTeleportIndex = 0;
    private final String[] teleportOptions = {"Mountain Lake", "Forest River", "Ocean", "Mayor Tadi House", "Caroline House", "Perry House", "Dasco House", "Emily House", "Abigail House"};
    private Font vt323;
    private boolean showShippingBinPopup = false;
    private int interactedObjectIndex = 999;

    private boolean showTilePopup = false;
    private int selectedTileAction = 0;
    private final String[] tileActions = {"Tilling", "Recover Land", "Planting", "Watering", "Harvesting"};
    private int interactCol = -1, interactRow = -1;
    private int lastDayChecked = -1;

    private boolean showSeedPopup = false;
    private List<Item> availableSeeds = new ArrayList<>();
    private int selectedSeedIndex = 0;
    private int seedCol, seedRow;

    // ini button inventory yah
    private final int inventoryBtnW = 131;
    private final int inventoryBtnH = 69;
    private int inventoryBtnX() { return gp.screenWidth - inventoryBtnW - 20; }
    private int inventoryBtnY() { return gp.screenHeight - inventoryBtnH - 20; }

    //ini button menu
    private final int menuBtnW = 131;
    private final int menuBtnH = 69;
    private int menuBtnX() { return gp.screenWidth - menuBtnW - 20; }
    private int menuBtnY() { return gp.screenHeight - menuBtnH - 80; }

    public MapState(GamePanel gp) {
        this.gp = gp;
        gp.addMouseListener(this);
    }

    @Override
    public void update() {
        gp.player.update();
        showShippingBinPopup = false;
        interactedObjectIndex = 999;
        
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                String imagePath = gp.obj[i].getClass().getSimpleName(); // or however you identify shipping bin
                int objLeftX = gp.obj[i].worldX;
                int objRightX = gp.obj[i].worldX + gp.tileSize * gp.obj[i].widthInTiles;
                int objTopY = gp.obj[i].worldY;
                int objBottomY = gp.obj[i].worldY + gp.tileSize * gp.obj[i].heightInTiles;
                int playerX = gp.player.worldX + gp.player.solid.x;
                int playerY = gp.player.worldY + gp.player.solid.y;
                int playerRightX = playerX + gp.player.solid.width;
                int playerBottomY = playerY + gp.player.solid.height;
                int interactionRange = 24; 
                if (playerRightX >= objLeftX - interactionRange && 
                    playerX <= objRightX + interactionRange &&
                    playerBottomY >= objTopY - interactionRange && 
                    playerY <= objBottomY + interactionRange) {
                    if (gp.obj[i].getClass().getSimpleName().equals("ShippingBin")) {
                        showShippingBinPopup = true;
                        interactedObjectIndex = i;
                        break;
                    }
                }
            }

        }
        int currentDay = GameClock.getDay();
        if (currentDay != lastDayChecked) {
            boolean isRainy = (GameClock.getCurrentWeather() == entity.Farm.Weather.RAINY);
            gp.tileManager.incrementPlantAges();
            gp.tileManager.checkPlantsAtEndOfDay();
            gp.tileManager.resetWaterCountAndWateredMap(isRainy);
            lastDayChecked = currentDay;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        gp.tileManager.draw(g2);

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                gp.obj[i].draw(g2, gp);
            }
        }

        gp.player.draw(g2);
        gp.ui.draw(g2);

        if (showShippingBinPopup && interactedObjectIndex != 999) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(gp.screenWidth / 2 - 150, 50, 300, 40, 15, 15);
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
            String message = "Press SHIFT to use shipping bin";
            int textWidth = g2.getFontMetrics().stringWidth(message);
            int textX = gp.screenWidth / 2 - textWidth / 2;
            g2.drawString(message, textX, 75);
        }

        if (gp.player.teleportMode) {
            int boxWidth = 400;
            int boxHeight = 60 + teleportOptions.length * 32 + 60;
            int boxX = (gp.screenWidth - boxWidth) / 2;
            int boxY = (gp.screenHeight - boxHeight) / 2;

            gp.ui.drawPopupWindow(g2, boxX, boxY, boxWidth, boxHeight);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
            gp.ui.drawCenteredText(g2, "Choose Destination:", boxX, boxY + 38, boxWidth);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
            for (int i = 0; i < teleportOptions.length; i++) {
                int textY = boxY + 70 + i * 32;
                if (i == selectedTeleportIndex) {
                    g2.setColor(new Color(255, 215, 0)); 
                    gp.ui.drawCenteredText(g2, "> " + teleportOptions[i], boxX, textY, boxWidth);
                } else {
                    g2.setColor(Color.WHITE);
                    gp.ui.drawCenteredText(g2, teleportOptions[i], boxX, textY, boxWidth);
                }
            }
            g2.setColor(Color.WHITE);
            gp.ui.drawCenteredText(g2, "Press ENTER to teleport, ESC to cancel", boxX, boxY + boxHeight - 30, boxWidth);
        }

        if (showTilePopup) {
            int boxWidth = 320;
            int boxHeight = 60 + tileActions.length * 32 + 40;
            int boxX = (gp.screenWidth - boxWidth) / 2;
            int boxY = (gp.screenHeight - boxHeight) / 2;

            gp.ui.drawPopupWindow(g2, boxX, boxY, boxWidth, boxHeight);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
            gp.ui.drawCenteredText(g2, "Choose Action:", boxX, boxY + 38, boxWidth);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
            for (int i = 0; i < tileActions.length; i++) {
                int textY = boxY + 70 + i * 32;
                if (i == selectedTileAction) {
                    g2.setColor(new Color(255, 215, 0));
                    gp.ui.drawCenteredText(g2, "> " + tileActions[i], boxX, textY, boxWidth);
                } else {
                    g2.setColor(Color.WHITE);
                    gp.ui.drawCenteredText(g2, tileActions[i], boxX, textY, boxWidth);
                }
            }
            g2.setColor(Color.WHITE);
            gp.ui.drawCenteredText(g2, "ENTER: Select   ESC: Cancel", boxX, boxY + boxHeight - 20, boxWidth);
        }
        else {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16F));
            gp.ui.drawCenteredText(g2, "Press SPACE to interact with tiles",0, gp.screenHeight - 60, gp.screenWidth);
        }

        if (showSeedPopup) {
            int boxWidth = 320;
            int boxHeight = 60 + availableSeeds.size() * 32 + 40;
            int boxX = (gp.screenWidth - boxWidth) / 2;
            int boxY = (gp.screenHeight - boxHeight) / 2;

            gp.ui.drawPopupWindow(g2, boxX, boxY, boxWidth, boxHeight);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
            gp.ui.drawCenteredText(g2, "Choose Seeds:", boxX, boxY + 38, boxWidth);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
            for (int i = 0; i < availableSeeds.size(); i++) {
                int textY = boxY + 70 + i * 32;
                if (i == selectedSeedIndex) {
                    g2.setColor(new Color(255, 215, 0));
                    gp.ui.drawCenteredText(g2, "> " + availableSeeds.get(i).getItemName(), boxX, textY, boxWidth);
                } else {
                    g2.setColor(Color.WHITE);
                    gp.ui.drawCenteredText(g2, availableSeeds.get(i).getItemName(), boxX, textY, boxWidth);
                }
            }
            g2.setColor(Color.WHITE);
            gp.ui.drawCenteredText(g2, "ENTER: Pilih   ESC: Batal", boxX, boxY + boxHeight - 20, boxWidth);
            return;
        }

        gp.ui.drawPopupMessage(g2);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gp.player.teleportMode) {
            if (key == KeyEvent.VK_UP) {
                selectedTeleportIndex--;
                if (selectedTeleportIndex < 0) {
                    selectedTeleportIndex = teleportOptions.length - 1;
                }
            } else if (key == KeyEvent.VK_DOWN) {
                selectedTeleportIndex++;
                if (selectedTeleportIndex >= teleportOptions.length) {
                    selectedTeleportIndex = 0;
                }
            } else if (key == KeyEvent.VK_ENTER) {
                gp.player.savedWorldX = gp.player.worldX;
                gp.player.savedWorldY = gp.player.worldY;
                teleportPlayerTo(teleportOptions[selectedTeleportIndex]);
                //gp.player.teleportMode = false; 
            } else if (key == KeyEvent.VK_ESCAPE) {
                gp.player.teleportMode = false;
                gp.player.update();
            }
        } else if (key == KeyEvent.VK_SPACE && !showTilePopup) {
            Point steppedTile = gp.cm.getTileStepped(gp.player);
            int col = steppedTile.getX();
            int row = steppedTile.getY();
            int tileNum = gp.tileManager.getMapTileNum()[col][row];
            Tile tile = gp.tileManager.getTile()[tileNum];

            if (tile.getType() == TileType.TILLABLE || tile.getType() == TileType.TILLED || tile.getType() == TileType.PLANTED) {
                showTilePopup = true;
                selectedTileAction = 0;
                interactCol = col;
                interactRow = row;
            }
        } else if(showShippingBinPopup && interactedObjectIndex != 999 && key == KeyEvent.VK_SHIFT){
            if (showShippingBinPopup && interactedObjectIndex != 999 && key == KeyEvent.VK_SHIFT) {
                //System.out.println("Player is using the shipping bin!");
                if (gp.obj[interactedObjectIndex] instanceof ShippingBin) {
                    ShippingBin shippingBin = (ShippingBin) gp.obj[interactedObjectIndex];
                    shippingBin.sellInventory(gp.player);
                    //System.out.println("Shipping bin used successfully!");
                }
            }
        }else {
            if (key == KeyEvent.VK_W) {
                gp.keyHandler.upPressed = true;
            } else if (key == KeyEvent.VK_S) {
                gp.keyHandler.downPressed = true;
            } else if (key == KeyEvent.VK_A) {
                gp.keyHandler.leftPressed = true;
            } else if (key == KeyEvent.VK_D) {
                gp.keyHandler.rightPressed = true;
            }
        }

        if (showTilePopup) {
            if (key == KeyEvent.VK_UP) {
                selectedTileAction = (selectedTileAction - 1 + tileActions.length) % tileActions.length;
            } else if (key == KeyEvent.VK_DOWN) {
                selectedTileAction = (selectedTileAction + 1) % tileActions.length;
            } else if (key == KeyEvent.VK_ENTER) {
                switch (tileActions[selectedTileAction]) {
                    case "Tilling":
                        gp.tileManager.tillTile(interactCol, interactRow);
                        gp.player.performAction(5);
                        break;
                    case "Recover Land":
                        gp.tileManager.recoverTile(interactCol, interactRow);
                        gp.player.performAction(5);
                        break;
                    case "Planting":
                        Set<Item> seeds = gp.player.getInventory().getSeeds();
                        Season currentSeason = GameClock.getCurrentSeason();
                        Set<Item> plantableSeeds = new HashSet<>();
                        for (Item item : seeds) {
                            if (item instanceof Seeds && ((Seeds) item).getSeason() == currentSeason) {
                                plantableSeeds.add(item);
                            }
                        }
                        if (!plantableSeeds.isEmpty()) {
                            showSeedSelectionPopup(plantableSeeds, interactCol, interactRow);
                        } else {
                            gp.ui.showPopupMessage("You have no seeds to plant for this season.");
                        }
                        break;
                    case "Watering":
                        gp.tileManager.waterTile(interactCol, interactRow);
                        gp.player.performAction(5);
                        break;
                    case "Harvesting":
                        String plantedSeed = gp.tileManager.getPlantedSeedNameMap()[interactCol][interactRow];
                        int tileNum = gp.tileManager.getMapTileNum()[interactCol][interactRow];
                        if (gp.tileManager.getTile()[tileNum].getType() == TileType.PLANTED
                            && !gp.tileManager.getWateredMap()[interactCol][interactRow]) {
                            gp.ui.showPopupMessage("You must water the plant before harvesting!");
                        } else {
                            boolean success = gp.tileManager.harvestPlant(interactCol, interactRow, gp.player.getInventory(), plantedSeed);
                            if (success) {
                                gp.player.performAction(5);
                                gp.ui.showPopupMessage("Harvest successful!");
                                ((EndGameStatistics) gp.stateHandlers.get(GameStates.STATISTICS)).incrementCropsHarvested();
                            }
                        }
                        break;

                }
                showTilePopup = false;
            } else if (key == KeyEvent.VK_ESCAPE) {
                showTilePopup = false;
            }
            return;
        }

        if (showSeedPopup) {
            if (key == KeyEvent.VK_UP) {
                selectedSeedIndex = (selectedSeedIndex - 1 + availableSeeds.size()) % availableSeeds.size();
            } else if (key == KeyEvent.VK_DOWN) {
                selectedSeedIndex = (selectedSeedIndex + 1) % availableSeeds.size();
            } else if (key == KeyEvent.VK_ENTER) {
                Item selectedSeed = availableSeeds.get(selectedSeedIndex);
                int jumlah = gp.player.getInventory().getItemCount(selectedSeed);
                if (jumlah > 0) {
                    
                    gp.tileManager.plantSeed(seedCol, seedRow, selectedSeed);
                    gp.player.getInventory().removeItem(selectedSeed, 1);
                    gp.player.performAction(5);
                } else {
                    gp.ui.showPopupMessage("You ran out of " + selectedSeed.getItemName() + "!");
                }
                showSeedPopup = false;
                showTilePopup = false;

            } else if (key == KeyEvent.VK_ESCAPE) {
                showSeedPopup = false;
            }
            return;
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
        } else if (key == KeyEvent.VK_SPACE) {
            gp.keyHandler.spacePressed = false;
        }
    }

    public void teleportPlayerTo(String location) {
        if (location.equals("Pond")) {
            GameClock.setPaused(true);
            
            FishingState fishingState = (FishingState) gp.stateHandlers.get(GameStates.FISHING);
            fishingState.setFishingLocation(location);
            gp.gameState = GameStates.FISHING;
            gp.player.update();
            gp.player.teleportMode = false;
        } else if (location.equals("Mountain Lake") || location.equals("Forest River") || location.equals("Ocean")) {
            gp.player.performAction(10);
            GameClock.skipMinutes(15);
            GameClock.setPaused(true);

            FishingState fishingState = (FishingState) gp.stateHandlers.get(GameStates.FISHING);
            fishingState.setFishingLocation(location);
            gp.gameState = GameStates.FISHING;
            gp.player.update();
            gp.player.teleportMode = false;
        } else if (location.endsWith("House")) {
            gp.player.performAction(10); 
            GameClock.skipMinutes(15);

            gp.player.houseX = gp.screenWidth / 2 - (gp.tileSize / 2);
            gp.player.houseY = gp.screenHeight / 2 - (gp.tileSize / 2);
            // ini posisi kl indoor ya
            gp.player.worldX = gp.tileSize * 16;
            gp.player.worldY = gp.tileSize * 16;
            gp.player.solid.x = gp.player.houseX;
            gp.player.solid.y = gp.player.houseY;
            gp.player.update();

            NPC npc = null;
            switch (location) {
                case "Mayor Tadi House":
                    npc = gp.npc[0];
                    break;
                case "Caroline House":
                    npc = gp.npc[1];
                    break;
                case "Perry House":
                    npc = gp.npc[2];
                    break;
                case "Dasco House":
                    npc = gp.npc[3];
                    break;
                case "Emily House":
                    npc = gp.npc[4];
                    break;
                case "Abigail House":
                    npc = gp.npc[5];
                    break;
            }
            if (npc != null) {
                npc.incrementVisiting();
            }
            gp.stateHandlers.put(GameStates.NPC_HOUSE, new NPCHouseState(gp, npc));
            gp.gameState = GameStates.NPC_HOUSE;
            gp.player.teleportMode = false;
        }
    }

    private void showSeedSelectionPopup(Set<Item> seeds, int col, int row) {
        showSeedPopup = true;
        availableSeeds.clear();
        availableSeeds.addAll(seeds);
        selectedSeedIndex = 0;
        seedCol = col;
        seedRow = row;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if (gp.gameState == GameStates.MAP || gp.gameState == GameStates.NPC_HOUSE || gp.gameState == GameStates.INSIDE_HOUSE) {
            int btnX = inventoryBtnX();
            int btnY = inventoryBtnY();
            if (mx >= btnX && mx <= btnX + inventoryBtnW &&
                my >= btnY && my <= btnY + inventoryBtnH) {
                InventoryState.showInventory(gp.player);
            } else if (mx >= menuBtnX() && mx <= menuBtnX() + menuBtnW &&
                my >= menuBtnY() && my <= menuBtnY() + menuBtnH) {
                gp.gameState = GameStates.MENU;
            }
        }
    }
    @Override public void mousePressed(MouseEvent e) {

    }
    @Override public void mouseReleased(MouseEvent e) {

    }
    @Override public void mouseEntered(MouseEvent e) {

    }
    @Override public void mouseExited(MouseEvent e) {

    }
}

