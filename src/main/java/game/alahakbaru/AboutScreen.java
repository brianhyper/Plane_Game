package game.alahakbaru;

import javax.swing.*;
import java.awt.*;

public class AboutScreen extends JPanel {
    private final JFrame frame;
    private final MenuScreen menuScreen;

    public AboutScreen(JFrame frame, MenuScreen menuScreen) {
        this.frame = frame;
        this.menuScreen = menuScreen;
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 70));

        initUI();
    }

    private void initUI() {
        JTextArea aboutText = new JTextArea(
                "AlahAkbaru Game\n\n" +
                        "Inspired by Flappy Bird but with a twist!\n\n" +
                        "Control your plane with SPACEBAR to flap upwards\n" +
                        "Navigate through gaps in skyscrapers\n" +
                        "Avoid crashing into buildings or the ground\n\n" +
                        "Features:\n" +
                        "- Dynamic sky color changes every 20 points\n" +
                        "- Customizable graphics (replace plane.png and building.png)\n" +
                        "- Local high score tracking\n\n" +
                        "Press ESC during game to pause"
        );

        aboutText.setFont(new Font("Arial", Font.PLAIN, 20));
        aboutText.setForeground(Color.WHITE);
        aboutText.setBackground(new Color(30, 30, 70));
        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);

        add(aboutText, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.addActionListener(e -> menuScreen.switchToPanel(menuScreen));
        add(backButton, BorderLayout.SOUTH);
    }
}