package entity.Item;

public abstract class Item {
    protected String itemName;
    protected String itemType;

    public Item(String itemName, String itemType){
        this.itemName = itemName;
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }
    
}
