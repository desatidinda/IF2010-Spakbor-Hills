package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import main.GameStates;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;
    public boolean enterPressed, escapePressed, shiftPressed, controlPressed, altPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gp.gameState == GameStates.INITIAL) {
            if (key == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            }
            else if (key == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0; 
                }
            }
            else if (key == KeyEvent.VK_ENTER) {
                enterPressed = true;
                if (gp.ui.commandNum == 0) {
                    gp.gameState = GameStates.MAP;
                }
                else if (gp.ui.commandNum == 1) {
                    
                }
                else if (gp.ui.commandNum == 2) {
                    
                }
            }
        }

        if (gp.gameState == GameStates.MAP) {
            if (key == KeyEvent.VK_W) {
                upPressed = true;
            } else if (key == KeyEvent.VK_S) {
                downPressed = true;
            } else if (key == KeyEvent.VK_A) {
                leftPressed = true;
            } else if (key == KeyEvent.VK_D) {
                rightPressed = true;
            } else if (key == KeyEvent.VK_SPACE) {
                spacePressed = true;
                if (gp.gameState == GameStates.MAP) {
                    gp.gameState = GameStates.MENU;
                    // System.out.println(gp.gameState);
                }
                else if (gp.gameState == GameStates.MENU) {
                    gp.gameState = GameStates.MAP;
                    // System.out.println(gp.gameState);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            upPressed = false;
        } else if (key == KeyEvent.VK_S) {
            downPressed = false;
        } else if (key == KeyEvent.VK_A) {
            leftPressed = false;
        } else if (key == KeyEvent.VK_D) {
            rightPressed = false;
        } else if (key == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
    }
    
}
