package state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.Color;

import main.GamePanel;
import entity.NPC.*;

public class NPCHouseState extends InsideHouseState {
    private final GamePanel gp;
    private NPC npcInHouse;
    private boolean showInteractPopup = false;
    private boolean showChoicePopup = false;
    private boolean showDialogPopup = false;
    private String dialogText = "";
    private long dialogStartTime = 0;
    private final long dialogDuration = 2000;
    private int selectedChoice = 0;
    private final String[] choices = {"Chat", "Give Gift", "Cancel"};

    public NPCHouseState(GamePanel gp, NPC npc) {
        super(gp);
        this.gp = gp;
        this.npcInHouse = npc;
        npcInHouse.worldX = gp.player.houseX + gp.tileSize * 2 - 16;
        npcInHouse.worldY = gp.player.houseY - gp.tileSize * 3;
        super.loadBackground();
        super.deployFurniture();
    }

    @Override
    public void update() {
        gp.player.update();

        gp.player.collision = false;
        gp.cm.checkNPCCollision(gp.player, npcInHouse);
        showInteractPopup = gp.player.collision;

        if (showDialogPopup && System.currentTimeMillis() - dialogStartTime > dialogDuration) {
            showDialogPopup = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        if (npcInHouse != null) {
            npcInHouse.draw(g2);
        }

        int popupWidth = 400;
        int popupHeight = showChoicePopup ? 180 : 80;
        int popupX = gp.screenWidth / 2 - popupWidth / 2;
        int popupY = gp.screenHeight - popupHeight - 40;
        
        if (showDialogPopup) {
            int dialogWidth = 400;
            int dialogHeight = 80;
            int dialogX = gp.screenWidth / 2 - dialogWidth / 2;
            int dialogY = gp.screenHeight/2;
            gp.ui.drawPopupWindow(g2, dialogX, dialogY, dialogWidth, dialogHeight);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
            g2.setColor(Color.WHITE);
            gp.ui.drawCenteredText(g2, npcInHouse.getName() + " says:", dialogX, dialogY + 30, dialogWidth);
            gp.ui.drawCenteredText(g2, dialogText, dialogX, dialogY + 50, dialogWidth);
        } 
        else if (showChoicePopup) {
            gp.ui.drawPopupWindow(g2, popupX, popupY, popupWidth, popupHeight);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
            g2.setColor(java.awt.Color.WHITE);
            gp.ui.drawCenteredText(g2, "Choose interaction:", popupX, popupY + 35, popupWidth);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
            for (int i = 0; i < choices.length; i++) {
                int y = popupY + 70 + i * 28;
                if (i == selectedChoice) {
                    g2.setColor(new java.awt.Color(255, 215, 0)); // gold/yellow highlight
                    gp.ui.drawCenteredText(g2, "> " + choices[i], popupX, y, popupWidth);
                } else {
                    g2.setColor(java.awt.Color.WHITE);
                    gp.ui.drawCenteredText(g2, choices[i], popupX, y, popupWidth);
                }
            }
        }
        else if (showInteractPopup) {
            gp.ui.drawPopupWindow(g2, popupX, popupY, popupWidth, popupHeight);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
            g2.setColor(java.awt.Color.WHITE);
            gp.ui.drawCenteredText(g2, "Press SPACE to interact with " + npcInHouse.getName(), popupX, popupY + 45, popupWidth);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

    if (showInteractPopup && !showChoicePopup && key == KeyEvent.VK_SPACE) {
        showChoicePopup = true;
        selectedChoice = 0;
    } else if (showChoicePopup) {
        if (key == KeyEvent.VK_UP) {
            selectedChoice = (selectedChoice + choices.length - 1) % choices.length;
        } else if (key == KeyEvent.VK_DOWN) {
            selectedChoice = (selectedChoice + 1) % choices.length;
        } else if (key == KeyEvent.VK_ENTER) {
            if (choices[selectedChoice].equals("Chat")) {
                dialogText = npcInHouse.chat();
                gp.player.chatWith(npcInHouse);
                showChoicePopup = false;
                showDialogPopup = true;
                // TODO: ini nnt ganti kl udah bener time threadny
                dialogStartTime = System.currentTimeMillis();
            } else if (choices[selectedChoice].equals("Give Gift")) {
                gp.player.giveGift(npcInHouse, "NamaItem"); 
            }
        } else if (key == KeyEvent.VK_ESCAPE || choices[selectedChoice].equals("Cancel")) {
            showChoicePopup = false;
        }
    } else {
        // movement, teleport, dll
        super.keyPressed(e);
    }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
    }

    public NPC getNpcInHouse() {
        return npcInHouse;
    }
}
