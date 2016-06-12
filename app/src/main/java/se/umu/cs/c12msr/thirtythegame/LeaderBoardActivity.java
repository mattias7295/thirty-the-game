package se.umu.cs.c12msr.thirtythegame;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LeaderBoardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        Intent intent = getIntent();

        int score[] = intent.getIntArrayExtra(GameBoardActivity.SCORE);

        TableLayout tl = (TableLayout)findViewById(R.id.score_table);
        int numRows = tl.getChildCount();


        TextView playerTextView = new TextView(this);
        playerTextView.setText("player1");
        playerTextView.setTypeface(Typeface.DEFAULT_BOLD);
        View topChild = tl.getChildAt(0);
        if (topChild instanceof TableRow) {
            TableRow topRow = (TableRow)topChild;
            topRow.addView(playerTextView);
        }
        for (int i = 1; i < numRows-1; i++) {
            View child = tl.getChildAt(i);
            if (child instanceof TableRow) {
                TextView tmpView = new TextView(this);
                tmpView.setText(String.format("%d", score[i-1]));

                TableRow row = (TableRow)child;
                row.addView(tmpView);
            }

        }
        TextView totalScoreView = new TextView(this);
        totalScoreView.setText("1");
        totalScoreView.setTypeface(Typeface.DEFAULT_BOLD);
        View bottomChild = tl.getChildAt(numRows-1);
        if (bottomChild instanceof TableRow) {
            TableRow bottomRow = (TableRow)bottomChild;
            bottomRow.addView(totalScoreView);
        }

    }


}
