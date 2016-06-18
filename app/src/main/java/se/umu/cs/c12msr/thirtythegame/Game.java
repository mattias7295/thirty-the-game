package se.umu.cs.c12msr.thirtythegame;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Mattias Scherer on 2016-06-17.
 */
public class Game {

    private ArrayList<Player> mPlayers;
    private int mCurrentPlayer;
    private int[] mDiceValues;

    public Game(String[] choices) {
        this(1, choices);
    }

    public Game(int numPlayers, String[] choices) {
        mPlayers = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            String name = String.format("Player %d", i+1);
            mPlayers.add(new Player(name, new ThirtyPointCalculator(), choices));
        }
        mCurrentPlayer = 0;
        mDiceValues = new int[GameConstants.NUM_DICES.getValue()];
    }

    public int getCurrentPlayerIndex() {
        return mCurrentPlayer;
    }

    public Player getCurrentPlayer() {
        return mPlayers.get(mCurrentPlayer);
    }

    public void nextPlayer() {
        mCurrentPlayer++;
        if (mCurrentPlayer == mPlayers.size()) {
            mCurrentPlayer = 0;
        }
    }

    public int[] getDiceValues() {
        return mDiceValues;
    }

    private Game(ArrayList<Player> players, int currentPlayer, int[] diceValues) {
        mPlayers = players;
        mCurrentPlayer = currentPlayer;
        mDiceValues = diceValues;
    }

    public static Game restoreGame(PlayerDataParcel[] playersData, String[] choices,
                                   int currentPlayer, int[] diceValues, int rolls) {
        ArrayList<Player> players = new ArrayList<>(playersData.length);
        for (PlayerDataParcel playerInfo: playersData) {
            players.add(Player.restorePlayer(playerInfo, choices));
        }
        players.get(currentPlayer).setRollsRemaining(rolls);
        return new Game(players, currentPlayer, diceValues);
    }


    public PlayerDataParcel[] getPlayersDataParcel() {
        PlayerDataParcel[] dataParcels = new PlayerDataParcel[mPlayers.size()];

        for (int i = 0; i < mPlayers.size(); i++) {
            Player player = mPlayers.get(i);
            dataParcels[i] = new PlayerDataParcel(player.getPlayerName(), player.getScore(),
                    player.getTotalScore(), player.getTurn());
        }
        return dataParcels;
    }
}
