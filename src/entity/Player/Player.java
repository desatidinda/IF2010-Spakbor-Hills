package entity.Player;

import entity.Item.Fish;
import entity.Item.Item;
import entity.NPC.NPC;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private final int MAX_ENERGY = 100;
    private int energy =  MAX_ENERGY;
    private String name;
    private String gender;
    private String farmName;
    private NPC partner = null;
    private int gold = 0;
    private ArrayList<Item> inventory = new ArrayList<>();
    private Point location;
    private Map<Fish, Integer> fishCaught = new HashMap<>();
    private int cropsHarvested;

    public Player(String name, String gender, String farmName, Point startingLocation) {
        this.name = name;
        this.gender = gender;
        this.farmName = farmName;
        this.location = startingLocation;
        this.energy = MAX_ENERGY;
    }

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

    public NPC getPartner() {
        return partner;
    }

    public int getGold() {
        return gold;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public Point getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setPartner(NPC npc) {
        this.partner = npc;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
 
    public void setLocation(Point point) {
        this.location = point;
    }

    

}
