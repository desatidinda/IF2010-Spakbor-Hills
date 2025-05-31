package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import entity.Farm.Season;
import entity.Farm.Weather;
import state.EndGameStatistics;

public class GameClock {
    private static Thread clockThread;
    private static boolean running = false;
    private static boolean paused = false;
    private static final Object lock = new Object();

    private static long gameStartTime = 0;
    private static long totalGameTime = 0;

    private static int minute = 0;
    private static int hour = 6;
    private static int day = 1;
    private static int dayfix = 1;
    private static Season currentSeason = Season.SPRING;
    private static Weather currentWeather = Weather.SUNNY;

    private static int rainyDayCounter = 0;
    private static Random rand = new Random();

    private static List<GameObserver> observers = new ArrayList<>();

    public static void startClock() {
        if (clockThread == null || !clockThread.isAlive()) {
            running = true;
            clockThread = new Thread(() -> {
                while (running) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                    synchronized (lock) {
                        if (!paused) updateTime(1);
                    }
                }
            });
            clockThread.setDaemon(true);
            clockThread.start();
        }
    }

    public static void init() {
        synchronized (lock) {
            hour = 6;
            minute = 0;
            day = dayfix = 1;
            currentSeason = Season.SPRING;
            currentWeather = generateWeather();
            rainyDayCounter = currentWeather == Weather.RAINY ? 1 : 0;

            gameStartTime = System.currentTimeMillis();
            totalGameTime = 0;
        }
    }

    public static void updateTime(int realSeconds) {
        int gameMinutesToAdd = realSeconds * 5;

        totalGameTime += realSeconds * 5000;
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

    public static long getGameTime() {
        synchronized (lock) {
            return totalGameTime;
        }
}

    public static void stopClock() {
        running = false;
        if (clockThread != null) clockThread.interrupt();
    }

    public static void setPaused(boolean value) {
        paused = value;
    }

    public static boolean isPaused() {
        return paused;
    }

    public static String getFormattedTime() {
        return String.format("%02d:%02d", hour, minute);
    }

    public static int getHour() {
        synchronized (lock) {
            return hour;
        }
    }

    public static int getMinute() {
        synchronized (lock) {
            return minute;
        }
    }

    public static int getDay() {
        synchronized (lock) {
            return dayfix;
        }
    }

    public static Season getCurrentSeason() {
        synchronized (lock) {
            return currentSeason;
        }
    }

    public static Weather getCurrentWeather() {
        synchronized (lock) {
            return currentWeather;
        }
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
        notifySeasonChanged();
    }
    
    public static void skipToMorning() {
        synchronized (lock) {
            hour = 6;
            minute = 0;
            day++;
            dayfix++;
            
            currentWeather = generateWeather();
            checkSeasonChange();
        }
    }

    public static void skipUntilMorning() {
        synchronized(lock) {
            hour = 6;
            minute = 0;
        }
    }

    public static void skipMinutes(int minutes) {
        synchronized (lock) {
            if (paused) return; 
            minute += minutes;
            while (minute >= 60) {
                minute -= 60;
                hour++;
            }
            while (hour >= 24) {
                hour -= 24;
                day++;
                dayfix++;
                currentWeather = generateWeather();
                checkSeasonChange();
            }
        }
    }

    public static void addMinutes(int minutesToAdd) {
        synchronized (lock) {
            if (paused) return;
            minute += minutesToAdd;
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
            
            if (day > 10) {
                day = 1;
                nextSeason();
            }
        }
    }

    public static void skipToHour(int targetHour) {
        synchronized (lock) {
            if (targetHour < 0 || targetHour >= 24) {
                targetHour = Math.max(0, Math.min(23, targetHour));
            }

            hour = targetHour;
            minute = 0;

            if (targetHour < getHour()) {
                day++;
                dayfix++;
                currentWeather = generateWeather();
                checkSeasonChange();
            }
        }
    }

    public static boolean isPingsan() {
        synchronized (lock) {
            return hour >= 2 && hour < 6; // pingsan
        }
    }

    public enum TimePhase {
        DAY,    // jaaaaam 06:00 - 17:59
        NIGHT   // jaaaaam 18:00 - 05:59
    }

    public static TimePhase getCurrentTimePhase() {
        synchronized (lock) {
            if (hour >= 6 && hour <= 17) {
                return TimePhase.DAY;
            } else {
                return TimePhase.NIGHT;
            }
        }
    }

    public static boolean isDaytime() {
        return getCurrentTimePhase() == TimePhase.DAY;
    }

    public static boolean isNighttime() {
        return getCurrentTimePhase() == TimePhase.NIGHT;
    }

    public static void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    private static void notifySeasonChanged() {
        for (GameObserver observer : observers) {
            observer.onSeasonChanged();
        }
    }
}
