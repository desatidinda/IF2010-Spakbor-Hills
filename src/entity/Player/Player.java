package entity.Player;

import entity.Item.*;
import entity.NPC.NPC;
import map.Point;
import input.KeyHandler;
import main.GameClock;
import main.GamePanel;
import main.GameStates;
import main.GameObserver;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {
    private String name;
    private String gender;
    private int energy;
    private String farmName;
    private String partner;
    private String relationshipStatus = "Single"; //status: Single, Engaged, Married
    private double gold;

    private static List<GameObserver> observers = new ArrayList<>();
    private Inventory inventory;
    private Point location;
    private Point indoorLocation;
    private static final int MAX_ENERGY = 100;
    private static final int MIN_ENERGY = -20;
    private boolean hasReachedEndgame = false;

    public final int speed = 1;
    public int worldX, worldY;
    public int savedWorldX, savedWorldY;
    public final int screenX, screenY;
    public int houseX, houseY;


    GamePanel gp;
    KeyHandler keyHandler;
    private BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction = "down";
    private int spriteCounter = 0;
    private int spriteNum = 1;
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
        this.gold = 0.0;
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
        Item parsnipSeeds = ItemFactory.createItem("Parsnip Seeds");
        inventory.addItem(parsnipSeeds, 15);
    }

    public static void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    private void notifyExhausted() {
        for (GameObserver observer : observers) {
            observer.onPlayerExhausted();
        }
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
                if (gp.gameState == GameStates.NPC_HOUSE) {
                    state.NPCHouseState npcHouseState = (state.NPCHouseState) gp.stateHandlers.get(GameStates.NPC_HOUSE);
                    if (npcHouseState != null) {
                        gp.cm.checkNPCCollision(this, npcHouseState.getNpcInHouse());
                    }
                }
                int furnitureIndex = gp.cm.checkIndoorObject(this, true);
                if (collision == false) {
                    switch (direction) {
                        case "up":
                            houseY -= gp.tileSize;
                            break;
                        case "down":
                            houseY += gp.tileSize;
                            break;
                        case "left":
                            houseX -= gp.tileSize;
                            break;
                        case "right":
                            houseX += gp.tileSize;
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

    public String getRelationshipStatus() {
        return relationshipStatus;
    } 

    public double getGold() { 
        return gold; 
    }

    public Inventory getInventory() { 
        return inventory; 
    }

    public Item getFavoriteItem() {
        //favorite item dr player ga berpengaruh apa apa di game ini hehe
        if (getGender().equals("Male")){
            return ItemFactory.createItem("AK47");
        } else { 
            return ItemFactory.createItem("Lalabu");
        }
    }

    public Point getLocation() { 
        if (gp.gameState == GameStates.MAP) {
            return location;
        } else if (gp.gameState == GameStates.INSIDE_HOUSE) {
            return indoorLocation;
        }
        return null;
    }

    public boolean hasReachedEndgame() {
        return hasReachedEndgame;
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

    public void setRelationshipStatus(String status) {
        this.relationshipStatus = status;
    }

    public void setLocation(Point location) { 
        this.location = location; 
    }
    
    public void setEnergy(int energy) { 
        this.energy = Math.max(MIN_ENERGY, Math.min(MAX_ENERGY, energy)); 
    }

    public void setReachedEndgame(boolean value) {
        hasReachedEndgame = value;
    }
    
    // METHOD
    public void addGold(double amount) { 
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

        if (energy <= 10) {
            notifyExhausted();
        }

        if (isExhausted()) {
            pingsan();
        }
        return true;
    }

    public void recoverEnergy(int amount) {
        energy = Math.min(MAX_ENERGY, energy + amount);
    }

    public boolean isExhausted() {
        return energy <= MIN_ENERGY;
    }

    public void sleep() {
        gp.ui.startSleepFade();

        if (energy < MAX_ENERGY * 0.1) {
            energy = MAX_ENERGY / 2;
        } else if (energy == 0) {
            energy = MAX_ENERGY / 2 + 10;
        } else {
            energy = MAX_ENERGY;
        }
        //GameClock.skipToMorning();
    }

    public void pingsan() {
        gp.ui.startPingsanFade(isExhausted());

        if (energy <= MIN_ENERGY) {
        energy = 10; 
        } else {
            energy = Math.min(MAX_ENERGY, (int)(energy + Math.floor(energy/2))); 
        }

        // if (GameClock.isPingsan() || GameClock.getHour() < 6) {
        //     GameClock.skipUntilMorning();
        // } else {
        //     GameClock.skipToMorning();
        // }

        gp.gameState = GameStates.INSIDE_HOUSE;
        houseX = gp.screenWidth / 2 - (gp.tileSize / 2);
        houseY = gp.screenHeight / 2 - (gp.tileSize / 2);
        worldX = gp.tileSize * 16;
        worldY = gp.tileSize * 16;
        solid.x = houseX;
        solid.y = houseY;
    }

    public void chatWith(NPC npc) {
        npc.incrementChatting();
        npc.chat();
        performAction(10);
    }

    public boolean giveGift(NPC npc, String itemName) {
        Item item = ItemFactory.createItem(itemName);
        if (inventory.hasItem(item)) {
            npc.incrementGifting(); 
            return true;
        }
        return false;
    }

     public boolean propose(NPC npc) {
        if (!npc.getRelationshipStatus().equals("Single")) {
            performAction(20);
            GameClock.skipMinutes(60);
            return false;
        }

        if (npc.getHeartPoints() < 150) {
            performAction(20);
            GameClock.skipMinutes(60);
            return false;
        }

        npc.setRelationshipStatus("Fiance");
        setPartner(npc.getName());
        npc.setLastProposalDay(GameClock.getDay());
        performAction(10);
        GameClock.skipMinutes(60);
        return true;
    }

    public boolean marry(NPC npc) {
        if (!npc.getRelationshipStatus().equals("Fiance")) return false;
        if (!npc.getName().equals(partner)) return false;
        if (GameClock.getDay() - npc.getLastProposalDay() < 1) return false;

        npc.setRelationshipStatus("Spouse");
        setPartner(npc.getName());
        performAction(80);
        GameClock.skipToHour(22);
        return true;
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
        int availableFuel = inventory.getItemCount(fuel);

        if (availableFuel < neededFuelUnits) {
            System.out.println("Fuel tidak cukup. Butuh " + neededFuelUnits + ", punya " + availableFuel);
            return;
        }

        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            String ingredientName = entry.getKey();
            int totalNeeded = entry.getValue() * quantity;

            if (ingredientName.equals("Any Fish")) {
                int totalFish = 0;
                for (Map.Entry<Item, Integer> invEntry : inventory.getItems().entrySet()) {
                    if (invEntry.getKey() instanceof Fish) {
                        totalFish += invEntry.getValue();
                    }
                }
                if (totalFish < totalNeeded) {
                    System.out.println("Jumlah ikan tidak cukup (butuh " + totalNeeded + ")");
                    return;
                }
            } else {
                Item item = ItemFactory.createItem(ingredientName);
                if (!inventory.hasItem(item, totalNeeded)) {
                    System.out.println("Bahan tidak cukup: " + item.getItemName() + " (butuh " + totalNeeded + ")");
                    return;
                }
            }

            for (Map.Entry<String, Integer> ingredientEntry : recipe.getIngredients().entrySet()) {
                String ingName = ingredientEntry.getKey();
                int total = ingredientEntry.getValue() * quantity;

                if (ingName.equals("Any Fish")) {
                    int remainingToRemove = total;

                    for (Map.Entry<Item, Integer> invEntry : inventory.getItems().entrySet()) {
                        Item fish = invEntry.getKey();
                        int available = invEntry.getValue();

                        if (fish instanceof entity.Item.Fish) {
                            int toRemove = Math.min(available, remainingToRemove);
                            inventory.removeItem(fish, toRemove);
                            remainingToRemove -= toRemove;
                            if (remainingToRemove == 0) break;
                        }
                    }
                } else {
                    Item item = ItemFactory.createItem(ingredientName);
                    inventory.removeItem(item, totalNeeded);
                }
            }

            inventory.removeItem(fuel, neededFuelUnits);

            Item cookedItem = ItemFactory.createItem(recipe.getName());
            inventory.addItem(cookedItem, quantity);

            energy -= 10 * quantity;

            System.out.println("Berhasil memasak " + quantity + "x " + recipe.getName() + "!");
        }
    }

    public void watchingTV() {
        performAction(5);
    }
}

