package controller;

import main.GamePanel;
import entity.Player.*;
import objects.*;

public class CollisionManager {
    GamePanel gp;

    public CollisionManager(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Player player) {
        // batas player di world
        int playerLeftWorldX = player.worldX + player.solid.x;
        int playerRightWorldX = player.worldX + player.solid.x + player.solid.width;
        int playerTopWorldY = player.worldY + player.solid.y;
        int playerBottomWorldY = player.worldY + player.solid.y + player.solid.height;
        
        // menyimpan posisi tile yg mau dicek
        int playerLeftCol, playerRightCol, playerTopRow, playerBottomRow;
        int tileNum1, tileNum2;
        
        player.collision = false;
        
        switch (player.direction) {
            case "up":
                playerTopRow = (playerTopWorldY - player.speed) / gp.tileSize;
                playerLeftCol = playerLeftWorldX / gp.tileSize;
                playerRightCol = playerRightWorldX / gp.tileSize;
                
                // cek ujung world
                if (playerTopRow < 0) {
                    player.collision = true;
                    return;
                }
                
                playerLeftCol = Math.max(0, Math.min(playerLeftCol, gp.maxWorldCol - 1));
                playerRightCol = Math.max(0, Math.min(playerRightCol, gp.maxWorldCol - 1));
                
                try {
                    tileNum1 = gp.tileManager.mapTileNum[playerLeftCol][playerTopRow];
                    tileNum2 = gp.tileManager.mapTileNum[playerRightCol][playerTopRow];
                    
                    if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
                        player.collision = true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Warning: Array bound check in UP direction: " + e.getMessage());
                    player.collision = true;
                }
                break;
                
            case "down":
                playerBottomRow = (playerBottomWorldY + player.speed) / gp.tileSize;
                playerLeftCol = playerLeftWorldX / gp.tileSize;
                playerRightCol = playerRightWorldX / gp.tileSize;
                
                if (playerBottomRow >= gp.maxWorldRow) {
                    player.collision = true;
                    return;
                }
                
                playerLeftCol = Math.max(0, Math.min(playerLeftCol, gp.maxWorldCol - 1));
                playerRightCol = Math.max(0, Math.min(playerRightCol, gp.maxWorldCol - 1));
                
                try {
                    tileNum1 = gp.tileManager.mapTileNum[playerLeftCol][playerBottomRow];
                    tileNum2 = gp.tileManager.mapTileNum[playerRightCol][playerBottomRow];
                    
                    if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
                        player.collision = true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Warning: Array bound check in DOWN direction: " + e.getMessage());
                    player.collision = true;
                }
                break;
                
            case "left":
                playerLeftCol = (playerLeftWorldX - player.speed) / gp.tileSize;
                playerTopRow = playerTopWorldY / gp.tileSize;
                playerBottomRow = playerBottomWorldY / gp.tileSize;
                
                if (playerLeftCol < 0) {
                    player.collision = true;
                    return;
                }
                
                playerTopRow = Math.max(0, Math.min(playerTopRow, gp.maxWorldRow - 1));
                playerBottomRow = Math.max(0, Math.min(playerBottomRow, gp.maxWorldRow - 1));
                
                try {
                    tileNum1 = gp.tileManager.mapTileNum[playerLeftCol][playerTopRow];
                    tileNum2 = gp.tileManager.mapTileNum[playerLeftCol][playerBottomRow];
                    
                    if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
                        player.collision = true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Warning: Array bound check in LEFT direction: " + e.getMessage());
                    player.collision = true;
                }
                break;
                
            case "right":
                playerRightCol = (playerRightWorldX + player.speed) / gp.tileSize;
                playerTopRow = playerTopWorldY / gp.tileSize;
                playerBottomRow = playerBottomWorldY / gp.tileSize;
                
                if (playerRightCol >= gp.maxWorldCol) {
                    player.collision = true;
                    return;
                }
                
                playerTopRow = Math.max(0, Math.min(playerTopRow, gp.maxWorldRow - 1));
                playerBottomRow = Math.max(0, Math.min(playerBottomRow, gp.maxWorldRow - 1));
                
                try {
                    tileNum1 = gp.tileManager.mapTileNum[playerRightCol][playerTopRow];
                    tileNum2 = gp.tileManager.mapTileNum[playerRightCol][playerBottomRow];
                    
                    if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
                        player.collision = true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Warning: Array bound check in RIGHT direction: " + e.getMessage());
                    player.collision = true;
                }
                break;
        }
    }
    public int checkObject(Player player, boolean playerCollision) {
        int index = 999;
        for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] != null) {
                    player.solid.x = player.worldX + player.solid.x;
                    player.solid.y = player.worldY + player.solid.y;

                    gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                    gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                    gp.obj[i].solidArea.width = gp.obj[i].widthInTiles * gp.tileSize;
                    gp.obj[i].solidArea.height = gp.obj[i].heightInTiles * gp.tileSize;

                    switch (player.direction) {
                        case "up":
                            player.solid.y -= player.speed;
                            if (player.solid.intersects(gp.obj[i].solidArea)) {
                                if (gp.obj[i].collision) {
                                    player.collision = true;
                                    index = i;
                                }
                            }
                            break;
                        case "down":
                            player.solid.y += player.speed;
                            if (player.solid.intersects(gp.obj[i].solidArea)) {
                                if (gp.obj[i].collision) {
                                    player.collision = true;
                                    index = i;
                                }
                            }
                            break;
                        case "left":
                            player.solid.x -= player.speed;
                            if (player.solid.intersects(gp.obj[i].solidArea)) {
                                if (gp.obj[i].collision) {
                                    player.collision = true;
                                    index = i;
                                }
                            }
                            break;
                        case "right":
                            player.solid.x += player.speed;
                            if (player.solid.intersects(gp.obj[i].solidArea)) {
                                if (gp.obj[i].collision) {
                                    player.collision = true;
                                    index = i;
                                }
                            }
                            break;

                    }
                    player.solid.x = player.solidDefaultX;
                    player.solid.y = player.solidDefaultY;
                    gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                    gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
                    gp.obj[i].solidArea.width = gp.obj[i].solidAreaDefaultWidth;
                    gp.obj[i].solidArea.height = gp.obj[i].solidAreaDefaultHeight;
                }
            }
            return index;
    }
}