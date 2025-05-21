package entity.recipe;

public class Fuel {
    private final FuelType type;
    private int availableUnit;

    public Fuel(FuelType type, int availableUnit) {
        this.type = type;
        this.availableUnit = availableUnit;
    }

    public FuelType getType() {
        return type;
    }

    public int getAvailableUnit() {
        return availableUnit;
    }

    // Fuel efficiency: 1 COAL = 2 makanan, 1 FIREWOOD = 1 makanan
    public boolean useFuel(int requiredFoodCount) {
        int totalCapacity = type.getEfficiency() * availableUnit;

        if (totalCapacity < requiredFoodCount) {
            return false; // fuel tidak cukup
        }

        // Hitung berapa unit yang dibutuhkan
        int unitsToUse = (int) Math.ceil((double) requiredFoodCount / type.getEfficiency());
        availableUnit -= unitsToUse;
        return true;
    }

    public boolean isEnough(int requiredFoodCount) {
        return (type.getEfficiency() * availableUnit) >= requiredFoodCount;
    }
}