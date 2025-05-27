package entity.Item;

import entity.Farm.Season;

public class Crops extends Item{
    private Season season;
    private int dayToHarvest;
    private int buyPrice;
    private int sellPrice;
    
    public Crops(String seedName, Season season, int dayToHarvest, int buyPrice, int sellPrice){
        super(seedName, "Crops");
        this.dayToHarvest = dayToHarvest;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.season = season;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public int getdayToHarvest() {
        return dayToHarvest;
    }

    public Season getSeason() {
        return season;
    }
}