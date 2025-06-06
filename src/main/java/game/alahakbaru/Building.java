package game.alahakbaru;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Building {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean passed;
    private BufferedImage buildingImage;

    public Building(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.passed = false;
        loadImage();
    }

    private void loadImage() {
        try {
            buildingImage = ImageIO.read(getClass().getResource("/building.png"));
        } catch (IOException | IllegalArgumentException e) {
            buildingImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = buildingImage.getGraphics();
            g.setColor(new Color(70, 70, 70));
            g.fillRect(0, 0, width, height);
            g.setColor(new Color(200, 230, 255, 200));

            int windowSize = 10;
            int windowSpacing = 20;
            int startX = 10;
            int startY = 10;

            for (int row = startY; row < height - windowSize; row += windowSpacing) {
                for (int col = startX; col < width - windowSize; col += windowSpacing) {
                    if (Math.random() > 0.3) {
                        g.fillRect(col, row, windowSize, windowSize);
                    }
                }
            }
            g.dispose();
        }
    }

    public void move() {
        x -= GameConstants.BUILDING_SPEED;
    }

    public void draw(Graphics g) {
        g.drawImage(buildingImage, x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getWidth() { return width; }
    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
}