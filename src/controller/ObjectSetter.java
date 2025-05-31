package controller;

import java.util.Random;

import entity.House.House;
import entity.Farm.Pond;
import entity.Farm.ShippingBin;
import main.GamePanel;

public class ObjectSetter {
    GamePanel gp;
    Random random = new Random();

    int houseStartCol, houseStartRow, houseEndCol, houseEndRow;

    public ObjectSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        deployHouseAndBin();
        deployPond();
    }

    public void deployHouseAndBin() {
        int houseWidth = 6;
        int houseHeight = 6;

        int binWidth = 2;
        int binHeight = 2;

        int binOffsetX = 6;
        int binOffsetY = 4;

        int totalAreaWidth = Math.max(houseWidth, binOffsetX + binWidth);
        int totalAreaHeight = Math.max(houseHeight, binOffsetY + binHeight);

        int col, row;
        boolean inCenterZone;

        do {
            int maxCol = gp.maxWorldCol - totalAreaWidth - 1; 
            int maxRow = gp.maxWorldRow - totalAreaHeight - 1;

            col = random.nextInt(maxCol) + 1; 
            row = random.nextInt(maxRow) + 1;

            int houseEndCol = col + totalAreaWidth - 1;
            int houseEndRow = row + totalAreaHeight - 1;

            int centerCol = gp.maxWorldCol / 2;
            int centerRow = gp.maxWorldRow / 2;
            int exclusionSize = 8; 
            
            int centerZoneStartCol = centerCol - exclusionSize / 2;
            int centerZoneEndCol = centerCol + exclusionSize / 2;
            int centerZoneStartRow = centerRow - exclusionSize / 2;
            int centerZoneEndRow = centerRow + exclusionSize / 2;
            
            inCenterZone = !(col > centerZoneEndCol || houseEndCol < centerZoneStartCol ||
                            row > centerZoneEndRow || houseEndRow < centerZoneStartRow);

        } while (inCenterZone);

        houseStartCol = col;
        houseStartRow = row;
        houseEndCol = houseStartCol + totalAreaWidth - 1;
        houseEndRow = houseStartRow + totalAreaHeight - 1;

        House house = new House();
        house.worldX = col * gp.tileSize;
        house.worldY = row * gp.tileSize;
        gp.obj[0] = house;

        ShippingBin bin = new ShippingBin(gp);
        bin.worldX = (col * gp.tileSize) + gp.tileSize * binOffsetX;
        bin.worldY = (row * gp.tileSize) + gp.tileSize * binOffsetY;
        gp.obj[1] = bin;
    }

    public void deployPond() {
        int pondWidth = 2;
        int pondHeight = 2;

        int colPond, rowPond;
        boolean overlap, inCenterZone;

        do {
            int maxCol = gp.maxWorldCol - pondWidth - 1; 
            int maxRow = gp.maxWorldRow - pondHeight - 1;

            colPond = random.nextInt(maxCol - 1) + 1;
            rowPond = random.nextInt(maxRow - 1) + 1;

            int pondEndCol = colPond + pondWidth - 1;
            int pondEndRow = rowPond + pondHeight - 1;

            overlap = !(colPond > houseEndCol || pondEndCol < houseStartCol ||
                        rowPond > houseEndRow || pondEndRow < houseStartRow);
            
            int centerCol = gp.maxWorldCol / 2;
            int centerRow = gp.maxWorldRow / 2;
            int exclusionSize = 6;
            
            int centerZoneStartCol = centerCol - exclusionSize / 2;
            int centerZoneEndCol = centerCol + exclusionSize / 2;
            int centerZoneStartRow = centerRow - exclusionSize / 2;
            int centerZoneEndRow = centerRow + exclusionSize / 2;
            
            inCenterZone = !(colPond > centerZoneEndCol || pondEndCol < centerZoneStartCol ||
                            rowPond > centerZoneEndRow || pondEndRow < centerZoneStartRow);
        } while (overlap || inCenterZone);

        Pond pond = new Pond();
        pond.worldX = colPond * gp.tileSize;
        pond.worldY = rowPond * gp.tileSize;
        gp.obj[2] = pond;
    }
}
