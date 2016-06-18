package se.umu.cs.c12msr.thirtythegame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Mattias Scherer on 2016-06-09.
 */
public class Player {


    private int mScore[];
    private int mTotalScore;
    private int mRolls;
    private int mTurn;
    private int mDiceValues[];
    private String mPlayerName;

    private ArrayList<String> mChoices;
    private PointsCalculator calculator;
    private Random generator;

    public Player(String playerName, PointsCalculator calculator, String[] choices) {
        this.mPlayerName = playerName;
        this.calculator = calculator;
        mRolls = 3;
        mTurn = 1;
        mDiceValues = new int[GameConstants.NUM_DICES.getValue()];
        mScore = new int[GameConstants.NUM_CHOICES.getValue()];
        generator = new Random();
        mChoices = new ArrayList<>(Arrays.asList(choices));
    }

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

    public void setRollsRemaining(int value) {
        mRolls = value;
    }

    public ArrayList<String> getChoices() {
        return mChoices;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public int getTurn() {
        return mTurn;
    }

    public boolean isFinished() {
        return mTurn == 11;
    }

    public boolean canRoll() {
        return mRolls > 0;
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
        mTotalScore += val;

        mChoices.remove(choice);
    }

    public int getDiceValue(int dice) {
        return mDiceValues[dice];
    }

    public int[] getAllDiceValues() {
        return mDiceValues;
    }

    public void endTurn() {
        mTurn++;
        mRolls = 3;
    }

    public int getTotalScore() {
        return mTotalScore;
    }

    public int[] getScore() {
        return mScore;
    }
}
