package game.alahakbaru;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Plane {
    private int x = 100;
    private int y;
    private int velocity;
    private BufferedImage planeImage;

    public Plane() {
        reset();
        loadImage();
    }

    private void loadImage() {
        try {
            planeImage = ImageIO.read(getClass().getResource("/plane.png"));
        } catch (IOException | IllegalArgumentException e) {
            planeImage = new BufferedImage(GameConstants.PLANE_WIDTH, GameConstants.PLANE_HEIGHT,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = planeImage.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, GameConstants.PLANE_WIDTH, GameConstants.PLANE_HEIGHT);
            g.setColor(Color.BLUE);
            g.fillRect(GameConstants.PLANE_WIDTH - 20, 5, 15, 15);
            g.setColor(Color.RED);
            int[] xPoints = {GameConstants.PLANE_WIDTH, GameConstants.PLANE_WIDTH + 15, GameConstants.PLANE_WIDTH};
            int[] yPoints = {10, GameConstants.PLANE_HEIGHT/2, GameConstants.PLANE_HEIGHT - 10};
            g.fillPolygon(xPoints, yPoints, 3);
            g.dispose();
        }
    }

    public void reset() {
        y = GameConstants.HEIGHT / 2 - GameConstants.PLANE_HEIGHT / 2;
        velocity = 0;
    }

    public void update() {
        velocity += GameConstants.GRAVITY;
        y += velocity;
    }

    public void jump() {
        velocity = GameConstants.JUMP_STRENGTH;
    }

    public void draw(Graphics g) {
        g.drawImage(planeImage, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.PLANE_WIDTH, GameConstants.PLANE_HEIGHT);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return GameConstants.PLANE_WIDTH; }
    public int getHeight() { return GameConstants.PLANE_HEIGHT; }
}