package entity.Item;

import entity.Farm.Season;
import java.util.List;

public class SeedsData {
    public static final List<Seeds> ALL_SEEDS = List.of(
        //####################################### SPRING #######################################
        new Seeds("Parsnip Seeds",  Season.SPRING, 1,  20.0),
        new Seeds("Cauliflower Seeds", Season.SPRING, 5,  80.0),
        new Seeds("Potato Seeds",    Season.SPRING, 3,  50.0),
        new Seeds("Wheat Seeds",     Season.SPRING, 1,  60.0),

        //####################################### SUMMER #######################################
        new Seeds("Blueberry Seeds", Season.SUMMER, 7,  80.0),
        new Seeds("Tomato Seeds",    Season.SUMMER, 3,  50.0),
        new Seeds("Hot Pepper Seeds",Season.SUMMER, 1,  40.0),
        new Seeds("Melon Seeds",     Season.SUMMER, 4,  80.0),

        //####################################### FALL #######################################
        new Seeds("Cranberry Seeds", Season.FALL,   2, 100.0),
        new Seeds("Pumpkin Seeds",   Season.FALL,   7, 150.0),
        new Seeds("Wheat Seeds",     Season.FALL,   1,  60.0),
        new Seeds("Grape Seeds",     Season.FALL,   3,  60.0)

    );
}