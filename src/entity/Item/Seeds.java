package entity.Item;

public class Seeds extends Item{
    private String season;
    private int daysToHarvest;
    private double buyPrice;

    public Seeds(String seedName, String season, int daysToHarvest, double buyPrice){
        super(seedName, "Seeds");
        this.season = season;
        this.daysToHarvest = daysToHarvest;
        this.buyPrice = buyPrice;
    }
    
    public String getSeason(){
        return season;
    }

    public int getDaysToHarvest(){
        return daysToHarvest;
    }

    public double buyPrice(){
        return buyPrice;
    }
}
