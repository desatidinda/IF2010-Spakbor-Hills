package entity.Item;

import java.util.EnumSet;
import main.GameClock.Season;

public class Seeds extends Item{
    private EnumSet<Season> seasons;
    private int daysToHarvest;
    private Double buyPrice;

    public Seeds(String itemName, EnumSet<Season> seasons, int daysToHarvest, Double buyPrice){
        super(itemName, "Seeds");
        this.seasons = seasons;
        this.daysToHarvest = daysToHarvest;
        this.buyPrice = buyPrice;
    }
    
    public EnumSet<Season> getSeason(){
        return seasons;
    }

    public int getDaysToHarvest(){
        return daysToHarvest;
    }

    public Double buyPrice(){
        return buyPrice;
    }
}