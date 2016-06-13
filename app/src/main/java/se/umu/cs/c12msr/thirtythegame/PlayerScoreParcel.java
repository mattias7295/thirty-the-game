package se.umu.cs.c12msr.thirtythegame;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mattias Scherer on 2016-06-13.
 */
public class PlayerScoreParcel implements Parcelable {

    /* list of players */
    private String[] playerName;
    /* 2D array of players score for each choice */
    private int[] playerScore;
    /* total score of the players */
    private int[] playerTotalScore;

    public PlayerScoreParcel(String[] playerName, int[] playerScore, int[] playerTotalScore) {
        this.playerName = playerName;
        this.playerScore = playerScore;
        this.playerTotalScore = playerTotalScore;
    }

    public String[] getPlayerName() {
        return playerName;
    }

    public int[] getPlayerScore() {
        return playerScore;
    }

    public int[] getPlayerTotalScore() {
        return playerTotalScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.playerName);
        dest.writeIntArray(this.playerScore);
        dest.writeIntArray(this.playerTotalScore);
    }

    protected PlayerScoreParcel(Parcel in) {
        this.playerName = in.createStringArray();
        this.playerScore = in.createIntArray();
        this.playerTotalScore = in.createIntArray();
    }

    public static final Parcelable.Creator<PlayerScoreParcel> CREATOR = new Parcelable.Creator<PlayerScoreParcel>() {
        @Override
        public PlayerScoreParcel createFromParcel(Parcel source) {
            return new PlayerScoreParcel(source);
        }

        @Override
        public PlayerScoreParcel[] newArray(int size) {
            return new PlayerScoreParcel[size];
        }
    };
}
