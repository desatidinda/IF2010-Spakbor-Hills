package main;

import java.util.Random;
import entity.Farm.Season;
import entity.Farm.Weather;

public class GameClock {
    private static int minute = 0;
    private static int hour = 6;
    private static int day = 1;
    private static int dayfix = 1;
    private static Season currentSeason = Season.SPRING;
    private static Weather currentWeather = Weather.SUNNY;

    private static int rainyDayCounter = 0;
    private static Random rand = new Random();

    public static void init() {
        hour = 6;
        minute = 0;
        day = dayfix = 1;
        currentSeason = Season.SPRING;
        currentWeather = generateWeather();
        rainyDayCounter = currentWeather == Weather.RAINY ? 1 : 0;
    }

    public static void updateTime(int realSeconds) {
        int gameMinutesToAdd = realSeconds * 30; 
        //TODO: nanti 30 nya ganti jd 5 skrg buat tes biar ga kelamaan aowkawoakwoawkaowk

        minute += gameMinutesToAdd;
        while (minute >= 60) {
            minute -= 60;
            hour++;
        }

        if (hour >= 24) {
            hour = 0;
            day++;
            dayfix++;
            currentWeather = generateWeather();
        }

        checkSeasonChange();
    }

    public static String getFormattedTime() {
        return String.format("%02d:%02d", hour, minute);
    }

    public static int getHour() {
        return hour;
    }

    public static int getMinute() {
        return minute;
    }

    public static int getDay() {
        return dayfix;
    }

    public static Season getCurrentSeason() {
        return currentSeason;
    }

    public static Weather getCurrentWeather() {
        return currentWeather;
    }

    private static Weather generateWeather() {
        if (rainyDayCounter < 2) {
            rainyDayCounter++;
            return Weather.RAINY;
        } else {
            return rand.nextBoolean() ? Weather.SUNNY : Weather.RAINY;
        }
    }

    private static void checkSeasonChange() {
    if (day > 10) {
        day = 1;
        nextSeason();
    }
}

    private static void nextSeason() {
        currentSeason = switch (currentSeason) {
            case SPRING -> Season.SUMMER;
            case SUMMER -> Season.FALL;
            case FALL -> Season.WINTER;
            case WINTER -> Season.SPRING;
        };
    }
    
}
