package entity.Item;

public class Seeds extends Item{
    private String season;
    private int daysToHarvest;
    private Double buyPrice;

    public Seeds(String itemName, String season, int daysToHarvest, Double buyPrice){
        super(itemName, "Seeds");
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

    public Double buyPrice(){
        return buyPrice;
    }
}
