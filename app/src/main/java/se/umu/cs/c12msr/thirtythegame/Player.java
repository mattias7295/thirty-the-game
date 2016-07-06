package se.umu.cs.c12msr.thirtythegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * This class contains information about the player's
 * current state in the game.
 *
 * @author Mattias Scherer
 * @version 1.0
 */
public class Player {

    /*
     * score for each choice.
     */
    private int mScore[];

    /*
     * total score of all choices.
     */
    private int mTotalScore;

    /*
     * roll counter.
     */
    private int mRolls;

    /*
     * Turn counter.
     */
    private int mTurn;

    /*
     * Value of each dice.
     */
    private int mDiceValues[];

    /*
     * Name of the player.
     */
    private String mPlayerName;

    /*
     * List of choices. Choices will be removed
     * as they are used up.
     */
    private ArrayList<String> mChoices;

    /*
     * Point calculator. Decides how the points will
     * be calculated.
     */
    private PointsCalculator calculator;

    /*
     * Random number generator for dice rolls.
     */
    private Random generator;

    /**
     * Construct a new player.
     * @param playerName    name of the player
     * @param calculator    a point calculator
     * @param choices       list of choices
     */
    public Player(String playerName, PointsCalculator calculator, String[] choices) {
        this.mPlayerName = playerName;
        this.calculator = calculator;
        mRolls = GameConstants.NUM_ROLLS.getValue();
        mTurn = 1;
        mDiceValues = new int[GameConstants.NUM_DICES.getValue()];
        mScore = new int[GameConstants.NUM_CHOICES.getValue()];
        generator = new Random();
        mChoices = new ArrayList<>(Arrays.asList(choices));
    }

    /*
     Internal constructor for when a player is restored from a saved state.
     */
    private Player(String mPlayerName, PointsCalculator calculator, ArrayList<String> remainingChoices,
                  int[] score, int totalScore, int turn) {
        this.mPlayerName = mPlayerName;
        this.calculator = calculator;
        mChoices = remainingChoices;
        mScore = score;
        mTotalScore = totalScore;
        mTurn = turn;
        generator = new Random();
    }

    /**
     * Restore a player from a saved state with <tt>PlayerDataParcel</tt>.
     * @param playerInfo    the saved state of the player
     * @param choices       list of choices
     * @return              the restored player
     */
    public static Player restorePlayer(PlayerDataParcel playerInfo, String[] choices) {
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < playerInfo.getPlayerScore().length; i++) {
            if (playerInfo.getPlayerScore()[i] == 0) {
                res.add(choices[i]);
            }
        }

        return new Player(playerInfo.getPlayerName(), new ThirtyPointCalculator(),
                res, playerInfo.getPlayerScore(), playerInfo.getPlayerTotalScore(),
                playerInfo.getPlayerTurn());
    }

    /**
     * Sets the remaining rolls this player has.
     * @param value the amount of rolls remaining
     * @throws IllegalArgumentException if {@code value < 0}
     */
    public void setRollsRemaining(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }
        mRolls = value;
    }

    /**
     * @return the list of choices this player has
     */
    public ArrayList<String> getChoices() {
        return mChoices;
    }

    /**
     * @return this player's name
     */
    public String getPlayerName() {
        return mPlayerName;
    }

    /**
     * @return which turn this player is at
     */
    public int getTurn() {
        return mTurn;
    }

    /**
     * @return  true if this player has finished 10 turns
     *          (players starts at turn 1), otherwise false
     */
    public boolean isFinished() {
        return mTurn == GameConstants.NUM_TURNS.getValue()+1;
    }

    /**
     * @return true if {@code mRolls > 0}, otherwise false
     */
    public boolean canRoll() {
        return mRolls > 0;
    }

    /**
     * @return the remaining rolls this player has
     */
    public int rollsRemaining() {
        return mRolls;
    }

    /**
     * Generate a new number for each dice with
     * a value from 1 to 6 inclusive. Decrements the roll
     * counter by 1 for this player.
     * @param dices the array of dices by their index that will
     *              be generated a value
     */
    public void roll(Integer dices[]) {
        for (int i = 0; i < dices.length; i++) {
            mDiceValues[dices[i]] = generator.nextInt(6) + 1;
        }
        mRolls--;
    }

    /**
     * Calculates the amount of points this
     * player will receive depending on the choice.
     * The choice will be removed from the list of
     * available choices this player has.
     * @param choice the choice to calculate
     *               points by
     */
    public void calculatePoints(String choice) {

        int val = calculator.calculate(mDiceValues, choice);
        if (choice.equals("Low")) {
            mScore[0] = val;
        } else {
            int x = Integer.parseInt(choice);
            /* the value of x can be from 4 to 12 inclusive */
            mScore[x-3] = val;
        }
        mTotalScore += val;

        mChoices.remove(choice);
    }

    /**
     * @param   dice the index
     * @return  the value of the dice with index <tt>dice</tt>
     */
    public int getDiceValue(int dice) {
        return mDiceValues[dice];
    }

    /**
     * @return the value of all dices
     */
    public int[] getAllDiceValues() {
        return mDiceValues;
    }

    /**
     * When this player ends a turn, the turn counter increment by 1
     * and the roll counter resets to 3.
     */
    public void endTurn() {
        mTurn++;
        mRolls = GameConstants.NUM_ROLLS.getValue();
    }

    /**
     * @return the total score of this player
     */
    public int getTotalScore() {
        return mTotalScore;
    }

    /**
     * @return an array of scores for each choice of this player
     */
    public int[] getScore() {
        return mScore;
    }
}
