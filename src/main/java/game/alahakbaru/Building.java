package game.alahakbaru;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Building {
    private int x, y, width, height;
    private boolean passed = false;
    private BufferedImage image; // For custom graphics

    public Building(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // Load image from assets/building.png
    }

    public void move(int dx) {
        x += dx;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2d) {
        // Draw placeholder rectangle
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(x, y, width, height);

        // Draw windows
        g2d.setColor(Color.YELLOW);
        for (int i = 0; i < height; i += 20) {
            for (int j = 0; j < width; j += 15) {
                if (i > 10 && j > 10) {
                    g2d.fillRect(x + j, y + i, 8, 8);
                }
            }
        }

        // If image is loaded: g2d.drawImage(image, x, y, width, height, null);
    }

    public void markPassed() {
        passed = true;
    }

    // Getters
    public int getX() { return x; }
    public int getWidth() { return width; }
    public boolean isPassed() { return passed; }
}