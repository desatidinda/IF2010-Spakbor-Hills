package state;

import entity.NPC.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import main.GamePanel;
import main.GameStates;

public class MapState implements StateHandler, MouseListener {

    private final GamePanel gp;
    private int selectedTeleportIndex = 0;
    private final String[] teleportOptions = {"Mountain Lake", "Forest River", "Ocean", "Mayor Tadi House", "Caroline House", "Perry House", "Dasco House", "Emily House", "Abigail House"};
    private Font vt323;

    // ini button inventory yah
    private final int inventoryBtnW = 131;
    private final int inventoryBtnH = 69;
    private int inventoryBtnX() { return gp.screenWidth - inventoryBtnW - 20; }
    private int inventoryBtnY() { return gp.screenHeight - inventoryBtnH - 20; }

    public MapState(GamePanel gp) {
        this.gp = gp;
        gp.addMouseListener(this);
    }

    @Override
    public void update() {
        gp.player.update();
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

        if (gp.player.teleportMode) {
            int boxWidth = 400;
            int boxHeight = 60 + teleportOptions.length * 32;
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
        }
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
            }
        } else {
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
        if (location.equals("Mountain Lake") || location.equals("Forest River") || location.equals("Ocean")) {
            FishingState fishingState = (FishingState) gp.stateHandlers.get(GameStates.FISHING);
            fishingState.setFishingLocation(location);
            
            gp.gameState = GameStates.FISHING;
            gp.player.update();
            gp.player.teleportMode = false;
        } else if (location.endsWith("House")) {
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
            gp.stateHandlers.put(GameStates.NPC_HOUSE, new NPCHouseState(gp, npc));
            gp.gameState = GameStates.NPC_HOUSE;
            gp.player.teleportMode = false;
        }
    }    

    @Override
    public void mouseClicked(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if (gp.gameState == GameStates.MAP) {
            int btnX = inventoryBtnX();
            int btnY = inventoryBtnY();
            if (mx >= btnX && mx <= btnX + inventoryBtnW &&
                my >= btnY && my <= btnY + inventoryBtnH) {
                InventoryState.showInventory(gp.player);
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

