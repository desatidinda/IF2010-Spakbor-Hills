package entity.Item;

public class Crops extends Item{
    private Double buyPrice;
    private double sellPrice;
    private int quantityPerHarvest;
    private int energyRestored;

    public Crops(String itemName, Double buyPrice, double sellPrice, int quantityPerHarvest, int energyRestored){
        super(itemName, "Crops");
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.quantityPerHarvest = quantityPerHarvest;
        this.energyRestored = energyRestored;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public int getQuantityPerHarvest() {
        return quantityPerHarvest;
    }

    public int getEnergyRestored() {
        return energyRestored;
    }
    
}
