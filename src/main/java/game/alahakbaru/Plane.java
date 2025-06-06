package game.alahakbaru;

import java.awt.*;

public class Plane {
    private int x = 100;
    private int y;
    private int velocity;

    public void update() {
        velocity += GameConstants.GRAVITY;
        y += velocity;
    }

    public void jump() {
        velocity = GameConstants.JUMP_STRENGTH;
    }
    public void reset() {
        y = GamePanel.HEIGHT / 2 - SIZE/2;
        velocity = 0;
    }

    public void draw(Graphics g) {
        // Plane body
        g.setColor(Color.WHITE);
        g.fillRect(x, y, SIZE, SIZE);

        // Plane details
        g.setColor(Color.BLUE);
        g.fillRect(x + SIZE - 15, y + 5, 10, 10); // Cockpit

        g.fillRect(x, y + SIZE/2 - 5, 20, 10); // Wings

        // Tail
        g.setColor(Color.RED);
        int[] xPoints = {x + SIZE, x + SIZE + 15, x + SIZE};
        int[] yPoints = {y + 10, y + SIZE/2, y + SIZE - 10};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return SIZE; }
}