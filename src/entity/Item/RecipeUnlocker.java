package entity.Item;

import entity.Player.Inventory;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeUnlocker {
    private static boolean hasHarvested = false;
    private static final Set<String> unlockedFromItems = new HashSet<>();
    
    private static final Set<String> fishItems = FishData.ALL_FISH.stream().map(Fish::getName).collect(Collectors.toSet());

    public static void checkItemUnlock(String itemName) {
        switch (itemName) {
            case "Hot Pepper" -> tryUnlock("Fish Stew");
            case "Pufferfish" -> tryUnlock("Fugu");
            case "Legend" -> tryUnlock("The Legends of Spakbor");
            // case "Fish n' Chips Recipe" -> tryUnlock("Fish n' Chips");
            // case "Fish Sandwich Recipe" -> tryUnlock("Fish Sandwich");
        }
    }

    public static void checkFishUnlock(Inventory inventory) {
        int totalFish = getTotalFishCount(inventory);
        if (totalFish >= 10) {
            tryUnlock("Sashimi");
        }
    }

    private static int getTotalFishCount(Inventory inventory) {
        int totalFish = 0;
        Map<Item, Integer> items = inventory.getItems();

        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            if (fishItems.contains(entry.getKey().getItemName())) {
                totalFish += entry.getValue();
            }
        }
        return totalFish;
    }

    public static void checkHarvestUnlock() {
        if (!hasHarvested) {
            hasHarvested = true;
            tryUnlock("Veggie Soup");
        }
    }

    private static void tryUnlock(String recipeName) {
        if (unlockedFromItems.add(recipeName)) {
            RecipeRegistry.unlock(recipeName);
            //System.out.println("Unlocked: " + recipeName);
        }
    }
}