package game.alahakbaru;

import java.io.*;
import java.util.*;

public class HighScoreManager {
    private static final String HIGH_SCORE_FILE = "highscore.dat";
    private List<ScoreRecord> highScores;

    public HighScoreManager() {
        highScores = loadHighScores();
    }

    @SuppressWarnings("unchecked")
    private List<ScoreRecord> loadHighScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGH_SCORE_FILE))) {
            return (List<ScoreRecord>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public void addScore(int score) {
        highScores.add(new ScoreRecord(score));
        Collections.sort(highScores);

        // Keep only top 10 scores
        if (highScores.size() > 10) {
            highScores = highScores.subList(0, 10);
        }

        saveHighScores();
    }

    private void saveHighScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGH_SCORE_FILE))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    public List<ScoreRecord> getHighScores() {
        return new ArrayList<>(highScores);
    }

    public int getHighestScore() {
        return highScores.isEmpty() ? 0 : highScores.get(0).getScore();
    }
}