package entity.Item;

import entity.Farm.Season;
import entity.Farm.Weather;
import java.util.List;

public class FishData {
    public static final List<Fish> ALL_FISH = List.of(
// ########################################## COMMON FISH ##########################################
        new Fish("Bullhead", FishType.COMMON,
            List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),
            "00:00-23:59", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Mountain Lake")),

        new Fish("Carp", FishType.COMMON,
            List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),
            "00:00-23:59", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Mountain Lake", "Pond")),

        new Fish("Chub", FishType.COMMON,
            List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),
            "00:00-23:59", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Forest River", "Mountain Lake")),

// ########################################## REGULAR FISH ##########################################
        new Fish("Halibut", FishType.REGULAR,
            List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),
            "06:00-11:00,19:00-02:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Ocean")),

        new Fish("Catfish", FishType.REGULAR,
            List.of(Season.SPRING, Season.SUMMER, Season.FALL),
            "06:00-22:00", List.of(Weather.RAINY),
            List.of("Forest River", "Pond")),

        new Fish("Flounder", FishType.REGULAR,
            List.of(Season.SPRING, Season.SUMMER),
            "06:00-22:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Ocean")),

        new Fish("Large Mouth Bass", FishType.REGULAR,
            List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),
            "06:00-18:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Mountain Lake")),

        new Fish("Rainbow Trout", FishType.REGULAR,
            List.of(Season.SUMMER),
            "06:00-18:00", List.of(Weather.SUNNY),
            List.of("Forest River", "Mountain Lake")),

        new Fish("Sturgeon", FishType.REGULAR,
            List.of(Season.SUMMER, Season.WINTER),
            "06:00-18:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Mountain Lake")),

        new Fish("Midnight Carp", FishType.REGULAR,
            List.of(Season.WINTER, Season.FALL),
            "20:00-02:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Mountain Lake", "Pond")),

        new Fish("Octopus", FishType.REGULAR,
            List.of(Season.SUMMER),
            "06:00-22:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Ocean")),

        new Fish("Pufferfish", FishType.REGULAR,
            List.of(Season.SUMMER),
            "00:00-16:00", List.of(Weather.SUNNY),
            List.of("Ocean")),

        new Fish("Sardine", FishType.REGULAR,
            List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),
            "06:00-18:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Ocean")),

        new Fish("Super Cucumber", FishType.REGULAR,
            List.of(Season.SUMMER, Season.FALL, Season.WINTER),
            "18:00-02:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Ocean")),

        new Fish("Salmon", FishType.REGULAR,
            List.of(Season.FALL),
            "06:00-18:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Forest River")),

// ########################################## LEGENDARY FISH ##########################################
        new Fish("Legend", FishType.LEGENDARY,
            List.of(Season.SPRING),
            "08:00-20:00", List.of(Weather.RAINY),
            List.of("Mountain Lake")),

        new Fish("Angler", FishType.LEGENDARY,
            List.of(Season.FALL),
            "08:00-20:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Pond")),

        new Fish("Crimsonfish", FishType.LEGENDARY,
            List.of(Season.SUMMER),
            "08:00-20:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Ocean")),

        new Fish("Glacierfish", FishType.LEGENDARY,
            List.of(Season.WINTER),
            "08:00-20:00", List.of(Weather.SUNNY, Weather.RAINY),
            List.of("Forest River"))
    );
}
