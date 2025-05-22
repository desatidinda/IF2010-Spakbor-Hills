package entity.Item;


public class Fish extends Item{
    private String season;
    private String weather;
    private String location;
    private String rarity;

    public Fish(String fishName, String season, String weather, String location, String rarity){
        super(fishName, "Fish");
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
