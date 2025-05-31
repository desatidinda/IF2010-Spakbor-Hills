package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import java.awt.*;

import main.GamePanel;
import map.Tile;
import map.TileType;
import entity.Player.Inventory;
import entity.Item.*;

public class TileManager {

    GamePanel gp;
    Tile[] tile;
    int mapTileNum[][];
    private boolean[][] wateredMap;
    private int[][] waterCountMap;
    private String[][] plantedSeedNameMap;
    private int[][] plantAgeMap;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];

        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        wateredMap = new boolean[gp.maxWorldCol][gp.maxWorldRow];
        waterCountMap = new int[gp.maxWorldCol][gp.maxWorldRow];
        plantedSeedNameMap = new String[gp.maxWorldCol][gp.maxWorldRow];
        plantAgeMap = new int[gp.maxWorldCol][gp.maxWorldRow];

        getImage();
        loadMap("/map/map.txt");
    }

    public void getImage() {
        try {
            tile[0] = new Tile(TileType.TILLED);
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/map/TileImage/Tilled.png"));

            tile[1] = new Tile(TileType.TILLABLE);
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/map/TileImage/Tillable.png"));

            tile[2] = new Tile(TileType.PLANTED);
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/map/TileImage/Planted.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream input = getClass().getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = reader.readLine();
                while (col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            reader.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public void tillTile(int col, int row) {
        int tileNum = mapTileNum[col][row];
        if (tile[tileNum].getType() == TileType.TILLABLE) {
            mapTileNum[col][row] = 0; // 0 = TILLED
        }
    }

    public void recoverTile(int col, int row) {
        int tileNum = mapTileNum[col][row];
        if (tile[tileNum].getType() == TileType.TILLED) {
            mapTileNum[col][row] = 1; // 1 = TILLABLE
            wateredMap[col][row] = false;
        }
    }

    public void plantSeed(int col, int row, Item seed) {
        int tileNum = mapTileNum[col][row];
        if (tile[tileNum].getType() == TileType.TILLED) {
            mapTileNum[col][row] = 2; // 2 = PLANTED
            plantedSeedNameMap[col][row] = seed.getItemName();
            plantAgeMap[col][row] = 0;
        }
    }

    public void waterTile(int col, int row) {
        int tileNum = mapTileNum[col][row];
        if (tile[tileNum].getType() == TileType.PLANTED) {
            wateredMap[col][row] = true;
            waterCountMap[col][row]++;
        }
    }

    public boolean harvestPlant(int col, int row, Inventory inventory, String plantedSeedName) {
        int tileNum = mapTileNum[col][row];
        if (tile[tileNum].getType() == TileType.PLANTED) {
            Seeds seed = (Seeds) ItemFactory.createItem(plantedSeedName);
            int age = plantAgeMap[col][row];
            if (age < seed.getDaysToHarvest()) {
                gp.ui.showPopupMessage("Crops not yet ready for harvest! Age: " + age + " / " + seed.getDaysToHarvest());
                return false;
            }
            String cropName = plantedSeedName.replace("Seeds", "").trim();
            Item cropItem = ItemFactory.createItem(cropName); 
            inventory.addItem(cropItem, 1);

            String unlocked = RecipeUnlocker.checkItemUnlock(cropItem.getItemName());
            if (unlocked != null) {
                gp.ui.showRecipeUnlockMessage("Resep " + unlocked + " berhasil dipelajari!");
            }
            if (RecipeUnlocker.checkHarvestUnlock()) {
                gp.ui.showRecipeUnlockMessage("Resep Veggie Soup berhasil dipelajari!");
            }


            mapTileNum[col][row] = 0;
            wateredMap[col][row] = false;
            plantAgeMap[col][row] = 0;
            return true;
        }
        return false;
    }

    public int[][] getMapTileNum() {
        return mapTileNum;
    }

    public Tile[] getTile() {
        return tile;
    }

    public String[][] getPlantedSeedNameMap() {
        return plantedSeedNameMap;
    }
    
    public boolean[][] getWateredMap() {
        return wateredMap;
    }

    public void resetWaterCountAndWateredMap(boolean isRainy) {
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                if (isRainy) {
                    waterCountMap[col][row] = 2;
                    wateredMap[col][row] = true;
                } else {
                    waterCountMap[col][row] = 0;
                    wateredMap[col][row] = false;
                }
            }
        }
    }

    public void checkPlantsAtEndOfDay() {
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                int tileNum = mapTileNum[col][row];
                if (tile[tileNum].getType() == TileType.PLANTED) {
                    if (waterCountMap[col][row] < 2) {
                        System.out.println("Tanaman di (" + col + "," + row + ") mati karena kurang siram!");
                        // Tanaman mati kl gadisiram duakali

                        mapTileNum[col][row] = 0; // 0 = TILLED
                        plantedSeedNameMap[col][row] = null;
                        wateredMap[col][row] = false;
                        waterCountMap[col][row] = 0;
                    }
                }
            }
        }
    }

    public void incrementPlantAges() {
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                int tileNum = mapTileNum[col][row];
                if (tile[tileNum].getType() == TileType.PLANTED) {
                    plantAgeMap[col][row]++;
                }
            }
        }
    }
}
