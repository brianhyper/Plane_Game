package game.alahakbaru;

import java.awt.Color;

public class SkyManager {
    private final Color[] skyColors = {
            new Color(135, 206, 235),    // Sky Blue
            new Color(106, 90, 205),     // Slate Blue
            new Color(255, 165, 0),      // Orange
            new Color(220, 20, 60),      // Crimson
            new Color(75, 0, 130)        // Indigo
    };
    private int currentIndex = 0;

    public Color getCurrentColor() {
        return skyColors[currentIndex];
    }

    public void nextSkyColor() {
        currentIndex = (currentIndex + 1) % skyColors.length;
    }
}