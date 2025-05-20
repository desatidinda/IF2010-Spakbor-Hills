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
                            gp.gameState = GameStates.INFORMATION;
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
            } 
            // else if (key == KeyEvent.VK_SPACE) {
            //     spacePressed = true;
            //     if (gp.gameState == GameStates.MAP) {
            //         gp.gameState = GameStates.MENU;
            //         // System.out.println(gp.gameState);
            //     }
            //     else if (gp.gameState == GameStates.MENU) {
            //         gp.gameState = GameStates.MAP;
            //         // System.out.println(gp.gameState);
            //     }
            // }
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
