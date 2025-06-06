package game.alahakbaru;

import java.io.*;

public class HighScoreManager {
    private static final String HIGH_SCORE_FILE = "highscore.dat";

    public int loadHighScore() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGH_SCORE_FILE))) {
            return (int) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File doesn't exist or error reading - return 0
            return 0;
        }
    }

    public void saveHighScore(int score) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGH_SCORE_FILE))) {
            oos.writeObject(score);
        } catch (IOException e) {
            System.err.println("Error saving high score: " + e.getMessage());
        }
    }
}