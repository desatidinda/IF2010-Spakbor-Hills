package entity.Item;

import java.util.List;

public class FoodData {
    public static final List<Food> ALL_FOOD = List.of(
        new Food("Wine", 0, 100.0, 90),
        new Food("Pumpkin Pie", 40, 120.0, 100),
        new Food("Baguette", 20, 100.0, 80),
        new Food("Fugu", 10, 0.0, 135),
        new Food("Fish Sandwich", 25, 200.0, 180),
        new Food("Fish Stew", 35, 280.0, 260),
        new Food("Fish n’ Chips", 30, 150.0, 135),
        new Food("Cooked Pig's Head", 60, 1000.0, 0),
        new Food("Spakbor Salad", 50, 0.0, 250),
        new Food("The Legends of Spakbor", 50, 0.0, 2000)
        // tambahin lg aja nnt
    );
}