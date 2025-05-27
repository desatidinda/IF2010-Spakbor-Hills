package controller;

import main.GamePanel;
import map.Point;
import entity.NPC.NPC;
import entity.Player.*;

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
                if (playerTopWorldY - player.speed < 0) {
                    player.collision = true;
                    player.teleportMode = true;
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
                    player.teleportMode = true;
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
                
                if (playerLeftWorldX - player.speed < 0) {
                player.collision = true;
                player.teleportMode = true;
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
                    player.teleportMode = true;
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

    public int checkIndoorObject(Player player, boolean playerCollision) {
        int index = 999;

        // original solid position
        int originalX = player.solid.x;
        int originalY = player.solid.y;
        
        // krn di dalem house pake housex
        int playerHouseX = player.houseX - 378;
        int playerHouseY = player.houseY - 288;
        
        // prediksi abis ini dmn yh
        int predictedX = playerHouseX + player.solid.x;
        int predictedY = playerHouseY + player.solid.y;
        
        switch (player.direction) {
            case "up":
                predictedY -= gp.tileSize; 
                break;
            case "down":
                predictedY += gp.tileSize;
                break;
            case "left":
                predictedX -= gp.tileSize;
                break;
            case "right":
                predictedX += gp.tileSize;
                break;
        }
        
        // solid playernya jadiin ke prediksi
        player.solid.x = predictedX;
        player.solid.y = predictedY;
        
        // System.out.println("DEBUG - Player house coords: (" + playerHouseX + "," + playerHouseY + ")");
        // System.out.println("DEBUG - Player predicted position: (" + predictedX + "," + predictedY + ") direction: " + player.direction);
        
        for (int i = 0; i < gp.furniture.length; i++) {
            if (gp.furniture[i] != null) {
                if (gp.furniture[i].solidArea == null) {
                    continue;
                }
                
                // original furniture solid
                int furnitureOriginalX = gp.furniture[i].solidArea.x;
                int furnitureOriginalY = gp.furniture[i].solidArea.y;
                
                gp.furniture[i].solidArea.x = gp.furniture[i].worldX + gp.furniture[i].solidAreaDefaultX;
                gp.furniture[i].solidArea.y = gp.furniture[i].worldY + gp.furniture[i].solidAreaDefaultY;
                
                // System.out.println("DEBUG: Furniture[" + i + "] solid: (" + gp.furniture[i].solidArea.x + "," + gp.furniture[i].solidArea.y + ") size: " + gp.furniture[i].solidArea.width + "x" + gp.furniture[i].solidArea.height);
                
                // Cek collision
                if (player.solid.intersects(gp.furniture[i].solidArea)) {
                    if (gp.furniture[i].collision) {
                        player.collision = true;
                        index = i;
                        System.out.println("COLLISION DETECTED with furniture[" + i + "]!");
                    }
                }
                
                // reset posisi solid furniture
                gp.furniture[i].solidArea.x = furnitureOriginalX;
                gp.furniture[i].solidArea.y = furnitureOriginalY;
            }
        }
        
        // reset posisi solid player
        player.solid.x = originalX;
        player.solid.y = originalY;
        
        return index;
    }

    public void checkNPCCollision(Player player, NPC npcInHouse) {
        int originalX = player.solid.x;
        int originalY = player.solid.y;

        int playerHouseX = player.houseX - 384;
        int playerHouseY = player.houseY - 288;

        int predictedX = playerHouseX + player.solid.x;
        int predictedY = playerHouseY + player.solid.y;

        switch (player.direction) {
            case "up": predictedY -= gp.tileSize; break;
            case "down": predictedY += gp.tileSize; break;
            case "left": predictedX -= gp.tileSize; break;
            case "right": predictedX += gp.tileSize; break;
        }

        player.solid.x = predictedX;
        player.solid.y = predictedY;

        if (npcInHouse != null && npcInHouse.solidArea != null) {
            int npcOriginalX = npcInHouse.solidArea.x;
            int npcOriginalY = npcInHouse.solidArea.y;

            npcInHouse.solidArea.x = npcInHouse.worldX + npcInHouse.solidAreaDefaultX;
            npcInHouse.solidArea.y = npcInHouse.worldY + npcInHouse.solidAreaDefaultY;

            if (player.solid.intersects(npcInHouse.solidArea)) {
                player.collision = true;
                System.out.println("COLLISION DETECTED with " + npcInHouse.getName() + "!");
            }

            npcInHouse.solidArea.x = npcOriginalX;
            npcInHouse.solidArea.y = npcOriginalY;
        }

        player.solid.x = originalX;
        player.solid.y = originalY;
    }

    public Point getTileStepped(Player player) {
        int tileX = player.worldX / gp.tileSize;
        int tileY = player.worldY / gp.tileSize;
        return new Point(tileX, tileY);
}
}