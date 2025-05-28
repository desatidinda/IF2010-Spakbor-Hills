package entity.Item;
import entity.Farm.Season;

public class Seeds extends Item{
    private Season season;
    private int daysToHarvest;
    private Double buyPrice;

    public Seeds(String itemName, Season season, int daysToHarvest, Double buyPrice){
        super(itemName, ItemType.SEEDS);
        this.season = season;
        this.daysToHarvest = daysToHarvest;
        this.buyPrice = buyPrice;
    }

    public Season getSeason(){
        return season;
    }

    public int getDaysToHarvest(){
        return daysToHarvest;
    }

    public Double getBuyPrice(){
        return buyPrice;
    }
}