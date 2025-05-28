package entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Random;


public class Inventory {
    private final Map<String, Integer> items = new HashMap<>();
    private final Set<String> unlimitedTools = new HashSet<>();

    public Inventory() {
        // ini yg default itu bakalan unlimited
        unlimitedTools.add("Hoe");
        unlimitedTools.add("Watering Can");
        unlimitedTools.add("Pickaxe");
        unlimitedTools.add("Fishing Rod");

        initDebugInventory();
    }

    public void addItem(String itemName, int quantity) {
        if (!unlimitedTools.contains(itemName)) {
            items.put(itemName, items.getOrDefault(itemName, 0) + quantity);
        }
    }

    public boolean hasItem(String itemName) {
        if (unlimitedTools.contains(itemName)) {
            return true;
        }
        return items.getOrDefault(itemName, 0) > 0;
    }

    public boolean hasItem(String itemName, int quantity) {
        if (unlimitedTools.contains(itemName)) {
            return true;
        }
        return items.getOrDefault(itemName, 0) >= quantity;
    }

    public void removeItem(String itemName) {
        removeItem(itemName, 1);
    }

    public void removeItem(String itemName, int quantity) {
        if (!unlimitedTools.contains(itemName) && hasItem(itemName, quantity)) {
            items.put(itemName, items.get(itemName) - quantity);
        }

    }

    public int getItemCount(String itemName) {
        if (unlimitedTools.contains(itemName)) {
            return -1;
        }
        return items.getOrDefault(itemName, 0);
    }

    public boolean isUnlimitedTool(String itemName) {
        return unlimitedTools.contains(itemName);
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

    public Map<String, Integer> getItems() {
        Map<String, Integer> displayItems = new HashMap<>(items);
        for (String tool : unlimitedTools) {
            displayItems.put(tool, -1);
        }

        return displayItems;
    }

    public Set<String> getUnlimitedTools() {
        return unlimitedTools;
    }

    public Set<String> getAvailableSeeds() {
        Set<String> seeds = new HashSet<>();
        for (String item : items.keySet()) {
            if (item.endsWith("Seeds")) {
                seeds.add(item);
            }
        }
        return seeds;
    }
}