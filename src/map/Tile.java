package map;

// import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;
    public boolean collision = false;

    private TileType type;
    private boolean watered = false;

    public Tile(TileType type) {
        this.type = type;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public boolean isWatered() {
        return watered;
    }

    public void setWatered(boolean watered) {
        this.watered = watered;
    }

    // public void draw(Graphics2D g2, int x, int y, int size) {
    //     Color color = switch (type) {
    //         case TILLABLE -> new Color(194, 178, 128);
    //         case TILLED -> new Color(150, 111, 51);
    //         case PLANTED -> Color.GREEN;
    //         case HOUSE -> Color.GRAY;
    //         case POND -> Color.CYAN;
    //         case SHIPPING_BIN -> Color.ORANGE;
    //     };

    //     g2.setColor(color);
    //     g2.fillRect(x, y, size, size);
    //     g2.setColor(Color.BLACK);
    //     g2.drawRect(x, y, size, size);
    // }
}
