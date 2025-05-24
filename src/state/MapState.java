package state;

import main.GamePanel;
import main.GameStates;
import entity.NPC.*;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;

public class MapState implements StateHandler {

    private final GamePanel gp;
    private int selectedTeleportIndex = 0;
    private final String[] teleportOptions = {"Lake A", "Mayor Tadi House", "Caroline House", "Perry House", "Dasco House", "Emily House", "Abigail House"};
    private Font vt323;

    public MapState(GamePanel gp) {
        this.gp = gp;
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
            int boxWidth = 300;
            int boxHeight = 200;
            int boxX = (gp.screenWidth - boxWidth) / 2;
            int boxY = (gp.screenHeight - boxHeight) / 2;

            g2.setColor(new Color(0, 0, 0, 250));
            g2.fillRect(boxX, boxY, boxWidth, boxHeight);

            g2.setColor(Color.WHITE);
            g2.setFont(vt323);
            g2.drawString("Choose Destination:", boxX + 10, boxY + 30);

            for (int i = 0; i < teleportOptions.length; i++) {
                int textY = boxY + 60 + i * 30;
                if (i == selectedTeleportIndex) {
                    g2.setColor(Color.YELLOW);
                    g2.drawString(">", boxX + 10, textY);
                } else {
                    g2.setColor(Color.WHITE);
                }
                g2.drawString(teleportOptions[i], boxX + 40, textY);
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
        if (location.equals("Lake A")) {
            gp.gameState = GameStates.FISHING;
            gp.player.update();
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
        }
    }
}

