package state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public interface StateHandler {
    void update();
    void draw(Graphics2D g2);
    void keyPressed(KeyEvent e);
    void keyReleased(KeyEvent e);
}