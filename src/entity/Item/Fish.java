package entity.Item;

import main.GameClock;


public class Fish extends Item{
    public enum Rarity{
        COMMON, REGULAR, LEGENDARY
    }

    private String season;
    private String weather;
    private String location;
    private String rarity;
    private String time;

    public Fish(String itemName, String season, String weather, String location, String rarity, String time){
        super(itemName, "Fish");
        this.season = season;
        this.weather = weather;
        this.location = location;
        this.rarity = rarity;
        this.time = time;
    }

    public String getSeason() {
        return season;
    }

    public String getWeather() {
        return weather;
    }

    public String getLocation() {
        return location;
    }

    public String getRarity() {
        return rarity;
    }

    public boolean isCatchable(){
        if(time.equals("Any")){
            return true;
        }

        String[] timeRange = time.split("-");
        int startMinute = parseTimeKeMenit(timeRange[0]);
        int endMinute = parseTimeKeMenit(timeRange[1]);
        int currentMinute = GameClock.getHour() * 60 + GameClock.getMinute();

        return currentMinute >= startMinute && currentMinute <= endMinute;
    }

    private int parseTimeKeMenit(String timeStr){
        String[] jamDanMenit = timeStr.split(":");
        int hour = Integer.parseInt(jamDanMenit[0]);
        int minute = Integer.parseInt(jamDanMenit[1]);
        return hour * 60 + minute;
    }
}
