package entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<String, Integer> items = new HashMap<>();

    public void addItem(String itemName) {
        items.put(itemName, items.getOrDefault(itemName, 0) + 1);
    }

    public boolean hasItem(String itemName) {
        return items.getOrDefault(itemName, 0) > 0;
    }

    public void removeItem(String itemName) {
        if (hasItem(itemName)) {
            items.put(itemName, items.get(itemName) - 1);
            if (items.get(itemName) <= 0) {
                items.remove(itemName);
            }
        }
    }

    public Map<String, Integer> getItems() {
        return items;
    }
    
    public void printInventory() {
        if (items.isEmpty()) {
            System.out.println("(Kosong)");
        } else {
            System.out.println("Inventaris:");
            items.entrySet().forEach(entry ->
                System.out.println("- " + entry.getKey() + " x" + entry.getValue()));
        }
    }
}
