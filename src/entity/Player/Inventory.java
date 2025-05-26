package entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Inventory {
    private final Map<String, Integer> items = new HashMap<>();

     Inventory() {
        initDebugInventory();
    }
    public void addItem(String itemName, int quantity) {
        items.put(itemName, items.getOrDefault(itemName, 0) + quantity);
    }

    public boolean hasItem(String itemName) {
        return items.getOrDefault(itemName, 0) > 0;
    }

    public boolean hasItem(String itemName, int quantity) {
        return items.getOrDefault(itemName, 0) >= quantity;
    }

    public void removeItem(String itemName) {
        removeItem(itemName, 1);
    }

    public void removeItem(String itemName, int quantity) {
        if (hasItem(itemName, quantity)) {
            items.put(itemName, items.get(itemName) - quantity);
        }
    }

    public int getItemCount(String itemName) {
        return items.getOrDefault(itemName, 0);
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

    private void initDebugInventory() {
        String[] seedItems = {
                "Wheat", "Tomato", "Pumpkin", "Potato", "Parsnip",
                "Hot Pepper", "Egg", "Melon", "Blueberry", "Cranberry",
                "Grape", "Cauliflower", "Salmon", "Pufferfish", "Legend",
                "Any Fish", "Coal", "Firewood"
        };

        Random rand = new Random();
        for (String item : seedItems) {
            int qty = 2 + rand.nextInt(4); // 2-5 item acak
            addItem(item, qty);
        }

        printContents();
    }
}