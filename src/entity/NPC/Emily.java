package entity.NPC;

import java.util.Arrays;
import java.util.List;

public class Emily extends NPC {

    private final List<String> likedItemNames = Arrays.asList(
            "Catfish", "Salmon", "Sardine"
    );

    private final List<String> hatedItemNames = Arrays.asList(
            "Coal", "Wood"
    );

    public Emily() {
        super("Emily");
    }

    @Override
    public void chat() {
        System.out.println("Emily: Aku senang memasak dengan bahan segar dari kebun!");
    }

    @Override
    public void reactToGift(String itemName) {
        if (itemName.toLowerCase().contains("seed")) {
            addHeartPoints(25);
        } else if (likedItemNames.contains(itemName)) {
            addHeartPoints(20);
        } else if (hatedItemNames.contains(itemName)) {
            addHeartPoints(-25);
        }
    }
}