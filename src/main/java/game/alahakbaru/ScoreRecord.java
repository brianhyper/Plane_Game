package game.alahakbaru;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreRecord implements Serializable, Comparable<ScoreRecord> {
    private final int score;
    private final LocalDateTime dateTime;

    public ScoreRecord(int score) {
        this.score = score;
        this.dateTime = LocalDateTime.now();
    }

    public int getScore() {
        return score;
    }

    public String getFormattedDateTime() {
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }

    @Override
    public int compareTo(ScoreRecord other) {
        return Integer.compare(other.score, this.score); // Descending order
    }

    @Override
    public String toString() {
        return score + " - " + getFormattedDateTime();
    }
}