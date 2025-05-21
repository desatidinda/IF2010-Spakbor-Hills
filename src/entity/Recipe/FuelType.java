package entity.recipe;

public enum FuelType {
    FIREWOOD(1), COAL(2), GAS(3);

    private final int efficiency;

    FuelType(int efficiency) {
        this.efficiency = efficiency;
    }

    public int getEfficiency() {
        return efficiency;
    }
}
