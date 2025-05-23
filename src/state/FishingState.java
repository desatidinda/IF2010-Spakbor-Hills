package state;

import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class FishingState implements StateHandler {

    private final GamePanel gp;
    private BufferedImage image;

    public FishingState(GamePanel gp) {
        this.gp = gp;
        loadBackground();
    }

    private void loadBackground() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/seaside.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        gp.player.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, 0, 0, gp.screenWidth, gp.screenHeight, null);

        gp.player.draw(g2);
        gp.ui.draw(g2);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            gp.keyHandler.upPressed = true;
        } else if (key == KeyEvent.VK_S) {
            gp.keyHandler.downPressed = true;
        } else if (key == KeyEvent.VK_A) {
            gp.keyHandler.leftPressed = true;
        } else if (key == KeyEvent.VK_D) {
            gp.keyHandler.rightPressed = true;
        } else if (key == KeyEvent.VK_ESCAPE) {
            gp.player.teleportOut();
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
}

// ini main logic nya (di remark dulu karena belum fix untuk penggunaan buttonnya)
// package state;

// import controller.FishingManager;
// import entity.Item.Fish;
// import entity.Item.FishData;
// import entity.Player.Player;
// import main.GameClock;

// import java.util.List;
// import java.util.Scanner;

// public class FishingState {

//     public static void run() {
//         Scanner scanner = new Scanner(System.in);
//         Player player = new Player("Tester");

//         GameClock.init();

//         System.out.println("=== Fishing Test Program ===");
//         System.out.println("Cuaca: " + GameClock.getCurrentWeather());
//         System.out.println("Musim: " + GameClock.getCurrentSeason());
//         System.out.println("Waktu: " + GameClock.getFormattedTime());

//         System.out.println("Pilih lokasi memancing:");
//         System.out.println("1. Mountain Lake");
//         System.out.println("2. Forest River");
//         System.out.println("3. Ocean");
//         System.out.println("4. Pond");
//         System.out.print("Lokasi: ");
//         int choice = scanner.nextInt();

//         String location;
//         switch (choice) {
//             case 1 -> location = "Mountain Lake";
//             case 2 -> location = "Forest River";
//             case 3 -> location = "Ocean";
//             case 4 -> location = "Pond";
//             default -> {
//                 System.out.println("Lokasi tidak valid!");
//                 return;
//             }
//         }


//         List<Fish> availableFish = FishData.ALL_FISH.stream()
//                 .filter(f -> f.getLocations().contains(location))
//                 .toList();

//         System.out.println("Ikan yang tersedia:");
//         for (Fish f : availableFish) {
//             System.out.println("- " + f.getName());
//         }

//         FishingManager.startFishing(player, availableFish);

//         player.getInventory().printInventory();

//         System.out.println("=== End of Fishing Test ===");
//     }

//     public static void main(String[] args) {
//         run();
//     }
// }

