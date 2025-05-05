package entity.Item;

public class Fish extends Item{
    private String season;
    private String weather;
    private String location;
    private String rarity;

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

    public void setSeason(String season) {
        this.season = season;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

}
