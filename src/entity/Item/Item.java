package entity.Item;

public abstract class Item {
    protected String itemName;
    protected ItemType itemType;

    public Item(String itemName, ItemType itemType){
        this.itemName = itemName;
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public static Item create(String itemName, ItemType itemType) {
        return ItemFactory.createItem(itemName);
    }

}