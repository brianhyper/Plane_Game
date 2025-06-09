package game.alahakbaru;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class HighScoreManager {
    private static final String FILE_PATH = "highscores.dat";
    private static final int MAX_SCORES = 10;

    public static void saveScore(int score) {
        List<Integer> scores = loadScores();
        scores.add(score);
        scores.sort(Collections.reverseOrder());

        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            System.err.println("Error saving scores: " + e.getMessage());
        }
    }

    public static List<Integer> loadScores() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (List<Integer>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error loading scores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void showHighScores(JFrame parent) {
        List<Integer> scores = loadScores();

        StringBuilder sb = new StringBuilder("<html><h1>High Scores</h1><ol>");
        for (int i = 0; i < Math.min(scores.size(), MAX_SCORES); i++) {
            sb.append("<li>").append(scores.get(i)).append("</li>");
        }
        sb.append("</ol></html>");

        JOptionPane.showMessageDialog(parent, sb.toString(),
                "Top Scores", JOptionPane.INFORMATION_MESSAGE);
    }
}