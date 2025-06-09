package game.alahakbaru;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Plane {
    // Plane constants
    private static final int FLAP_FORCE = -15;  // Negative because y increases downward
    private static final int PLANE_WIDTH = 50;
    private static final int PLANE_HEIGHT = 30;

    private int x, y;
    private int velocity;
    private BufferedImage image;

    public Plane(int x, int y) {
        this.x = x;
        this.y = y;
        this.velocity = 0;
    }

    public void update() {
        y += velocity;
    }

    public void flap() {
        velocity = FLAP_FORCE;
    }

    public void applyGravity(int gravity) {
        velocity += gravity;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, PLANE_WIDTH, PLANE_HEIGHT);
    }

    public void draw(Graphics2D g2d) {
        if (image != null) {
            g2d.drawImage(image, x, y, PLANE_WIDTH, PLANE_HEIGHT, null);
        } else {
            // Draw placeholder plane
            g2d.setColor(Color.RED);
            g2d.fillRect(x, y, PLANE_WIDTH, PLANE_HEIGHT);

            // Draw wings
            g2d.setColor(Color.BLUE);
            int[] xPoints = {x + PLANE_WIDTH, x + PLANE_WIDTH + 20, x + PLANE_WIDTH};
            int[] yPoints = {y, y + PLANE_HEIGHT/2, y + PLANE_HEIGHT};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return PLANE_WIDTH; }
    public int getHeight() { return PLANE_HEIGHT; }

    // Setter for image
    public void setImage(BufferedImage image) {
        this.image = image;
    }
}