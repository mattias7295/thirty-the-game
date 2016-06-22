package se.umu.cs.c12msr.thirtythegame;

import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
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

        /* fill names */
        for (int i = 0; i < numPlayers; i++) {
            View topChild = tl.getChildAt(0);
            if (topChild instanceof TableRow) {
                TextView tvName = (TextView) View.inflate(this, R.layout.text_view_right_aligned, null);
                tvName.setText(playersInfo[i].getPlayerName());
                tvName.setTypeface(Typeface.DEFAULT_BOLD);
                TableRow topRow = (TableRow)topChild;
                topRow.addView(tvName);
            }
        }

        /* draw divider */
        for (int i = 0; i < numPlayers; i++) {
            View child = tl.getChildAt(1);
            if (child instanceof TableRow) {
                View rowDivider = View.inflate(this, R.layout.black_divider, null);
                TableRow row = (TableRow) child;
                row.addView(rowDivider);
            }
        }

        /* fill table with score */
        for (int i = 0; i < numPlayers; i++) {
            for (int j = 2; j < numRows-2; j++) {
                View child = tl.getChildAt(j);
                if (child instanceof TableRow) {
                    TextView tvScore = (TextView) View.inflate(this, R.layout.text_view_right_aligned, null);
                    tvScore.setText(String.format("%d", playersInfo[i].getPlayerScore()[j-2]));
                    TableRow row = (TableRow)child;
                    row.addView(tvScore);
                }
            }
        }

        /* draw divider */
//        for (int i = 0; i < numPlayers; i++) {
//            View child = tl.getChildAt(numRows-2);
//            if (child instanceof TableRow) {
//                View rowDivider = View.inflate(this, R.layout.black_divider, null);
//                TableRow row = (TableRow) child;
//                row.addView(rowDivider);
//            }
//        }

        /* fill total score */
        for (int i = 0; i < numPlayers; i++) {
            TextView tvTotalScore = (TextView) View.inflate(this, R.layout.text_view_right_aligned, null);
            tvTotalScore.setText(String.format("%d",playersInfo[i].getPlayerTotalScore()));
            tvTotalScore.setTypeface(Typeface.DEFAULT_BOLD);
            View bottomChild = tl.getChildAt(numRows-1);
            if (bottomChild instanceof TableRow) {
                TableRow bottomRow = (TableRow)bottomChild;
                bottomRow.addView(tvTotalScore);
            }
        }

    }
}
