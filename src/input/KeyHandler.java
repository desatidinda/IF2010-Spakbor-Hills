package input;

import state.StateHandler;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private final GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        StateHandler handler = gp.stateHandlers.get(gp.gameState);
        if (handler != null) {
            handler.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        StateHandler handler = gp.stateHandlers.get(gp.gameState);
        if (handler != null) {
            handler.keyReleased(e);
        }
    }
}
