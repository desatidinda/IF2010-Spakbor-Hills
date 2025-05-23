package entity.Item;

import entity.Farm.Season;
import entity.Farm.Weather;
import java.util.List;
import main.GameClock;

public class Fish extends Item{
    private String name;
    private FishType type;
    private List<Season> seasons;
    private String timeRange; // formatnya "06:00-18:00"
    private List<Weather> weathers;
    private List<String> locations;

    public Fish(String name, FishType type, List<Season> seasons, String timeRange, List<Weather> weathers, List<String> locations) {
        this.name = name;
        this.type = type;
        this.seasons = seasons;
        this.timeRange = timeRange;
        this.weathers = weathers;
        this.locations = locations;
    }

    public boolean isAvailable() {
        int currentMinutes = GameClock.getHour() * 60 + GameClock.getMinute();
        boolean match = seasons.contains(GameClock.getCurrentSeason()) &&
                        isTimeInRange(currentMinutes, timeRange) &&
                        weathers.contains(GameClock.getCurrentWeather());

        // System.out.println("[DEBUG] Cek ikan: " + name);
        // System.out.println(" - Season cocok : " + seasons.contains(GameClock.getCurrentSeason()));
        // System.out.println(" - Weather cocok: " + weathers.contains(GameClock.getCurrentWeather()));
        // System.out.println(" - Time cocok   : " + isTimeInRange(currentMinutes, timeRange));
        // System.out.println(" - => RESULT    : " + match);

        return match;
    }


    public String getName() {
        return name;
    }

    public FishType getType() {
        return type;
    }

    public List<String> getLocations() {
        return locations;
    }

    private static boolean isTimeInRange(int now, String rangeText) {
        String[] ranges = rangeText.split(",");
        for (String range : ranges) {
            String[] parts = range.trim().split("-");
            if (parts.length != 2) continue;

            int start = parseTimeToMinutes(parts[0]);
            int end = parseTimeToMinutes(parts[1]);

            // handle waktu malem ke pagi (misalnya: 20:00 - 02:00)
            if (start > end) {
                if (now >= start || now <= end) return true;
            } else {
                if (now >= start && now <= end) return true;
            }
        }
        return false;
    }

    private static int parseTimeToMinutes(String timeStr) {
        String[] parts = timeStr.trim().split(":");
        int h = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        return h * 60 + m;
    }

}
