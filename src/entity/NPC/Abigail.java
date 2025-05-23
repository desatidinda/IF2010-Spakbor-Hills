package entity.NPC;

import java.util.Arrays;
import java.util.List;

public class Abigail extends NPC {

    private final List<String> lovedItemNames = Arrays.asList(
            "Blueberry", "Melon", "Pumpkin", "Grape", "Cranberry"
    );

    private final List<String> likedItemNames = Arrays.asList(
            "Baguette", "Pumpkin Pie", "Wine"
    );

    private final List<String> hatedItemNames = Arrays.asList(
            "Hot Pepper", "Cauliflower", "Parsnip", "Wheat"
    );

    public Abigail() {
        super("Abigail");
    }

    @Override
    public void chat() {
        System.out.println("Abigail: Ayo eksplorasi alam, energiku harus penuh!");
    }

    @Override
    public void reactToGift(String itemName) {
        if (lovedItemNames.contains(itemName)) {
            addHeartPoints(25);
        } else if (likedItemNames.contains(itemName)) {
            addHeartPoints(20);
        } else if (hatedItemNames.contains(itemName)) {
            addHeartPoints(-25);
        }
    }
}