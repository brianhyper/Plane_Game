package game.alahakbaru;

public enum MenuState {
    PLAY("Play Game"),
    RESUME("Resume Game"),
    HIGH_SCORES("High Scores"),
    EXIT("Exit Game");

    private final String label;

    MenuState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public MenuState next() {
        MenuState[] values = values();
        return values[(ordinal() + 1) % values.length];
    }

    public MenuState previous() {
        MenuState[] values = values();
        return values[(ordinal() + values.length - 1) % values.length];
    }
}