package se.umu.cs.c12msr.thirtythegame;

/**
 * Created by Mattias Scherer on 2016-06-17.
 */
public enum GameConstants {
    NUM_DICES (6),
    NUM_CHOICES (10);

    private final int value;

    GameConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
