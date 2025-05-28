package entity.Item;

import entity.Farm.Season;

public class Crops extends Item{
    private Season season;
    private int dayToHarvest;
    private double buyPrice;
    private double sellPrice;

    public Crops(String seedName, Season season, int dayToHarvest, double buyPrice, double sellPrice){
        super(seedName, ItemType.CROPS);
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