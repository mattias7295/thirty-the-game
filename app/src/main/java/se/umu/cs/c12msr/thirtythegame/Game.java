package se.umu.cs.c12msr.thirtythegame;

import java.util.Random;

/**
 * Created by Mattias Scherer on 2016-06-09.
 */
public class Game {

    private int mScore[];
    private int mTotalScore;
    private int mRolls;
    private int mTurns;
    private Integer mDiceValues[];
    private String mPlayerName;

    private PointsCalculator calculator;
    private Random generator;

    public Game(String playerName, PointsCalculator calculator) {
        this.calculator = calculator;
        this.mPlayerName = playerName;
        mRolls = 3;
        mTurns = 10;
        mDiceValues = new Integer[6];
        mScore = new int[10];
        generator = new Random(System.currentTimeMillis());
    }

    public String getPlayerName() {
        return mPlayerName;
    }



    public boolean isFinished() {
        return mTurns == 0;
    }

    public int rollsRemaining() {
        return mRolls;
    }

    public void roll(Integer dices[]) {
        for (int i = 0; i < dices.length; i++) {
            mDiceValues[dices[i]] = generator.nextInt(6) + 1;
        }
        mRolls--;
    }

    public void calculatePoints(String choice) {

        int val = calculator.calculate(mDiceValues, choice);
        if (choice.equals("Low")) {
            mScore[0] = val;
        } else {
            int x = Integer.parseInt(choice);
            mScore[x-3] = val;
        }
        System.out.println(choice + " " + val);
        mTotalScore += val;
    }

    public int getDiceValue(int dice) {
        return mDiceValues[dice];
    }

    public Integer[] getAllDiceValues() {
        return mDiceValues;
    }

    public void endTurn() {
        mTurns--;
        mRolls = 3;
    }

    public int getTotalScore() {
        return mTotalScore;
    }

    public int[] getScore() {
        return mScore;
    }
}
