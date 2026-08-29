import java.awt.Color;
import java.awt.Font;

public class ParkXTheme {

    public static final Color BACKGROUND = new Color(8, 20, 38);
    public static final Color HEADER = new Color(6, 16, 31);
    public static final Color CARD = new Color(15, 32, 55);
    public static final Color CARD_LIGHT = new Color(20, 41, 68);
    public static final Color INPUT = new Color(19, 39, 65);
    public static final Color BORDER = new Color(40, 63, 91);

    public static final Color TEXT = new Color(241, 245, 249);
    public static final Color MUTED = new Color(148, 163, 184);

    public static final Color BLUE = new Color(59, 130, 246);
    public static final Color RED = new Color(239, 68, 68);
    public static final Color GREEN = new Color(34, 197, 94);
    public static final Color PURPLE = new Color(168, 85, 247);
    public static final Color AMBER = new Color(245, 180, 55);

    public static final Color BLUE_DARK = new Color(20, 48, 88);
    public static final Color RED_DARK = new Color(75, 30, 45);
    public static final Color GREEN_DARK = new Color(20, 65, 55);
    public static final Color PURPLE_DARK = new Color(60, 35, 90);

    public static Font titleFont(int size) {
        return new Font("SansSerif", Font.BOLD, size);
    }

    public static Font normalFont(int size) {
        return new Font("SansSerif", Font.PLAIN, size);
    }

    public static Font boldFont(int size) {
        return new Font("SansSerif", Font.BOLD, size);
    }

    private ParkXTheme() {
    }
}
