package entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import entity.Item.*;



public class Inventory {
    private final Map<Item, Integer> items = new HashMap<>();
    private final Set<Item> unlimitedTools = new HashSet<>();

    public Inventory() {
        // ini yg default itu bakalan unlimited
        unlimitedTools.add(new Equipment("Hoe"));
        unlimitedTools.add(new Equipment("Watering Can"));
        unlimitedTools.add(new Equipment("Pickaxe"));
        unlimitedTools.add(new Equipment("Fishing Rod"));
    }

    public void addItem(Item item, int quantity) {
        if (!unlimitedTools.contains(item)) {
            items.put(item, items.getOrDefault(item, 0) + quantity);
        }
        RecipeUnlocker.checkItemUnlock(item.getItemName());
        RecipeUnlocker.checkFishUnlock(this);
    }

    public boolean hasItem(Item item) {
        if (unlimitedTools.contains(item)) {
            return true;
        }
        return items.getOrDefault(item, 0) > 0;
    }

    public boolean hasItem(Item item, int quantity) {
        if (unlimitedTools.contains(item)) {
            return true;
        }
        return items.getOrDefault(item, 0) >= quantity;
    }

    public void removeItem(Item item) {
        removeItem(item, 1);
    }

    public void removeItem(Item item, int quantity) {
        if (!unlimitedTools.contains(item) && hasItem(item, quantity)) {
            items.put(item, items.get(item) - quantity);
        }

    }

    public int getItemCount(Item item) {
        if (unlimitedTools.contains(item)) {
            return -1;
        }
        return items.getOrDefault(item, 0);
    }

    public boolean isUnlimitedTool(Item item) {
        return unlimitedTools.contains(item);
    }

    public void printContents() {
        if (items.isEmpty()) {
            System.out.println("Inventory kosong.");
        } else {
            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                System.out.println(entry.getKey().getItemName() + ": " + entry.getValue());
            }
        }
    }

    public Map<Item, Integer> getItems() {
        Map<Item, Integer> displayItems = new HashMap<>(items);
        for (Item tool : unlimitedTools) {
            displayItems.put(tool, -1);
        }
        
        return displayItems;
    }
    
    public Set<Item> getUnlimitedTools() {
        return unlimitedTools;
    }

    public Set<Item> getAvailableSeeds() {
        Set<Item> seeds = new HashSet<>();
        for (Item item : items.keySet()) {
            if (item.getItemName().endsWith("Seeds")) {
                seeds.add(item);
            }
        }
        return seeds;
    }
}