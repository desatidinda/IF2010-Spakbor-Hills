package entity.Item;

import java.util.List;
import entity.Farm.Season;

public class CropsData {
    public static final List<Crops> ALL_CORPS = List.of(
        //####################################### SPRING #######################################
        new Crops("Parsnip Seeds",  Season.SPRING, 1,  20, 10),
        new Crops("Cauliflower Seeds", Season.SPRING, 5,  80, 40),
        new Crops("Potato Seeds",    Season.SPRING, 3,  50, 25),
        new Crops("Wheat Seeds",     Season.SPRING, 1,  60, 30),

        //####################################### SUMMER #######################################
        new Crops("Blueberry Seeds", Season.SUMMER, 7,  80, 40),
        new Crops("Tomato Seeds",    Season.SUMMER, 3,  50, 25),
        new Crops("Hot Pepper Seeds",Season.SUMMER, 1,  40, 20),
        new Crops("Melon Seeds",     Season.SUMMER, 4,  80, 40),

        //####################################### FALL #######################################
        new Crops("Cranberry Seeds", Season.FALL,   2, 100, 50),
        new Crops("Pumpkin Seeds",   Season.FALL,   7, 150, 75),
        new Crops("Wheat Seeds",     Season.FALL,   1,  60, 30),
        new Crops("Grape Seeds",     Season.FALL,   3,  60, 30)

    );
}
