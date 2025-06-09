package game.alahakbaru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private final JFrame frame;
    private final MenuScreen menuScreen;
    private Timer gameTimer;
    private Plane plane;
    private ArrayList<Building> buildings;
    private Random random;
    private int score = 0;
    private boolean gameOver = false;
    private SkyManager skyManager;

    // Game constants
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int GRAVITY = 1;
    private static final int FLAP_FORCE = -15;
    private static final int BUILDING_SPEED = 5;
    private static final int BUILDING_GAP = 200;
    private static final int BUILDING_SPAWN_RATE = 100;

    public GamePanel(JFrame frame, MenuScreen menuScreen) {
        this.frame = frame;
        this.menuScreen = menuScreen;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocusInWindow();

        initGame();
        setupKeyBindings();
    }

    private void initGame() {
        plane = new Plane(100, HEIGHT / 2 - 15);
        buildings = new ArrayList<>();
        random = new Random();
        skyManager = new SkyManager();

        // Create initial buildings
        for (int i = 0; i < 5; i++) {
            spawnBuilding(WIDTH + i * 300);
        }
    }

    private void setupKeyBindings() {
        // Space to flap
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "flap");
        getActionMap().put("flap", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    plane.flap();
                }
            }
        });

        // ESC to pause
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
        getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseGame();
            }
        });
    }

    public void startGame() {
        gameOver = false;
        score = 0;
        initGame();
        resumeGame();
    }

    public void resumeGame() {
        if (gameTimer == null || !gameTimer.isRunning()) {
            gameTimer = new Timer(16, this); // ~60 FPS
            gameTimer.start();
        }
    }

    public void pauseGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        menuScreen.setGamePanel(this);
        menuScreen.switchToPanel(menuScreen);
    }

    private void spawnBuilding(int x) {
        int gapPosition = random.nextInt(HEIGHT - BUILDING_GAP - 100) + 50;
        buildings.add(new Building(x, 0, 80, gapPosition));
        buildings.add(new Building(x, gapPosition + BUILDING_GAP, 80, HEIGHT - gapPosition - BUILDING_GAP));
    }

    private void update() {
        if (gameOver) return;

        // Update plane
        plane.applyGravity(GRAVITY);
        plane.update();

        // Update buildings
        for (int i = 0; i < buildings.size(); i++) {
            Building building = buildings.get(i);
            building.move(-BUILDING_SPEED);

            // Remove off-screen buildings
            if (building.getX() + building.getWidth() < 0) {
                buildings.remove(i);
                i--;
            }
        }

        // Spawn new buildings
        if (buildings.get(buildings.size() - 1).getX() < WIDTH - BUILDING_SPAWN_RATE) {
            spawnBuilding(WIDTH);
        }

        // Check collisions
        checkCollisions();
    }

    private void checkCollisions() {
        Rectangle planeRect = plane.getBounds();

        // Ground and ceiling collision
        if (plane.getY() <= 0 || plane.getY() + plane.getHeight() >= HEIGHT) {
            endGame();
            return;
        }

        // Building collision
        for (Building building : buildings) {
            if (planeRect.intersects(building.getBounds())) {
                endGame();
                return;
            }
        }

        // Score increment (when passing a building pair)
        if (buildings.size() > 0 && buildings.get(0).getX() + buildings.get(0).getWidth() < plane.getX() && !buildings.get(0).isPassed()) {
            score++;
            buildings.get(0).markPassed();
            buildings.get(1).markPassed();

            // Update sky every 20 points
            if (score % 20 == 0) {
                skyManager.nextSkyColor();
            }
        }
    }

    private void endGame() {
        gameOver = true;
        gameTimer.stop();
        HighScoreManager.saveScore(score);
        JOptionPane.showMessageDialog(frame, "Game Over! Score: " + score);
        menuScreen.switchToPanel(menuScreen);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw sky
        g2d.setColor(skyManager.getCurrentColor());
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw buildings
        for (Building building : buildings) {
            building.draw(g2d);
        }

        // Draw plane
        plane.draw(g2d);

        // Draw score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString("Score: " + score, 20, 40);

        if (gameOver) {
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 72));
            String gameOverText = "GAME OVER";
            int textWidth = g2d.getFontMetrics().stringWidth(gameOverText);
            g2d.drawString(gameOverText, (WIDTH - textWidth) / 2, HEIGHT / 2);
        }
    }

}