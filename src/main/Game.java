package main;

import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Spakbor Hills");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(800, 600); 

        GamePanel gamePanel = new GamePanel(); 
        frame.add(gamePanel); 
        frame.pack();

        frame.setLocation(100, 100);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        gamePanel.setupGame();
        gamePanel.startGameThread(); 
    }
}
