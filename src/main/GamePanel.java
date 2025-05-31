package main;

import controller.*;
import entity.NPC.*;
import entity.Player.*;
import input.KeyHandler;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import objects.GameObject;
import state.*;

public class GamePanel extends JPanel implements Runnable {

    // screen setting
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 px
    public final int screenHeight = tileSize * maxScreenRow; // 576 px
    
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int worldWidth = tileSize * maxWorldCol; //1024
    public final int worldHeight = tileSize * maxWorldRow; //768

    int FPS = 10;
    Thread gameThread;

    public TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public CollisionManager cm = new CollisionManager(this);
    public ObjectSetter objSetter = new ObjectSetter(this);
    public UIController ui = new UIController(this);
    public Player player = new Player("Labpro", "Female", "MyFarm", this, keyHandler);
    public GameObject obj[] = new GameObject[10];
    public GameObject furniture[] = new GameObject[10];
    public NPC npc[] = new NPC[6];

    // game state
    public GameStates gameState;
    public final Map<GameStates, StateHandler> stateHandlers = new HashMap<>();

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        npc[0] = new MayorTadi(this);
        npc[1] = new Caroline(this);
        npc[2] = new Perry(this);
        npc[3] = new Dasco(this);
        npc[4] = new Emily(this);
        npc[5] = new Abigail(this);

        Player.addObserver(ui); 
        GameClock.addObserver(ui);

        setupGame();
    }

    public void setupGame() {
        objSetter.setObject();
        gameState = GameStates.INITIAL;

        stateHandlers.put(GameStates.INITIAL, new InitialState(this));
        stateHandlers.put(GameStates.MAP, new MapState(this));
        stateHandlers.put(GameStates.INSIDE_HOUSE, new InsideHouseState(this));
        stateHandlers.put(GameStates.FISHING, new FishingState(this));
        stateHandlers.put(GameStates.NPC_HOUSE, new NPCHouseState(this, npc[0]));
        stateHandlers.put(GameStates.INVENTORY, new InventoryState(this, player));
        stateHandlers.put(GameStates.MENU, new MenuState(this, player));
        stateHandlers.put(GameStates.STATISTICS, new EndGameStatistics(this));

        //GameClock.init();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;

        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            // Update + repaint sesuai FPS
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        StateHandler handler = stateHandlers.get(gameState);
        if (handler != null) {
            handler.update();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        StateHandler handler = stateHandlers.get(gameState);
        if (handler != null) {
            handler.draw(g2);
        }

        g2.dispose();
    }
}
