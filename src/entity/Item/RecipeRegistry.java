package entity.Item;

import java.util.*;

public class RecipeRegistry {
    private static final Map<String, Recipe> recipes = new HashMap<>();

    static {
        loadRecipes();
    }

    private static void loadRecipes() {
        recipes.put("Fish n’ Chips", new Recipe(
                "recipe_1", "Fish n’ Chips",
                Map.of("Any Fish", 2, "Wheat", 1, "Potato", 1),
                1, false
        ));

        recipes.put("Baguette", new Recipe(
                "recipe_2", "Baguette",
                Map.of("Wheat", 3),
                1, true
        ));

        recipes.put("Sashimi", new Recipe(
                "recipe_3", "Sashimi",
                Map.of("Salmon", 3),
                1, false
        ));

        recipes.put("Fugu", new Recipe(
                "recipe_4", "Fugu",
                Map.of("Pufferfish", 1),
                1, false
        ));

        recipes.put("Wine", new Recipe(
                "recipe_5", "Wine",
                Map.of("Grape", 2),
                1, true
        ));

        recipes.put("Pumpkin Pie", new Recipe(
                "recipe_6", "Pumpkin Pie",
                Map.of("Egg", 1, "Wheat", 1, "Pumpkin", 1),
                1, true
        ));

        recipes.put("Veggie Soup", new Recipe(
                "recipe_7", "Veggie Soup",
                Map.of("Cauliflower", 1, "Parsnip", 1, "Potato", 1, "Tomato", 1),
                2, false
        ));

        recipes.put("Fish Stew", new Recipe(
                "recipe_8", "Fish Stew",
                Map.of("Any Fish", 2, "Hot Pepper", 1, "Cauliflower", 2),
                2, false
        ));

        recipes.put("Spakbor Salad", new Recipe(
                "recipe_9", "Spakbor Salad",
                Map.of("Melon", 1, "Cranberry", 1, "Blueberry", 1, "Tomato", 1),
                2, true
        ));

        recipes.put("Fish Sandwich", new Recipe(
                "recipe_10", "Fish Sandwich",
                Map.of("Any Fish", 1, "Wheat", 2, "Tomato", 1, "Hot Pepper", 1),
                2, false
        ));

        recipes.put("The Legends of Spakbor", new Recipe(
                "recipe_11", "The Legends of Spakbor",
                Map.of("Legend", 1, "Potato", 2, "Parsnip", 1, "Tomato", 1, "Eggplant", 1),
                3, false
        ));
    }

    public static Recipe get(String name) {
        return recipes.get(name);
    }

    public static List<Recipe> getAll() {
        return new ArrayList<>(recipes.values());
    }

    public static void unlock(String name) {
        Recipe recipe = recipes.get(name);
        if (recipe != null) {
            recipe.unlock();
        }
    }

    public static List<Recipe> getUnlockedRecipes() {
        return recipes.values().stream()
                .filter(Recipe::isUnlocked)
                .toList();
    }
}