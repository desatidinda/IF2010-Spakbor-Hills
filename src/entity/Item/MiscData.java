package entity.Item;

import java.util.List;

public class MiscData {
    public static final List<Misc> ALL_MISC = List.of(
        new Firewood(20, 10),
        new Coal(100, 75),
        new Misc("Wood", 100.0, 50.0),
        new Misc("Eggplant", 20.0, 10.0),
        new Misc("Fish n’ Chips Recipe", 300.0, 0.0),
        new Misc("Fish Sandwich Recipe", 300.0,0.0),
        new Misc("Egg",20.0,10.0),
        new Misc("Proposal Ring", 3000.0,0.0)
    );
}