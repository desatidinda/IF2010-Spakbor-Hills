package entity.Farm;

public class Farm {
    private String name;
    private Player player;
    private FarmMap farmMap;
    private Time time;
    private int day = 0;
    private Season season;
    private Weather weather;

    public Farm(String name, Player player, FarmMap farmMap, Time time, Season season, Weather weather) {
        this.name = name;
        this.player = player;
        this.farmMap = farmMap;
        this.time = time;
        this.season = season;
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public FarmMap getFarmMap() {
        return farmMap;
    }

    public String getTime() {
        return time.toString();
    }

    public int getDay() {
        return day;
    }

    public Season getSeason() {
        return season;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setFarmMap(FarmMap farmMap) {
        this.farmMap = farmMap;
    }

    public void setTime(String timeStr) {
        this.time = new Time(timeStr);
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
