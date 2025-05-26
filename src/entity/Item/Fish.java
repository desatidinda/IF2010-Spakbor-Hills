package entity.Item;

import java.util.*;
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
    private List<int[]> timeRanges;
    private double sellPrice;

    public Fish(String itemName, String season, String weather, String location, String rarity, String time){
        super(itemName, "Fish");
        this.season = season;
        this.weather = weather;
        this.location = location;
        this.rarity = rarity;
        this.time = time;
        this.timeRanges = parseTimeRanges(time);
        this.sellPrice = calculateSellPrice();
    }

    private List<int[]> parseTimeRanges(String timeStr){
        List<int[]> ranges = new ArrayList<>();
        if(timeStr.equals("Any")){
            return ranges;
        }

        String[] parts = timeStr.split(",");
        for (String part : parts){
            String[] times = part.trim().split("-");
            int start = parseHour(times[0]);
            int end = parseHour(times[1]);
            ranges.add(new int[]{start, end});
        }
        return ranges;
    }

    private int parseHour(String timeStr){
        return Integer.parseInt(timeStr.split("\\.")[0]);
    }

    private double calculateSellPrice(){
        int seasonCount;
        if (season.equals("Any")){
            seasonCount = 4;
        } else{
            seasonCount = season.split(",").length;
        }
        int totalHours = 0;
        for (int[] range : timeRanges){
            int start = range[0];
            int end = range[1];
            if (start <= end){
                totalHours = totalHours + (end - start);
            } else{
                totalHours = totalHours + (24 - start + end);
            }
            
        }
        if (totalHours == 0){
            totalHours = 24;
        }

        int weatherCount = 0;
        if(weather.equals("Any")){
            weatherCount = 4;
        } else{
            weatherCount = weather.split(",").length;
        }
        int locationCount = location.split(",").length;

        int c;
        switch(rarity){
            case "COMMON":
                c = 10;
                break;
            case "REGULAR":
                c = 5;
                break;
            case "LEGENDARY":
                c = 25;
                break;
            default:
                break;
            
        }

        return (4.0 / seasonCount) * (24.0 / totalHours) * (2.0 / weatherCount) * (4.0 / locationCount) * c;
    }

    public boolean isCatchable(){
        int currentHour = GameClock.getHour();
        if (time.equals("Any")){
            return true;
        }

        for (int[] range : timeRanges){
            int start = range[0];
            int end = range[1];
            if (start <= end){
                if (currentHour >= start && currentHour < end){
                    return true;
                } else if (currentHour >= start || currentHour < end){
                    return true;
                }
            }
        }
        return false;
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

