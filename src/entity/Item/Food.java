package entity.Item;

public class Food extends Item{
    private int energyRestored;
    private Double buyPrice;
    private double sellPrice;

    public Food(String itemName, int energyRestored, Double buyPrice, double sellPrice){
        super(itemName, ItemType.FOOD);
        this.energyRestored = energyRestored;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public int getEnergyRestored() {
        return energyRestored;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

}