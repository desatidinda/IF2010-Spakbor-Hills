package entity.Item;

import entity.Farm.Season;
import java.util.List;

public class CropsData {
    public static final List<Crops> ALL_CROPS = List.of(
        //####################################### SPRING #######################################
        new Crops("Parsnip",  Season.SPRING, 1,  50, 35),
        new Crops("Cauliflower", Season.SPRING, 5,  200, 150),
        new Crops("Potato",    Season.SPRING, 3,  0, 80),
        new Crops("Wheat",     Season.SPRING, 1,  50, 30),

        //####################################### SUMMER #######################################
        new Crops("Blueberry", Season.SUMMER, 7,  150, 40),
        new Crops("Tomato",    Season.SUMMER, 3,  90, 60),
        new Crops("Hot Pepper",Season.SUMMER, 1,  40, 20),
        new Crops("Melon",     Season.SUMMER, 4,  0, 40),

        //####################################### FALL #######################################
        new Crops("Cranberry", Season.FALL,   2, 0, 25),
        new Crops("Pumpkin",   Season.FALL,   7, 300, 250),
        new Crops("Wheat",     Season.FALL,   1,  50, 30),
        new Crops("Grape",     Season.FALL,   3,  100, 10)

    );
}
