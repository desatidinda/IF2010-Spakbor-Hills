package entity.Item;

public abstract class Misc extends Item{
    private double buyPrice;
    private double sellPrice;

    public Misc(String miscName, double buyPrice, double sellPrice){
        super(miscName, "Misc.");
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
