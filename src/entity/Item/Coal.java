package entity.Item;

public class Coal extends FuelItem {
    public Coal(double buyPrice, double sellPrice) {
        super("Coal", buyPrice, sellPrice, 2);
    }
}