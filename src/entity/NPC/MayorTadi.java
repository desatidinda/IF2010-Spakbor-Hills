package entity.NPC;

import java.util.Arrays;
import java.util.List;

public class MayorTadi extends NPC {

    private final List<String> lovedItemNames = Arrays.asList(
            "Legend"
    );

    private final List<String> likedItemNames = Arrays.asList(
            "Angler", "Crimsonfish", "Glacierfish"
    );

    public MayorTadi() {
        super("Mayor Tadi");
    }

    @Override
    public void chat() {
        System.out.println("Mayor Tadi: Rakyatku, berikan aku barang-barang langka!");
    }

    @Override
    public void reactToGift(String itemName) {
        if (lovedItemNames.contains(itemName)) {
            addHeartPoints(25);
        } else if (likedItemNames.contains(itemName)) {
            addHeartPoints(20);
        } else {
            addHeartPoints(-25); // Semua item lain dianggap hated
        }
    }
}