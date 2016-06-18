package se.umu.cs.c12msr.thirtythegame;

import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;

public class LeaderBoardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        Parcelable[] parcelables = getIntent().getParcelableArrayExtra(GameBoardActivity.LEADERBOARD_SCORE);
        PlayerDataParcel[] playersInfo = Arrays.copyOf(parcelables, parcelables.length, PlayerDataParcel[].class);
        int numPlayers = playersInfo.length;

        TableLayout tl = (TableLayout)findViewById(R.id.score_table);
        int numRows = tl.getChildCount();


        for (int i = 0; i < numPlayers; i++) {
            TextView playerTextView = new TextView(this);
            playerTextView.setText(playersInfo[i].getPlayerName());
            playerTextView.setTypeface(Typeface.DEFAULT_BOLD);
            View topChild = tl.getChildAt(0);
            if (topChild instanceof TableRow) {
                TableRow topRow = (TableRow)topChild;
                topRow.addView(playerTextView);
            }
        }

        for (int i = 0; i < numPlayers; i++) {
            for (int j = 1; j < numRows-1; j++) {
                View child = tl.getChildAt(j);
                if (child instanceof TableRow) {
                    TextView tmpView = new TextView(this);
                    tmpView.setText(String.format("%d", playersInfo[i].getPlayerScore()[j-1]));
                    TableRow row = (TableRow)child;
                    row.addView(tmpView);
                }
            }
        }
        for (int i = 0; i < numPlayers; i++) {
            TextView totalScoreView = new TextView(this);
            totalScoreView.setText(String.format("%d",playersInfo[i].getPlayerTotalScore()));
            totalScoreView.setTypeface(Typeface.DEFAULT_BOLD);
            View bottomChild = tl.getChildAt(numRows-1);
            if (bottomChild instanceof TableRow) {
                TableRow bottomRow = (TableRow)bottomChild;
                bottomRow.addView(totalScoreView);
            }
        }

    }
}
