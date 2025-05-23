package entity.Item;


public class Fish extends Item{
    public enum Rarity{
        COMMON, REGULAR, LEGENDARY
    }

    private String season;
    private String weather;
    private String location;
    private String rarity;

    public Fish(String itemName, String season, String weather, String location, String rarity){
        super(itemName, "Fish");
        this.season = season;
        this.weather = weather;
        this.location = location;
        this.rarity = rarity;
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
}