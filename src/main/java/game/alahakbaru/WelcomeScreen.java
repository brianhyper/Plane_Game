package game.alahakbaru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JPanel {
    private final JFrame frame;
    private Timer timer;
    private int animationProgress = 0;

    public WelcomeScreen(JFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
    }

    public void playIntro() {
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationProgress++;
                repaint();
                if (animationProgress >= 100) {
                    timer.stop();
                    showMainMenu();
                }
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Title
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        String title = "ALAH AKBARU";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, 150);

        // Animation
        int planeX = animationProgress * 8;
        int planeY = 300 - Math.abs(50 - animationProgress % 100) * 2;
        g2d.setColor(Color.RED);
        g2d.fillRect(planeX, planeY, 40, 20);

        if (animationProgress > 70) {
            g2d.setColor(Color.ORANGE);
            g2d.fillRect(700, 200, 50, 400);
            g2d.setColor(Color.RED);
            g2d.fillOval(690, 300, 70, 70);
        }
    }

    private void showMainMenu() {
        frame.getContentPane().removeAll();
        frame.add(new MenuScreen(frame));
        frame.revalidate();
        frame.repaint();
    }
}