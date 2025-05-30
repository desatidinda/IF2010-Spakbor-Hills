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

    private static final Map<String, String> itemUnlockMap = Map.of(
            "Hot Pepper", "Fish Stew",
            "Pufferfish", "Fugu",
            "Legend", "The Legends of Spakbor"
    );

    public static String checkItemUnlock(String itemName) {
        if (itemUnlockMap.containsKey(itemName)) {
            String recipeName = itemUnlockMap.get(itemName);
            if (tryUnlock(recipeName)) {
                return recipeName;
            }
        }
        return null;
    }

    public static boolean checkFishUnlock(Inventory inventory) {
        int totalFish = getTotalFishCount(inventory);
        return totalFish >= 10 && tryUnlock("Sashimi");
    }

    public static int getTotalFishCount(Inventory inventory) {
        int totalFish = 0;
        Map<Item, Integer> items = inventory.getItems();

        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            if (fishItems.contains(entry.getKey().getItemName())) {
                totalFish += entry.getValue();
            }
        }
        return totalFish;
    }

    public static boolean checkHarvestUnlock() {
        if (!hasHarvested) {
            hasHarvested = true;
            tryUnlock("Veggie Soup");
            return true;
        }
        return false;
    }

    private static boolean tryUnlock(String recipeName) {
        if (unlockedFromItems.add(recipeName)) {
            RecipeRegistry.unlock(recipeName);
            return true;
        }
        return false;
    }

    public static boolean unlockFromPurchase(String recipeName) {
        return tryUnlock(recipeName);
    }

    public static boolean isUnlocked(String recipeName) {
        return unlockedFromItems.contains(recipeName);
    }

}