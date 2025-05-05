package entity.Item;

public class Crops extends Item{
    private int buyPrice;
    private int sellPrice;
    private int quantityPerHarvest;
    private int energyRestored;

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public int getQuantityPerHarvest() {
        return quantityPerHarvest;
    }

    public int getEnergyRestored() {
        return energyRestored;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setQuantityPerHarvest(int quantityPerHarvest) {
        this.quantityPerHarvest = quantityPerHarvest;
    }

    public void setEnergyRestored(int energyRestored) {
        this.energyRestored = energyRestored;
    }
    
}
