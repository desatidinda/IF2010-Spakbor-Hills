package entity.Farm;

public enum Weather {
    SUNNY,
    RAINY;

    public static Weather weather;

    public static void setWeather(Weather weather) {
        Weather.weather = weather;
    }
    
}