package state;

import main.GameClock;
import main.GamePanel;
import main.GameStates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class InitialState implements StateHandler {

    private final GamePanel gp;

    public InitialState(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void update() {
        // gadipake
    }

    @Override
    public void draw(Graphics2D g2) {
        gp.ui.draw(g2);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch(gp.ui.initialStep) {
            case 0:
                if (key == KeyEvent.VK_UP) {
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 2;
                    }
                } else if (key == KeyEvent.VK_DOWN) {
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 2) {
                        gp.ui.commandNum = 0;
                    }
                } else if (key == KeyEvent.VK_ENTER) {
                    if (gp.ui.commandNum == 0) {
                        gp.ui.initialStep = 1;
                        gp.ui.inputBuffer = "";
                    } else if (gp.ui.commandNum == 1) {
                        gp.ui.initialStep = 4;
                    } else if (gp.ui.commandNum == 2) {
                        System.exit(0);
                    }
                }
                break;
            case 1:
                if (key == KeyEvent.VK_ENTER) {
                    gp.ui.playerName = gp.ui.inputBuffer;
                    gp.ui.inputBuffer = "";
                    gp.ui.initialStep = 2;
                    gp.ui.commandNum = 0;
                } else if (key == KeyEvent.VK_BACK_SPACE) {
                    if (!gp.ui.inputBuffer.isEmpty()) {
                        gp.ui.inputBuffer = gp.ui.inputBuffer.substring(0, gp.ui.inputBuffer.length() - 1);
                    }
                } else {
                    char c = e.getKeyChar();
                    if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c)) {
                        gp.ui.inputBuffer += c;
                    }
                }
                break;
            case 2:
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
                    gp.ui.commandNum = 1 - gp.ui.commandNum;
                } else if (key == KeyEvent.VK_ENTER) {
                    gp.ui.gender = (gp.ui.commandNum == 0) ? "Male" : "Female";
                    gp.ui.initialStep = 3;
                    gp.ui.inputBuffer = "";
                }
                break;
            case 3:
                if (key == KeyEvent.VK_ENTER) {
                    gp.ui.farmName = gp.ui.inputBuffer;
                    gp.ui.inputBuffer = "";

                    gp.player.setName(gp.ui.playerName);
                    gp.player.setGender(gp.ui.gender);
                    gp.player.setFarmName(gp.ui.farmName);

                    gp.gameState = GameStates.MAP;
                    GameClock.init();
                    GameClock.startClock();
                } else if (key == KeyEvent.VK_BACK_SPACE) {
                    if (!gp.ui.inputBuffer.isEmpty()) {
                        gp.ui.inputBuffer = gp.ui.inputBuffer.substring(0, gp.ui.inputBuffer.length() - 1);
                    }
                } else {
                    char c = e.getKeyChar();
                    if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c)) {
                        gp.ui.inputBuffer += c;
                    }
                }
                break;
            case 4:
                if (key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_ENTER) {
                    gp.ui.initialStep = 0; // kembali ke menu awal
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // gadipake
    }
}
