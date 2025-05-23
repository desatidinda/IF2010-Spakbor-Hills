package entity.NPC;

import java.util.Arrays;
import java.util.List;

public class Caroline extends NPC {

    private final List<String> lovedItemNames = Arrays.asList(
            "Firewood", "Coal"
    );

    private final List<String> likedItemNames = Arrays.asList(
            "Potato", "Wheat"
    );

    private final List<String> hatedItemNames = Arrays.asList(
            "Hot Pepper"
    );

    public Caroline() {
        super("Caroline");
    }

    @Override
    public void chat() {
        System.out.println("Caroline: Seni itu adalah daur ulang yang indah!");
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