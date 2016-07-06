package se.umu.cs.c12msr.thirtythegame;


import java.util.ArrayList;
import java.util.Locale;

/**
 * This class contains the state of the game.
 *
 * @author Mattias Scherer
 * @version 1.0
 */
public class Game {

    /*
     * List of players and their state.
     */
    private ArrayList<Player> mPlayers;

    /*
     * Index used on mPlayers to keep track
     * of whose turn it is.
     */
    private int mCurrentPlayer;

    /*
     * Stores the value of the dices which are currently displayed.
     */
    private int[] mDiceValues;

    /**
     * Constructs a new game.
     * @param numPlayers    the number of players
     * @param choices       the different choices the game has
     */
    public Game(int numPlayers, String[] choices) {
        mPlayers = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            String name = String.format(Locale.ENGLISH, "Player %d", i+1);
            mPlayers.add(new Player(name, new ThirtyPointCalculator(), choices));
        }
        mCurrentPlayer = 0;
        mDiceValues = new int[GameConstants.NUM_DICES.getValue()];
    }


    /**
     * Returns the current index.
     * @return the current index
     */
    public int getCurrentPlayerIndex() {
        return mCurrentPlayer;
    }

    /**
     * Returns the current player.
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return mPlayers.get(mCurrentPlayer);
    }

    /**
     * Moves the turn to the next player.
     */
    public void nextPlayer() {
        mCurrentPlayer++;
        if (mCurrentPlayer == mPlayers.size()) {
            mCurrentPlayer = 0;
        }
    }

    /**
     * Returns the values of the dices.
     * @return the values of the dices
     */
    public int[] getDiceValues() {
        return mDiceValues;
    }

    /**
     * Internally constructs a new game from a restored state.
     * @param players           list of players
     * @param currentPlayer     index of the current player
     * @param diceValues        values of the dices
     */
    private Game(ArrayList<Player> players, int currentPlayer, int[] diceValues) {
        mPlayers = players;
        mCurrentPlayer = currentPlayer;
        mDiceValues = diceValues;
    }

    /**
     * Restores the game from saved state and returns the restored game.
     * @param playersData       the saved state the players
     * @param choices           the choices the game has
     * @param currentPlayer     index of the current player
     * @param diceValues        values of the dices
     * @param rolls             the remaining rolls the current player has
     * @return                  the restored game state
     */
    public static Game restoreGame(PlayerDataParcel[] playersData, String[] choices,
                                   int currentPlayer, int[] diceValues, int rolls) {
        ArrayList<Player> players = new ArrayList<>(playersData.length);
        for (PlayerDataParcel playerInfo: playersData) {
            players.add(Player.restorePlayer(playerInfo, choices));
        }
        players.get(currentPlayer).setRollsRemaining(rolls);
        return new Game(players, currentPlayer, diceValues);
    }


    /**
     * Creates and returns the state of the players.
     * @return  the state of the players.
     */
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
