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
    private String[][] plantedSeedNameMap;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];

        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        wateredMap = new boolean[gp.maxWorldCol][gp.maxWorldRow];
        plantedSeedNameMap = new String[gp.maxWorldCol][gp.maxWorldRow];

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
            // TODO: handle exception
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
        }
    }

    public void waterTile(int col, int row) {
        int tileNum = mapTileNum[col][row];
        if (tile[tileNum].getType() == TileType.PLANTED && !wateredMap[col][row]) {
            wateredMap[col][row] = true;
        }
    }

    public void harvestPlant(int col, int row, Inventory inventory, String plantedSeedName) {
        int tileNum = mapTileNum[col][row];
        if (tile[tileNum].getType() == TileType.PLANTED && wateredMap[col][row]) {
            String cropName = plantedSeedName.replace("Seeds", "").trim();
            Item cropItem = ItemFactory.createItem(cropName); 
            inventory.addItem(cropItem, 1);

            mapTileNum[col][row] = 0;
            wateredMap[col][row] = false;
        }
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
}
