package controller;

import java.util.Random;

import entity.House.House;
import entity.House.ShippingBin;
import main.GamePanel;

public class ObjectSetter {
    GamePanel gp;
    Random random = new Random();

    public ObjectSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        deployHouseAndBin();
        // gp.obj[0] = new House();
        // gp.obj[0].worldX = 12 * gp.tileSize;
        // gp.obj[0].worldY = 12 * gp.tileSize;

        // gp.obj[1] = new ShippingBin();
        // gp.obj[1].worldX = 18 * gp.tileSize;
        // gp.obj[1].worldY = 16 * gp.tileSize;
    }

    public void deployHouseAndBin() {
        int houseWidth = 6;
        int houseHeight = 6;

        int binWidth = 2;
        int binHeight = 2;

        // Posisi ShippingBin offset dari pojok kiri atas house
        int binOffsetX = 6; // tile ke kanan
        int binOffsetY = 4; // tile ke bawah

        // Hitung posisi maksimal house agar house & bin tetap di dalam world
        int maxCol = gp.maxWorldCol - Math.max(houseWidth, binOffsetX + binWidth);
        int maxRow = gp.maxWorldRow - Math.max(houseHeight, binOffsetY + binHeight);

        int col = random.nextInt(maxCol);
        int row = random.nextInt(maxRow);

        int houseX = col * gp.tileSize;
        int houseY = row * gp.tileSize;

        House house = new House();
        house.worldX = houseX;
        house.worldY = houseY;
        gp.obj[0] = house;

        ShippingBin bin = new ShippingBin();
        bin.worldX = houseX + gp.tileSize * binOffsetX;
        bin.worldY = houseY + gp.tileSize * binOffsetY;
        gp.obj[1] = bin;
    }

}
