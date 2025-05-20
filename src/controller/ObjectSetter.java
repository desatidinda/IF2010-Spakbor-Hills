package controller;

import java.util.Random;

import entity.House.House;
import entity.House.ShippingBin;
import entity.Farm.Pond;
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

        int maxCol = gp.maxWorldCol - totalAreaWidth;
        int maxRow = gp.maxWorldRow - totalAreaHeight;

        int col = random.nextInt(maxCol);
        int row = random.nextInt(maxRow);

        houseStartCol = col;
        houseStartRow = row;
        houseEndCol = houseStartCol + totalAreaWidth - 1;
        houseEndRow = houseStartRow + totalAreaHeight - 1;

        House house = new House();
        house.worldX = col * gp.tileSize;
        house.worldY = row * gp.tileSize;
        gp.obj[0] = house;

        ShippingBin bin = new ShippingBin();
        bin.worldX = (col * gp.tileSize) + gp.tileSize * binOffsetX;
        bin.worldY = (row * gp.tileSize) + gp.tileSize * binOffsetY;
        gp.obj[1] = bin;
    }

    public void deployPond() {
        int pondWidth = 2;
        int pondHeight = 2;

        int colPond, rowPond;
        boolean overlap;

        do {
            colPond = random.nextInt(gp.maxWorldCol - pondWidth);
            rowPond = random.nextInt(gp.maxWorldRow - pondHeight);

            int pondEndCol = colPond + pondWidth - 1;
            int pondEndRow = rowPond + pondHeight - 1;

            overlap = !(colPond > houseEndCol || pondEndCol < houseStartCol ||
                        rowPond > houseEndRow || pondEndRow < houseStartRow);
        } while (overlap);

        Pond pond = new Pond();
        pond.worldX = colPond * gp.tileSize;
        pond.worldY = rowPond * gp.tileSize;
        gp.obj[2] = pond;
    }
}
