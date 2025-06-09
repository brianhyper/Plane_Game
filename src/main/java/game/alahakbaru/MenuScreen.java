package game.alahakbaru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuScreen extends JPanel {
    private final JFrame frame;
    private GamePanel gamePanel;

    public MenuScreen(JFrame frame) {
        this.frame = frame;
        setLayout(new GridLayout(5, 1, 0, 20));
        setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));
        setBackground(new Color(30, 30, 70));
        initButtons();
    }

    private void initButtons() {
        JButton newGameBtn = createButton("New Game");
        JButton resumeBtn = createButton("Resume Game");
        JButton scoresBtn = createButton("High Scores");
        JButton aboutBtn = createButton("About Game");
        JButton exitBtn = createButton("Exit");

        newGameBtn.addActionListener(e -> startNewGame());
        resumeBtn.addActionListener(e -> resumeGame());
        scoresBtn.addActionListener(e -> showHighScores());
        aboutBtn.addActionListener(e -> showAboutScreen());
        exitBtn.addActionListener(e -> System.exit(0));

        // Disable resume button if no game exists
        resumeBtn.setEnabled(gamePanel != null);

        add(newGameBtn);
        add(resumeBtn);
        add(scoresBtn);
        add(aboutBtn);
        add(exitBtn);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        return button;
    }

    private void startNewGame() {
        gamePanel = new GamePanel(frame, this);
        switchToPanel(gamePanel);
        gamePanel.startGame();
    }

    private void resumeGame() {
        if (gamePanel != null) {
            switchToPanel(gamePanel);
            gamePanel.resumeGame();
        }
    }

    private void showHighScores() {
        // Implement high scores display
        JOptionPane.showMessageDialog(frame, "High Scores Feature Coming Soon!");
    }

    private void showAboutScreen() {
        switchToPanel(new AboutScreen(frame, this));
    }

    public void switchToPanel(JPanel panel) {
        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
}