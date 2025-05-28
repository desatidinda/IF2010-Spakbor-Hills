package entity.Item;

import entity.Farm.Season;
import java.util.List;

public class CropsData {
    public static final List<Crops> ALL_CROPS = List.of(
        //####################################### SPRING #######################################
        new Crops("Parsnip",  Season.SPRING, 1,  20, 10),
        new Crops("Cauliflower", Season.SPRING, 5,  80, 40),
        new Crops("Potato",    Season.SPRING, 3,  50, 25),
        new Crops("Wheat",     Season.SPRING, 1,  60, 30),

        //####################################### SUMMER #######################################
        new Crops("Blueberry", Season.SUMMER, 7,  80, 40),
        new Crops("Tomato",    Season.SUMMER, 3,  50, 25),
        new Crops("Hot Pepper",Season.SUMMER, 1,  40, 20),
        new Crops("Melon",     Season.SUMMER, 4,  80, 40),

        //####################################### FALL #######################################
        new Crops("Cranberry", Season.FALL,   2, 100, 50),
        new Crops("Pumpkin",   Season.FALL,   7, 150, 75),
        new Crops("Wheat",     Season.FALL,   1,  60, 30),
        new Crops("Grape",     Season.FALL,   3,  60, 30)

    );
}
