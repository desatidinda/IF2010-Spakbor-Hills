package entity.Player;

import map.Point;
import input.KeyHandler;
// import main.Game;
import main.GamePanel;

// import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Player {
    private String name;
    private String gender;
    private int energy;
    private final String farmName;
    private String partner;
    private int gold;
    private Inventory inventory;
    private Point location;
    public final int speed = 1;
    public int worldX, worldY;
    public final int screenX, screenY;

    public static final int MAX_ENERGY = 100;
    public static final int MIN_ENERGY = -20;

    GamePanel gp;
    KeyHandler keyHandler;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solid;
    public int solidDefaultX, solidDefaultY;
    public boolean collision = false;

  
    public Player(String name, String gender, String farmName, GamePanel gp, KeyHandler keyHandler) {
        this.name = name;
        this.gender = gender;
        this.farmName = farmName;
        this.energy = MAX_ENERGY;
        this.partner = null;
        this.gold = 0;
        this.inventory = new Inventory();
        this.location = new Point(16, 16); // default starting location (ini ditengah)
        this.gp = gp;
        this.keyHandler = keyHandler;   

        screenX = gp.screenWidth/2 - gp.tileSize / 2;
        screenY = gp.screenHeight/2 - gp.tileSize / 2;
        worldX = gp.tileSize * 15;
        worldY   = gp.tileSize * 15;

        solid = new Rectangle();
        solid.x = 0;
        solid.y = 0;
        solidDefaultX = solid.x;
        solidDefaultY = solid.y;
        solid.width = gp.tileSize;
        solid.height = gp.tileSize;

        getImage();

    }

    public void update() {
        if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
            if (keyHandler.upPressed) {
                direction = "up";
                //moveUp();
            } 
            else if (keyHandler.downPressed) {
                direction = "down";
                //moveDown();
            } 
            else if (keyHandler.leftPressed) {
                direction = "left";
                //moveLeft();
            } 
            else if (keyHandler.rightPressed) {
                direction = "right";
                //moveRight();
            }

            // cek collision
            collision = false;
            gp.cm.checkTile(this); 
            int objectIndex = gp.cm.checkObject(this, true);

            if (collision == false) {
                switch (direction) {
                    case "up":
                        worldY -= gp.tileSize;
                        location.setY(location.getY() - speed);
                        // moveUp();
                        //worldY -= speed;
                        break;
                    case "down":
                        worldY += gp.tileSize;
                        location.setY(location.getY() + speed);
                        // moveDown();
                        //worldY += speed;
                        break;
                    case "left":
                        worldX -= gp.tileSize;
                        location.setX(location.getX() - speed);
                        // moveLeft();
                        //worldX -= speed;
                        break;
                    case "right":
                        worldX += gp.tileSize;
                        location.setX(location.getX() + speed);
                        // moveRight();
                        //worldX += speed;
                        break;
                }
                System.out.println("Location: (" + location.getX() + ", " + location.getY() + ")");
            }
    
            spriteCounter++;
            if (spriteCounter > 1) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }   
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                } 
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                } 
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left": 
                if (spriteNum == 1) {
                    image = left1;
                } 
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                } 
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        //g2.drawImage(image, location.getX() * gp.tileSize, location.getY() * gp.tileSize, gp.tileSize, gp.tileSize, null);

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    } 

    public void getImage() {
        try {
            if (gender.equals("Female")) {
                up1 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cewe_belakang_1.png"));
                up2 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cewe_belakang_2.png"));
                down1 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cewe_depan_1.png"));
                down2 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cewe_depan_2.png"));
                right1 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cewe_kanan_1.png"));
                right2 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cewe_kanan_2.png"));
                left1 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cewe_kiri_1.png"));
                left2 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cewe_kiri_2.png"));
            }
            else {
                up1 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cowo_belakang_1.png"));
                up2 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cowo_belakang_2.png"));
                down1 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cowo_depan_1.png"));
                down2 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cowo_depan_2.png"));
                right1 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cowo_kanan_1.png"));
                right2 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cowo_kanan_2.png"));
                left1 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cowo_kiri_1.png"));
                left2 = ImageIO.read(getClass().getResourceAsStream("/entity/Player/PlayerImage/cowo_kiri_2.png"));
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GETTER
    public String getName() { 
        return name; 
    }

    public String getGender() { 
        return gender; 
    }

    public int getEnergy() { 
        return energy; 
    }

    public String getFarmName() { 
        return farmName; 
    }

    public String getPartner() { 
        return partner; 
    }

    public int getGold() { 
        return gold; 
    }

    public Inventory getInventory() { 
        return inventory; 
    }

    public Point getLocation() { 
        return location; 
    }


    // SETTER
    public void setPartner(String partner) { 
        this.partner = partner; 
    }

    public void setLocation(Point location) { 
        this.location = location; 
    }


    // MOVE METHOD
    // public void moveUp() {
    //     if (worldY - gp.tileSize >= 0) {
    //         location.setY(location.getY() - speed);
    //         worldY -= gp.tileSize;
    //         System.out.println("Location  : (" + location.getX() + ", " + location.getY() + ")");
    //     }
    // }
    
    // public void moveDown() {
    //     if (worldY + gp.tileSize < gp.tileSize * gp.maxWorldRow) {
    //         location.setY(location.getY() + speed);
    //         worldY += gp.tileSize;
    //         System.out.println("Location  : (" + location.getX() + ", " + location.getY() + ")");
            
    //     }
    // }
    
    // public void moveLeft() {
    //     if (worldX - gp.tileSize >= 0) {
    //         location.setX(location.getX() - speed);
    //         worldX -= gp.tileSize;
    //         System.out.println("Location  : (" + location.getX() + ", " + location.getY() + ")");
    //     }
    // }
    
    // public void moveRight() {
    //     if (worldX + gp.tileSize < gp.tileSize * gp.maxWorldCol) {
    //         location.setX(location.getX() + speed);
    //         worldX += gp.tileSize;
    //         System.out.println("Location  : (" + location.getX() + ", " + location.getY() + ")");
    //     }
    // }
    

    // METHOD
    public void addGold(int amount) { 
        this.gold += amount; 
    }

    public boolean useGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public boolean performAction(int energyCost) {
        if (energy - energyCost < MIN_ENERGY) {
            System.out.println("Not enough energy to perform this action.");
            return false;
        }
        energy -= energyCost;
        if (energy <= MIN_ENERGY) {
            System.out.println(name + " is exhausted and falls asleep automatically.");
            sleep();
        }
        return true;
    }

    public void recoverEnergy(int amount) {
        energy = Math.min(MAX_ENERGY, energy + amount);
    }

    public void sleep() {
        if (energy < MAX_ENERGY * 0.1) {
            energy = MAX_ENERGY / 2;
        } 
        else if (energy == 0) {
            energy = MAX_ENERGY / 2 + 10;
        } 
        else {
            energy = MAX_ENERGY;
        }
        System.out.println(name + " has slept and recovered energy.");
    }

    public void displayStatus() {
        System.out.println("Name      : " + name);
        System.out.println("Gender    : " + gender);
        System.out.println("Energy    : " + energy);
        System.out.println("Gold      : " + gold);
        System.out.println("Partner   : " + (partner != null ? partner : "None"));
        System.out.println("Location  : (" + location.getX() + ", " + location.getY() + ")");
        System.out.println("Inventory : " + inventory);
    }

    public void chatWith(NPC npc) {
        npc.incrementChatting();
        npc.chat();
        energy -= 10;
    }

    public void giveGift(NPC npc, Item item) {
        if (inventory.contains(item)) {
            inventory.remove(item);
            npc.incrementGifting();
            npc.reactToGift(item);
            energy -= 5;
        }
    }
}
