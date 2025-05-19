package main;

import entity.Player.Player;
import objects.GameObject;
import controller.CollisionManager;
import controller.ObjectSetter;
// import controller.TileManager;
// import controller.ActionManager;
// import map.Point;    
import input.KeyHandler;
import controller.TileManager;
import controller.UIController;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // private Game game;

    final int originalTileSize = 16;
    final int scale = 4;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768
    public final int screenHeight = tileSize * maxScreenRow; //576

    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int worldWidth = tileSize * maxWorldCol; //1024
    public final int worldHeight = tileSize * maxWorldRow; //768

    public TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);

    Thread gameThread;
    int FPS = 10;

    // private ActionManager actionManager;
    public CollisionManager cm = new CollisionManager(this);
    public ObjectSetter objSetter = new ObjectSetter(this);
    public UIController ui = new UIController(this);
    public Player player = new Player("Labpro", "Female", "MyFarm", this, keyHandler);
    public GameObject obj[] = new GameObject[10];

    public GameStates gameState;
    // public int gameState;
    // public int MAP = 0;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        // gameState = GameStates.MAP;
    }

    public void setupGame() {
        // actionManager = new ActionManager(this);
        objSetter.setObject();
        gameState = GameStates.INITIAL;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS; // 0.01666667 seconds
        long lastTime = System.nanoTime();
        long currentTime;
        double delta = 0;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            // System.out.println("Game is running...");
            // System.out.println("Current Time: " + currentTime);
            // long currentTime = System.nanoTime();


            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime); // Convert to milliseconds
            lastTime = currentTime;

            if (delta >= 1) {
                update(); // Update the game state
                repaint(); // Repaint the screen
                delta--; 
                drawCount++;
            }

            if (timer >= 1000000000) { // 1 second
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    public void update() {
        if (gameState == GameStates.MAP) {
            player.update();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // awal masuk game
        if (gameState == GameStates.INITIAL) {
            ui.draw(g2);
        }

        // masuk ke game
        else {
            // ini buat gambar tilenya
            tileManager.draw(g2);
            
            // ini buat gambar object di map
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }
            
            // ini buat gambar playernya di map
            player.draw(g2);

            ui.draw(g2);
        }

        g2.dispose(); 
    }
}
