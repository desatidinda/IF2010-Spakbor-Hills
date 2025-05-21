package entity.NPC;

import java.util.Arrays;
import java.util.List;

public class Perry extends NPC {

    private final List<String> lovedItemNames = Arrays.asList(
            "Cranberry", "Blueberry"
    );

    private final List<String> likedItemNames = Arrays.asList(
            "Wine"
    );

    public Perry() {
        super("Perry");
    }

    @Override
    public void chat() {
        System.out.println("Perry: Aku sedang menyelesaikan bab terakhir novelku.");
    }

    @Override
    public void reactToGift(String itemName) {
        String lowerName = itemName.toLowerCase();

        if (lovedItemNames.contains(itemName)) {
            addHeartPoints(25);
        } else if (likedItemNames.contains(itemName)) {
            addHeartPoints(20);
        } else if (lowerName.contains("fish") || lowerName.contains("salmon") || lowerName.contains("catfish") || lowerName.contains("sardine")) {
            addHeartPoints(-25); // fish-related, assumed hated
        }
    }
}