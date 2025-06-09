package game.alahakbaru;

import java.awt.*;

public class GameConstants {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int GROUND_HEIGHT = 50;
    public static final int PLANE_WIDTH = 60;
    public static final int PLANE_HEIGHT = 40;
    public static final int GRAVITY = 1;
    public static final int JUMP_STRENGTH = -15;
    public static final int BUILDING_WIDTH = 80;
    public static final int BUILDING_GAP = 250;
    public static final int BUILDING_SPEED = 5;
    public static final int BUILDING_SPACING = 300;
    public static final int SKY_COLOR_CHANGE_THRESHOLD = 20;

    // Sky colors for easter egg
    public static final Color[] SKY_COLORS = {
            new Color(135, 206, 235),  // Daytime blue
            new Color(25, 25, 112),     // Midnight blue
            new Color(148, 0, 211),     // Dark violet
            new Color(220, 20, 60),      // Crimson
            new Color(0, 100, 0)        // Dark green
    };
}