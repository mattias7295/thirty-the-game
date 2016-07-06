package se.umu.cs.c12msr.thirtythegame;

/**
 * This enum contains constants which
 * are defined by the rules of the game.
 *
 * @author Mattias Scherer
 * @version 1.0
 */
public enum GameConstants {
    NUM_ROLLS (3),
    NUM_DICES (6),
    NUM_TURNS (3),
    NUM_CHOICES (10);

    private final int value;

    GameConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
