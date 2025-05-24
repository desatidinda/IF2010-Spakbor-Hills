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

public class TileManager {

    GamePanel gp;
    Tile[] tile;
    int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];

        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
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
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/map/TileImage/Water.png"));
            tile[2].collision = true;

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

    // private void generateMap() {
    //     for (int x = 0; x < size; x++) {
    //         for (int y = 0; y < size; y++) {
    //             map[x][y] = new Tile(Tile.TileType.TILLABLE);
    //         }
    //     }

    //     // Contoh tile house dan lainnya
    //     for (int x = 5; x < 11; x++) {
    //         for (int y = 5; y < 11; y++) {
    //             map[x][y] = new Tile(Tile.TileType.HOUSE);
    //         }
    //     }
    // }

    // public Tile[][] getMap() {
    //     return map;
    // }

    // public int getSize() {
    //     return size;
    // }

    // public Tile getTile(int x, int y) {
    //     return map[x][y];
    // }
}
