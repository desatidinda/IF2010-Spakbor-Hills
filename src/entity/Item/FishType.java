package entity.Item;

public enum FishType {
    COMMON(10),
    REGULAR(5),
    LEGENDARY(25);

    private final int basePrice;

    FishType(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getBasePrice() {
        return basePrice;
    }
}
