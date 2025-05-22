package entity.Item;

public class Food extends Item{
    private int energyRestored;
    private double buyPrice;
    private double sellPrice;

    public Food(String foodName, int energyRestored, double buyPrice, double sellPrice){
        super(foodName, "Food");
        this.energyRestored = energyRestored;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public int getEnergyRestored() {
        return energyRestored;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

}
