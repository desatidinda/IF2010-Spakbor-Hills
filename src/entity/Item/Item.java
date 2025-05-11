package entity.Item;

public abstract class Item {
    private String itemName;
    private String itemType;

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public boolean isEdible() {
        return false;
    }
    
}
