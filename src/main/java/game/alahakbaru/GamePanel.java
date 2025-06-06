package game.alahakbaru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    // Remove constant declarations and use GameConstants instead
    private GameState gameState = GameState.START;
    private Plane plane;
    private List<Building> buildings;
    private int score;
    private int highScore;
    private Timer gameTimer;
    private Random random;
    private HighScoreManager highScoreManager;

    public GamePanel() {
        setPreferredSize(new Dimension(GameConstants.WIDTH, GameConstants.HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        random = new Random();
        plane = new Plane();
        buildings = new ArrayList<>();
        highScoreManager = new HighScoreManager();
        highScore = highScoreManager.loadHighScore();

        gameTimer = new Timer(20, this);
    }

    public void startGame() {
        gameState = GameState.PLAYING;
        plane.reset();
        buildings.clear();
        score = 0;

        // Create initial buildings
        for (int i = 0; i < 3; i++) {
            addBuilding(WIDTH + i * BUILDING_SPACING);
        }

        gameTimer.start();
        requestFocusInWindow();
    }

    private void addBuilding(int x) {
        int gapPosition = random.nextInt(GameConstants.HEIGHT - GameConstants.GROUND_HEIGHT -
                GameConstants.BUILDING_GAP - 100) + 50;
        buildings.add(new Building(x, 0, GameConstants.BUILDING_WIDTH, gapPosition));
        buildings.add(new Building(x, gapPosition + GameConstants.BUILDING_GAP,
                GameConstants.BUILDING_WIDTH,
                GameConstants.HEIGHT - gapPosition -
                        GameConstants.BUILDING_GAP -
                        GameConstants.GROUND_HEIGHT));
    }

    private void checkCollisions() {
        // Update collision checks:
        if (plane.getY() + plane.getSize() > GameConstants.HEIGHT - GameConstants.GROUND_HEIGHT) {
            gameOver();
            return;
        }

        // Ceiling collision
        if (plane.getY() < 0) {
            gameOver();
            return;
        }

        // Building collisions
        for (Building building : buildings) {
            if (planeRect.intersects(building.getBounds())) {
                gameOver();
                return;
            }
        }
    }

    private void gameOver() {
        gameState = GameState.GAME_OVER;
        gameTimer.stop();
        highScoreManager.saveHighScore(highScore);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        drawBackground(g);

        // Draw buildings
        for (Building building : buildings) {
            building.draw(g);
        }

        // Draw plane
        plane.draw(g);

        // Draw ground
        drawGround(g);

        // Draw UI based on game state
        switch (gameState) {
            case START -> drawStartScreen(g);
            case PLAYING -> drawPlayingUI(g);
            case GAME_OVER -> drawGameOverScreen(g);
        }
    }

    private void drawBackground(Graphics g) {
        // Sky gradient
        GradientPaint skyGradient = new GradientPaint(0, 0, new Color(135, 206, 235),
                0, HEIGHT/2, new Color(100, 149, 237));
        ((Graphics2D) g).setPaint(skyGradient);
        g.fillRect(0, 0, WIDTH, HEIGHT - GROUND_HEIGHT);

        // Sun
        g.setColor(Color.YELLOW);
        g.fillOval(WIDTH - 100, 50, 60, 60);

        // Distant buildings
        g.setColor(new Color(100, 100, 100));
        for (int i = 0; i < 10; i++) {
            int height = random.nextInt(100) + 50;
            g.fillRect(i * 80, HEIGHT - GROUND_HEIGHT - height, 60, height);
        }
    }

    private void drawGround(Graphics g) {
        // Ground
        g.setColor(new Color(34, 139, 34));
        g.fillRect(0, HEIGHT - GROUND_HEIGHT, WIDTH, GROUND_HEIGHT);

        // Ground details
        g.setColor(new Color(139, 69, 19));
        for (int i = 0; i < WIDTH; i += 20) {
            g.drawLine(i, HEIGHT - GROUND_HEIGHT, i, HEIGHT);
        }
    }

    private void drawStartScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("ALAH AKBARU", WIDTH/2 - 150, HEIGHT/2 - 60);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press SPACE to Start", WIDTH/2 - 120, HEIGHT/2 + 20);
        g.drawString("Use SPACE to fly", WIDTH/2 - 100, HEIGHT/2 + 60);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("High Score: " + highScore, WIDTH/2 - 70, HEIGHT/2 + 120);
    }

    private void drawPlayingUI(Graphics g) {
        // Score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 30);

        // High score
        g.drawString("High Score: " + highScore, WIDTH - 200, 30);
    }

    private void drawGameOverScreen(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Game Over!", WIDTH/2 - 120, HEIGHT/2 - 60);

        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Score: " + score, WIDTH/2 - 80, HEIGHT/2);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("High Score: " + highScore, WIDTH/2 - 70, HEIGHT/2 + 40);
        g.drawString("Press SPACE to Restart", WIDTH/2 - 120, HEIGHT/2 + 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            switch (gameState) {
                case START -> startGame();
                case PLAYING -> plane.jump();
                case GAME_OVER -> startGame();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}