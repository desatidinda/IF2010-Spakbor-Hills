package entity.Farm;

public enum Season {
    SPRING,
    SUMMER,
    FALL,
    WINTER;

    public static Season season;

    public static void setSeason(Season season) {
        Season.season = season;
    }
    
}
