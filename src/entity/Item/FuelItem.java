package entity.Item;

public class FuelItem extends Misc {
    private final int efficiency;

    public FuelItem(String name, double buyPrice, double sellPrice, int efficiency) {
        super(name, buyPrice, sellPrice);
        this.efficiency = efficiency;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public int calculateUnitsNeeded(int foodCount) {
        return (int) Math.ceil((double) foodCount / efficiency);
    }

    public static FuelItem getFuelFromName(String name) {
        return switch (name) {
            case "Coal" -> new FuelItem("Coal", 100, 75, 2);
            case "Firewood" -> new FuelItem("Firewood", 20, 10, 1);
            default -> null;
        };
    }
}