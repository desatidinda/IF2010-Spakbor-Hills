package entity.Item;

public class Misc extends Item{
    protected double buyPrice;
    protected double sellPrice;

    public Misc(String itemName, double buyPrice, double sellPrice){
        super(itemName, ItemType.MISC);
        if (sellPrice >= buyPrice){
            throw new IllegalArgumentException("Sell price harus lebih kecil dari buy price!!!");
        }
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public double getBuyPrice(){
        return buyPrice;
    }

    public double getSellPrice(){
        return sellPrice;
    }
}