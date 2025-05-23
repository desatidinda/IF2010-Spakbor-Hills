package entity.Item;

public abstract class Misc extends Item{
    protected double buyPrice;
    protected double sellPrice;

    public Misc(String itemName, double buyPrice, double sellPrice){
        super(itemName, "Misc.");
        if (sellPrice >= buyPrice){
            throw new IllegalArgumentException("Sell price harus lebih kecil dari buy price!!!");
        }
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public double buyPrice(){
        return buyPrice;
    }

    public double sellPrice(){
        return sellPrice;
    }
}
