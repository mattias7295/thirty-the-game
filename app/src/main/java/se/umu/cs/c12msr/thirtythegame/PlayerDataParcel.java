package se.umu.cs.c12msr.thirtythegame;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mattias Scherer on 2016-06-13.
 */
public class PlayerDataParcel implements Parcelable {

    /* player name */
    private String playerName;
    /* score for each choice */
    private int[] playerScore;
    /* total score */
    private int playerTotalScore;
    /* current turn */
    private int playerTurn;

    public PlayerDataParcel(String playerName, int[] playerScore, int playerTotalScore, int playerTurn) {
        this.playerName = playerName;
        this.playerScore = playerScore;
        this.playerTotalScore = playerTotalScore;
        this.playerTurn = playerTurn;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int[] getPlayerScore() {
        return playerScore;
    }

    public int getPlayerTotalScore() {
        return playerTotalScore;
    }

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
