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
        }
    }

    public void printContents() {
        if (items.isEmpty()) {
            System.out.println("Inventory kosong.");
        } else {
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public Map<String, Integer> getItems() {
        return items;
    }
}


