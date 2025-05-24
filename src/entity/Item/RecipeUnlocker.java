package entity.Item;

import java.util.Set;
import java.util.HashSet;

public class RecipeUnlocker {
    private static int fishCaught = 0;
    private static boolean hasHarvested = false;
    private static final Set<String> unlockedFromItems = new HashSet<>();

    public static void checkItemUnlock(String itemName) {
        switch (itemName) {
            case "Hot Pepper" -> tryUnlock("Fish Stew");
            case "Pufferfish" -> tryUnlock("Fugu");
            case "Legend" -> tryUnlock("The Legends of Spakbor");
            case "Fish n’ Chips Recipe" -> tryUnlock("Fish n’ Chips");
            case "Fish Sandwich Recipe" -> tryUnlock("Fish Sandwich");
        }
    }

    public static void checkFishUnlock() {
        fishCaught++;
        if (fishCaught >= 10) {
            tryUnlock("Sashimi");
        }
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
            System.out.println("Unlocked: " + recipeName);
        }
    }
}