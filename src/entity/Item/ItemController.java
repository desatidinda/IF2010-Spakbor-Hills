package entity.Item;

public class ItemController {
    public static void main(String[] args){
        Seeds parsnipSeed = new Seeds("Parsnip Seed", "Spring", 1, 20);
        Seeds cauliflowerSeed = new Seed("Cauliflower Seed", "Spring", 5, 80);

        Fish bullhead = new Fish("Bullhead", "Any", "Any", "Mountain Lake", "Common");

        Crops parsnip = new Crops("Parsnip", 50.0, 35, 1, 3);
        Crops potato = new Crops("Potato", null, 80, 1, 3);
        Crops wheat = new Crops("Wheat", 50.0, 30, 3, 3);

        Food fishNChips = new Food("Fish n' Chips", 50, 150.0, 135);
        Food fugu = new Food("Fugu", 50, null, 135);

        Equipment hoe = new Equipment("Hoe");
        Equipment wateringCan = new Equipment("Watering Can");
        Equipment pickaxe = new Equipment("Pickaxe");
        Equipment fishingRod = new Equipment("Fishing Rod");
    }
}
