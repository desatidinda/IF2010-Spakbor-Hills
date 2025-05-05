package entity.Item;

public class Food extends Item{
    private int energyRestored;
    private int buyPrice;
    private int sellPrice;

    public int getEnergyRestored() {
        return energyRestored;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setEnergyRestored(int energyRestored) {
        this.energyRestored = energyRestored;
    }
    
    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

}
