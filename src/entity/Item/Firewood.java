package entity.Item;

public class Firewood extends FuelItem {
    public Firewood(double buyPrice, double sellPrice) {
        super("Firewood", buyPrice, sellPrice, 1);
    }
}