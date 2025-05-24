package entity.Player;

import entity.Item.FuelItem;
import entity.Item.Recipe;
import entity.Item.RecipeRegistry;
import entity.NPC.NPC;
import map.Point;
import input.KeyHandler;
import main.GamePanel;
import main.GameStates;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Map;

public class Player {
    private String name;
    private String gender;
    private int energy;
    private String farmName;
    private String partner;
    private int gold;
    private Inventory inventory;
    private Point location;
    private Point indoorLocation;
    public final int speed = 1;
    public int worldX, worldY;
    public int savedWorldX, savedWorldY;
    public final int screenX, screenY;
    public int houseX, houseY;

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
    public boolean teleportMode = false;
    public int teleportOption = 0;

  
    public Player(String name, String gender, String farmName, GamePanel gp, KeyHandler keyHandler) {
        this.name = name;
        this.gender = gender;
        this.farmName = farmName;
        this.energy = MAX_ENERGY;
        this.partner = null;
        this.gold = 0;
        this.inventory = new Inventory();
        this.location = new Point(16, 16); // default starting location (ini ditengah)
        this.indoorLocation = new Point(16, 16); // default indoor location
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
            } 
            else if (keyHandler.downPressed) {
                direction = "down";
            } 
            else if (keyHandler.leftPressed) {
                direction = "left";
            } 
            else if (keyHandler.rightPressed) {
                direction = "right";
            }

            // cek collision di tiap state
            collision = false;
            if (gp.gameState == GameStates.MAP) {
                gp.cm.checkTile(this); 
                int objectIndex = gp.cm.checkObject(this, true);
                teleport(objectIndex);
                if (collision == false) {
                    switch (direction) {
                        case "up":
                            worldY -= gp.tileSize;
                            getLocation().setY(getLocation().getY() - speed);
                            break;
                        case "down":
                            worldY += gp.tileSize;
                            getLocation().setY(getLocation().getY() + speed);
                            break;
                        case "left":
                            worldX -= gp.tileSize;
                            getLocation().setX(getLocation().getX() - speed);
                            break;
                        case "right":
                            worldX += gp.tileSize;
                            getLocation().setX(getLocation().getX() + speed);
                            break;
                    }
                    System.out.println("Location: (" + location.getX() + ", " + location.getY() + ")");
                } 
            } else if (gp.gameState == GameStates.INSIDE_HOUSE || gp.gameState == GameStates.NPC_HOUSE) {
                    //TODO: bikin collision inside house
                    int furnitureIndex = gp.cm.checkIndoorObject(this, true);
                    //TODO: lakuin aktivitas sesuai apa yg ditabrak
                    if (collision == false) {
                        switch (direction) {
                            case "up":
                                houseY -= gp.tileSize;
                                //getLocation().setY(getLocation().getY() - speed);
                                break;
                            case "down":
                                houseY += gp.tileSize;
                                //getLocation().setY(getLocation().getY() + speed);
                                break;
                            case "left":
                                houseX -= gp.tileSize;
                                //getLocation().setX(getLocation().getX() - speed);
                                break;
                            case "right":
                                houseX += gp.tileSize;
                                //getLocation().setX(getLocation().getX() + speed);
                                break;
                        }
                        //System.out.println("Location: (" + getLocation().getX() + ", " + getLocation().getY() + ")");
                    }
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

    public void teleport(int i) {
        if (i == 0) {
            savedWorldX = worldX;
            savedWorldY = worldY;

            gp.gameState = GameStates.INSIDE_HOUSE;

            // Posisi houseX, houseY di layar (centered)
            houseX = gp.screenWidth / 2 - (gp.tileSize / 2);
            houseY = gp.screenHeight / 2 - (gp.tileSize / 2);

            // Posisi di dunia (indoor map), misalnya tile 16,16
            worldX = gp.tileSize * 16;
            worldY = gp.tileSize * 16;

            indoorLocation.setX(16);
            indoorLocation.setY(16);

            solid.x = houseX;
            solid.y = houseY;
        } else if (i == 2) {
            savedWorldX = worldX;
            savedWorldY = worldY;

            gp.gameState = GameStates.FISHING;
        }
    }

    public void teleportOut() {
        gp.gameState = GameStates.MAP;
        worldX = savedWorldX;
        worldY = savedWorldY;
        teleportMode = false;
        location.setX(worldX / gp.tileSize);
        location.setY(worldY / gp.tileSize);
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
        if (gp.gameState == GameStates.MAP) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        } else if (gp.gameState == GameStates.INSIDE_HOUSE || gp.gameState == GameStates.NPC_HOUSE) {
            g2.drawImage(image, houseX, houseY, gp.tileSize, gp.tileSize, null);
        } else if (gp.gameState == GameStates.FISHING) {
            g2.drawImage(image, gp.tileSize * 8 - 8, gp.tileSize * 9, gp.tileSize, gp.tileSize, null);
        }
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
        if (gp.gameState == GameStates.MAP) {
            return location;
        } else if (gp.gameState == GameStates.INSIDE_HOUSE) {
            return indoorLocation;
        }
        return null;
    }


    // SETTER
    public void setName(String name) { 
        this.name = name; 
    }

    public void setGender(String gender) { 
        this.gender = gender;
        getImage();
    }

    public void setFarmName(String farmName) { 
        this.farmName = farmName; 
    }

    public void setPartner(String partner) { 
        this.partner = partner; 
    }

    public void setLocation(Point location) { 
        this.location = location; 
    }

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

    public void giveGift(NPC npc, String itemName) {
        if (inventory.hasItem(itemName)) {
            inventory.removeItem(itemName);
            npc.reactToGift(itemName);
            energy -= 5;
            System.out.println("Kamu memberikan " + itemName + " ke " + npc.getName());
        } else {
            System.out.println("Kamu tidak memiliki item: " + itemName);
        }
    }


    public void cook(String recipeName, String fuelName, int quantity) {
        Recipe recipe = RecipeRegistry.get(recipeName);

        if (recipe == null) {
            System.out.println("Resep tidak ditemukan.");
            return;
        }

        if (!recipe.isUnlocked()) {
            System.out.println("Resep belum kamu pelajari.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Jumlah masakan harus lebih dari 0.");
            return;
        }

        FuelItem fuel = FuelItem.getFuelFromName(fuelName);
        if (fuel == null) {
            System.out.println("Bahan bakar \"" + fuelName + "\" tidak dikenali.");
            return;
        }

        int requiredFuelCount = recipe.getRequiredFuel() * quantity;
        int neededFuelUnits = fuel.calculateUnitsNeeded(requiredFuelCount);
        int availableFuel = inventory.getItemCount(fuel.getItemName());

        if (availableFuel < neededFuelUnits) {
            System.out.println("Fuel tidak cukup. Butuh " + neededFuelUnits + ", punya " + availableFuel);
            return;
        }

        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            String itemName = entry.getKey();
            int totalNeeded = entry.getValue() * quantity;

            if (!inventory.hasItem(itemName, totalNeeded)) {
                System.out.println("Bahan tidak cukup: " + itemName + " (butuh " + totalNeeded + ")");
                return;
            }
        }

        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            inventory.removeItem(entry.getKey(), entry.getValue() * quantity);
        }

        inventory.removeItem(fuel.getItemName(), neededFuelUnits);

        inventory.addItem(recipe.getName(), quantity);

        energy -= 10 * quantity;

        System.out.println("Berhasil memasak " + quantity + "x " + recipe.getName() + "!");
    }
}

