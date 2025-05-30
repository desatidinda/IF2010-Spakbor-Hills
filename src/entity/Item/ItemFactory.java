package entity.Item;

public class ItemFactory {

    public static Item createItem(String itemName) {
        for (Seeds seed : SeedsData.ALL_SEEDS) {
            if (seed.getItemName().equalsIgnoreCase(itemName)) {
                return seed;
            }
        }

        for (Crops crop : CropsData.ALL_CROPS) {
            if (crop.getItemName().equalsIgnoreCase(itemName)) {
                return crop;
            }
        }

        for (Fish fish : FishData.ALL_FISH) {
            if (fish.getName().equalsIgnoreCase(itemName)) {
                return new Fish(
                    fish.getName(),
                    fish.getType(),
                    fish.getSeasons(),
                    fish.getTimeRange(),
                    fish.getWeathers(),
                    fish.getLocations()
                );
            }
        }

        for (Misc misc : MiscData.ALL_MISC) {
            if (misc.getItemName().equalsIgnoreCase(itemName)) {
                if (itemName.endsWith("Recipe")) {
                    String recipeName = itemName.replace(" Recipe", "");
                    RecipeRegistry.unlock(recipeName);
                }
                return misc;
            }
        }

        for (Food food : FoodData.ALL_FOOD) {
            if (food.getItemName().equalsIgnoreCase(itemName)) {
                return food;
            }
        }

        throw new IllegalArgumentException("Item tidak ditemukan: " + itemName);
    }
}