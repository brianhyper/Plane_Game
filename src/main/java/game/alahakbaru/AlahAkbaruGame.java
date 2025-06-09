package game.alahakbaru;

import javax.swing.*;

public class AlahAkbaruGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("AlahAkbaru");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            WelcomeScreen welcomeScreen = new WelcomeScreen(frame);
            frame.add(welcomeScreen);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            welcomeScreen.playIntro();
        });
    }
}