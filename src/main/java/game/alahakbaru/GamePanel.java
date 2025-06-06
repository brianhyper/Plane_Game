package game.alahakbaru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.io.IOException;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private GameState gameState = GameState.START;
    private MenuState menuState = MenuState.PLAY;
    private Plane plane;
    private java.util.List<Building> buildings;
    private int score;
    private HighScoreManager highScoreManager;
    private Timer gameTimer;
    private Random random;
    private BufferedImage backgroundImage;
    private Color skyColor;
    private int skyColorIndex = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(GameConstants.WIDTH, GameConstants.HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        random = new Random();
        plane = new Plane();
        buildings = new ArrayList<>();
        highScoreManager = new HighScoreManager();

        gameTimer = new Timer(20, this);
        loadBackgroundImage();

        skyColor = GameConstants.SKY_COLORS[skyColorIndex];
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/city-skyline.png"));
        } catch (IOException | IllegalArgumentException e) {
            // Create a fallback background
            backgroundImage = new BufferedImage(GameConstants.WIDTH, GameConstants.HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics g = backgroundImage.getGraphics();
            g.setColor(new Color(100, 149, 237));
            g.fillRect(0, 0, GameConstants.WIDTH, GameConstants.HEIGHT);

            // Draw distant buildings
            g.setColor(new Color(70, 70, 70));
            for (int i = 0; i < 15; i++) {
                int height = random.nextInt(150) + 50;
                int width = random.nextInt(40) + 30;
                g.fillRect(i * 70, GameConstants.HEIGHT - GameConstants.GROUND_HEIGHT - height, width, height);
            }
            g.dispose();
        }
    }

    public void startGame() {
        gameState = GameState.PLAYING;
        plane.reset();
        buildings.clear();
        score = 0;
        skyColorIndex = 0;
        skyColor = GameConstants.SKY_COLORS[skyColorIndex];

        // Create initial buildings
        for (int i = 0; i < 3; i++) {
            addBuilding(GameConstants.WIDTH + i * GameConstants.BUILDING_SPACING);
        }

        gameTimer.start();
        requestFocusInWindow();
    }

    public void resumeGame() {
        if (gameState == GameState.PAUSED) {
            gameState = GameState.PLAYING;
            gameTimer.start();
            requestFocusInWindow();
        }
    }

    public void pauseGame() {
        if (gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED;
            gameTimer.stop();
        }
    }

    public void togglePause() {
        if (gameState == GameState.PLAYING) {
            pauseGame();
        } else if (gameState == GameState.PAUSED) {
            resumeGame();
        }
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

    private void updateGame() {
        if (gameState != GameState.PLAYING) return;

        // Update plane
        plane.update();

        // Move buildings
        Iterator<Building> it = buildings.iterator();
        while (it.hasNext()) {
            Building building = it.next();
            building.move();

            // Remove off-screen buildings
            if (building.getX() + building.getWidth() < 0) {
                it.remove();
            }

            // Check for passing buildings (score)
            if (building.getX() + building.getWidth() == plane.getX() && !building.isPassed()) {
                building.setPassed(true);
                score++;

                // Easter egg: Change sky color every 20 points
                if (score % GameConstants.SKY_COLOR_CHANGE_THRESHOLD == 0) {
                    skyColorIndex = (skyColorIndex + 1) % GameConstants.SKY_COLORS.length;
                    skyColor = GameConstants.SKY_COLORS[skyColorIndex];
                }
            }
        }

        // Add new buildings
        if (buildings.isEmpty() || buildings.get(buildings.size() - 1).getX() <
                GameConstants.WIDTH - GameConstants.BUILDING_SPACING) {
            addBuilding(GameConstants.WIDTH);
        }

        // Check collisions
        checkCollisions();
    }

    private void checkCollisions() {
        Rectangle planeRect = plane.getBounds();

        // Ground collision
        if (plane.getY() + plane.getHeight() > GameConstants.HEIGHT - GameConstants.GROUND_HEIGHT) {
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
        highScoreManager.addScore(score);
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
            case MENU -> drawMenuScreen(g);
            case PLAYING -> drawPlayingUI(g);
            case PAUSED -> drawPausedScreen(g);
            case GAME_OVER -> drawGameOverScreen(g);
            case HIGH_SCORES -> drawHighScoresScreen(g);
        }
    }

    private void drawBackground(Graphics g) {
        // Draw sky with current color
        g.setColor(skyColor);
        g.fillRect(0, 0, GameConstants.WIDTH, GameConstants.HEIGHT - GameConstants.GROUND_HEIGHT);

        // Draw city skyline
        g.drawImage(backgroundImage, 0, GameConstants.HEIGHT - GameConstants.GROUND_HEIGHT - 150,
                GameConstants.WIDTH, 150, null);
    }

    private void drawGround(Graphics g) {
        // Ground
        g.setColor(new Color(34, 139, 34));
        g.fillRect(0, GameConstants.HEIGHT - GameConstants.GROUND_HEIGHT,
                GameConstants.WIDTH, GameConstants.GROUND_HEIGHT);

        // Ground details
        g.setColor(new Color(139, 69, 19));
        for (int i = 0; i < GameConstants.WIDTH; i += 20) {
            g.drawLine(i, GameConstants.HEIGHT - GameConstants.GROUND_HEIGHT,
                    i, GameConstants.HEIGHT);
        }
    }

    private void drawStartScreen(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameConstants.WIDTH, GameConstants.HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("ALAH AKBARU", GameConstants.WIDTH/2 - 150, GameConstants.HEIGHT/2 - 80);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press SPACE to Continue", GameConstants.WIDTH/2 - 140, GameConstants.HEIGHT/2 + 20);

        g.setFont(new Font("Arial", Font.ITALIC, 18));
        g.drawString("Navigate with UP/DOWN arrows, select with ENTER",
                GameConstants.WIDTH/2 - 220, GameConstants.HEIGHT - 50);
    }

    private void drawMenuScreen(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameConstants.WIDTH, GameConstants.HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("MAIN MENU", GameConstants.WIDTH/2 - 100, 80);

        // Draw menu options
        int yPos = 150;
        for (MenuState option : MenuState.values()) {
            boolean isSelected = option == menuState;

            if (option == MenuState.RESUME && gameState != GameState.PAUSED) {
                continue; // Skip resume if no game is paused
            }

            if (isSelected) {
                g.setColor(Color.YELLOW);
                g.fillRect(GameConstants.WIDTH/2 - 150, yPos - 25, 300, 40);
            }

            g.setColor(isSelected ? Color.BLACK : Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString(option.getLabel(), GameConstants.WIDTH/2 - 70, yPos);

            yPos += 50;
        }

        // Draw high score
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("High Score: " + highScoreManager.getHighestScore(),
                GameConstants.WIDTH/2 - 70, GameConstants.HEIGHT - 50);
    }

    private void drawPlayingUI(Graphics g) {
        // Score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 30);

        // High score
        g.drawString("High Score: " + highScoreManager.getHighestScore(),
                GameConstants.WIDTH - 200, 30);

        // Pause button
        g.setColor(new Color(255, 255, 255, 150));
        g.fillRect(GameConstants.WIDTH - 100, 10, 90, 30);
        g.setColor(Color.BLACK);
        g.drawString("Pause (P)", GameConstants.WIDTH - 90, 30);
    }

    private void drawPausedScreen(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameConstants.WIDTH, GameConstants.HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("PAUSED", GameConstants.WIDTH/2 - 80, GameConstants.HEIGHT/2 - 50);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press P to Resume", GameConstants.WIDTH/2 - 100, GameConstants.HEIGHT/2 + 20);
        g.drawString("Press M for Menu", GameConstants.WIDTH/2 - 100, GameConstants.HEIGHT/2 + 60);
    }

    private void drawGameOverScreen(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameConstants.WIDTH, GameConstants.HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Game Over!", GameConstants.WIDTH/2 - 120, GameConstants.HEIGHT/2 - 60);

        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Score: " + score, GameConstants.WIDTH/2 - 80, GameConstants.HEIGHT/2);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("High Score: " + highScoreManager.getHighestScore(),
                GameConstants.WIDTH/2 - 70, GameConstants.HEIGHT/2 + 40);
        g.drawString("Press SPACE to Restart", GameConstants.WIDTH/2 - 120, GameConstants.HEIGHT/2 + 100);
        g.drawString("Press M for Menu", GameConstants.WIDTH/2 - 90, GameConstants.HEIGHT/2 + 140);
    }

    private void drawHighScoresScreen(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameConstants.WIDTH, GameConstants.HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("HIGH SCORES", GameConstants.WIDTH/2 - 100, 60);

        List<ScoreRecord> scores = highScoreManager.getHighScores();
        int yPos = 120;

        if (scores.isEmpty()) {
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("No scores recorded yet!", GameConstants.WIDTH/2 - 120, yPos);
        } else {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Rank  Score  Date", GameConstants.WIDTH/2 - 150, yPos);
            yPos += 30;

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            for (int i = 0; i < Math.min(10, scores.size()); i++) {
                ScoreRecord record = scores.get(i);
                String rank = (i + 1) + ".";
                String scoreStr = String.format("%-5d", record.getScore());
                g.drawString(rank + "   " + scoreStr + "   " + record.getFormattedDateTime(),
                        GameConstants.WIDTH/2 - 150, yPos);
                yPos += 30;
            }
        }

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press M to return to Menu",
                GameConstants.WIDTH/2 - 130, GameConstants.HEIGHT - 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (gameState) {
            case START:
                if (keyCode == KeyEvent.VK_SPACE) {
                    gameState = GameState.MENU;
                }
                break;

            case MENU:
                handleMenuNavigation(keyCode);
                break;

            case PLAYING:
                if (keyCode == KeyEvent.VK_SPACE) {
                    plane.jump();
                } else if (keyCode == KeyEvent.VK_P) {
                    pauseGame();
                } else if (keyCode == KeyEvent.VK_M) {
                    gameState = GameState.MENU;
                }
                break;

            case PAUSED:
                if (keyCode == KeyEvent.VK_P) {
                    resumeGame();
                } else if (keyCode == KeyEvent.VK_M) {
                    gameState = GameState.MENU;
                }
                break;

            case GAME_OVER:
                if (keyCode == KeyEvent.VK_SPACE) {
                    startGame();
                } else if (keyCode == KeyEvent.VK_M) {
                    gameState = GameState.MENU;
                }
                break;

            case HIGH_SCORES:
                if (keyCode == KeyEvent.VK_M) {
                    gameState = GameState.MENU;
                }
                break;
        }
    }

    private void handleMenuNavigation(int keyCode) {
        if (keyCode == KeyEvent.VK_UP) {
            menuState = menuState.previous();
            // Skip resume if no game is paused
            if (menuState == MenuState.RESUME && gameState != GameState.PAUSED) {
                menuState = menuState.previous();
            }
        } else if (keyCode == KeyEvent.VK_DOWN) {
            menuState = menuState.next();
            // Skip resume if no game is paused
            if (menuState == MenuState.RESUME && gameState != GameState.PAUSED) {
                menuState = menuState.next();
            }
        } else if (keyCode == KeyEvent.VK_ENTER) {
            switch (menuState) {
                case PLAY:
                    startGame();
                    break;
                case RESUME:
                    resumeGame();
                    break;
                case HIGH_SCORES:
                    gameState = GameState.HIGH_SCORES;
                    break;
                case EXIT:
                    System.exit(0);
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}