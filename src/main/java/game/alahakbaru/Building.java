package game.alahakbaru;

import java.awt.*;

public class Building {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean passed;

    public Building(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.passed = false;
    }

    public void move() {
        x -= GameConstants.BUILDING_SPEED;
    }

    public void draw(Graphics g) {
        // Building color based on height
        Color buildingColor = height > 300 ? new Color(70, 70, 70) :
                height > 150 ? new Color(90, 90, 90) :
                        new Color(110, 110, 110);

        g.setColor(buildingColor);
        g.fillRect(x, y, width, height);

        // Building outline
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        // Draw windows
        drawWindows(g);
    }

    private void drawWindows(Graphics g) {
        g.setColor(new Color(200, 230, 255, 200));

        int windowSize = 10;
        int windowSpacing = 20;
        int startX = x + 10;
        int startY = y + 10;

        for (int row = startY; row < y + height - windowSize; row += windowSpacing) {
            for (int col = startX; col < x + width - windowSize; col += windowSpacing) {
                if (Math.random() > 0.3) { // Randomize some windows
                    g.fillRect(col, row, windowSize, windowSize);
                }
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getWidth() { return width; }
    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
}