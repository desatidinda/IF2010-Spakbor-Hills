package entity.recipe;

import java.util.Map;

public class Recipe {
    private final String id;
    private final String name;
    private final Map<String, Integer> ingredients;
    private final int requiredFuel;
    private boolean unlocked;

    public Recipe(String id, String name, Map<String, Integer> ingredients, int requiredFuel, boolean unlocked) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.requiredFuel = requiredFuel;
        this.unlocked = unlocked;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Map<String, Integer> getIngredients() { return ingredients; }
    public int getRequiredFuel() { return requiredFuel; }
    public boolean isUnlocked() { return unlocked; }
    public void unlock() { this.unlocked = true; }
}