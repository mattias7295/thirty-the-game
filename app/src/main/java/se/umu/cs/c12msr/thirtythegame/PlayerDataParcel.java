package se.umu.cs.c12msr.thirtythegame;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class exist to save the state of the players
 * to be able to restore when the app crashes or when the app
 * is no longer in the foreground.
 *
 * @author Mattias Scherer
 * @version 1.0
 */
public class PlayerDataParcel implements Parcelable {

    /*
     * Name of the player.
     */
    private String playerName;

    /*
     * the score for each choice
     * the player has.
     */
    private int[] playerScore;

    /*
     * the total score of playerScore
     * for the player
     */
    private int playerTotalScore;

    /*
     * how many turns the player has
     * played.
     */
    private int playerTurn;

    /**
     * Constructs a new <tt>PlayerDataParcel</tt> with the current state of a player.
     * @param playerName        name of the player
     * @param playerScore       score for each choice
     * @param playerTotalScore  total score
     * @param playerTurn        current turn
     */
    public PlayerDataParcel(String playerName, int[] playerScore, int playerTotalScore, int playerTurn) {
        this.playerName = playerName;
        this.playerScore = playerScore;
        this.playerTotalScore = playerTotalScore;
        this.playerTurn = playerTurn;
    }

    /**
     * @return the name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @return the score for each choice
     */
    public int[] getPlayerScore() {
        return playerScore;
    }

    /**
     * @return the total score
     */
    public int getPlayerTotalScore() {
        return playerTotalScore;
    }

    /**
     * @return current turn
     */
    public int getPlayerTurn() {
        return playerTurn;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.playerName);
        dest.writeIntArray(this.playerScore);
        dest.writeInt(this.playerTotalScore);
        dest.writeInt(this.playerTurn);
    }

    /*
       internal constructor.
     */
    protected PlayerDataParcel(Parcel in) {
        this.playerName = in.readString();
        this.playerScore = in.createIntArray();
        this.playerTotalScore = in.readInt();
        this.playerTurn = in.readInt();
    }

    public static final Parcelable.Creator<PlayerDataParcel> CREATOR = new Parcelable.Creator<PlayerDataParcel>() {

        @Override
        public PlayerDataParcel createFromParcel(Parcel source) {
            return new PlayerDataParcel(source);
        }

        @Override
        public PlayerDataParcel[] newArray(int size) {
            return new PlayerDataParcel[size];
        }
    };
}
