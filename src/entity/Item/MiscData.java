package entity.Item;

import java.util.List;

public class MiscData {
    public static final List<Misc> ALL_MISC = List.of(
        new Firewood(20, 10),
        new Coal(100, 75),
        new Misc("Wood", 100.0, 50.0)

        //nanti tambahin lg aja
    );
}