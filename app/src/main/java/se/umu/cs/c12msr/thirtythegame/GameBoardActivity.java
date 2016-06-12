package se.umu.cs.c12msr.thirtythegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameBoardActivity extends AppCompatActivity {

    public final static String SCORE = "se.umu.cs.c12msr.thirtythegame.VALUES";

    private ImageButton mDiceButtons[];
    private TextView mScoreView;
    private Game mGame;
    private Button mRollButton;
    private Spinner mChoicesSpinner;
    private String mSelectedChoice;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        mDiceButtons = new ImageButton[] {
                (ImageButton) findViewById(R.id.dice1),
                (ImageButton) findViewById(R.id.dice2),
                (ImageButton) findViewById(R.id.dice3),
                (ImageButton) findViewById(R.id.dice4),
                (ImageButton) findViewById(R.id.dice5),
                (ImageButton) findViewById(R.id.dice6)
        };

        mScoreView = (TextView)findViewById(R.id.score_view);
        mRollButton = (Button)findViewById(R.id.roll_button);

        mChoicesSpinner = (Spinner)findViewById(R.id.choices_spinner);
        String tempArray[] = getResources().getStringArray(R.array.choices_array);
        ArrayList<String> choicesArray = new ArrayList<>(Arrays.asList(tempArray));

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choicesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChoicesSpinner.setAdapter(adapter);

        mChoicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedChoice = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSelectedChoice = adapter.getItem(0);


        mGame = new Game("player1", new ThirtyPointCalculator());
        updateRollButton();
    }

    public void rollDice(View view) {
        List<Integer> dices = new ArrayList<>();

        for (int i = 0; i < mDiceButtons.length; i++) {
            if (!mDiceButtons[i].isSelected()) {
                dices.add(i);
            }
        }
        mGame.roll(dices.toArray(new Integer[dices.size()]));

        updateDiceButtons();
        updateRollButton();

        if (mGame.rollsRemaining() == 0) {
            mRollButton.setEnabled(false);
        }
    }

    public void showLeaderBoardActivity(View view) {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        intent.putExtra(SCORE, mGame.getScore());

        startActivity(intent);
    }

    private void updateDiceButtons() {
        int value;
        for (int i = 0; i < mDiceButtons.length; i++) {
            value = mGame.getDiceValue(i);
            switch (value) {
                case 1: mDiceButtons[i].setImageResource(R.drawable.white1); break;
                case 2: mDiceButtons[i].setImageResource(R.drawable.white2); break;
                case 3: mDiceButtons[i].setImageResource(R.drawable.white3); break;
                case 4: mDiceButtons[i].setImageResource(R.drawable.white4); break;
                case 5: mDiceButtons[i].setImageResource(R.drawable.white5); break;
                case 6: mDiceButtons[i].setImageResource(R.drawable.white6); break;
                    default:
                        break;
            }
        }
    }

    public void dicePressed(View view) {
        ImageButton ib = (ImageButton)view;
        ib.setSelected(!ib.isSelected());
    }

    public void confirmedPressed(View view) {
        if (!mSelectedChoice.equals("None")) {
            mGame.calculatePoints(mSelectedChoice);
            adapter.remove(mSelectedChoice);
        }
        mSelectedChoice = (String)mChoicesSpinner.getSelectedItem();
        mGame.endTurn();
        updateRollButton();
        updateScoreView();
        mRollButton.setEnabled(true);
    }

    private void updateRollButton() {
        mRollButton.setText(String.format("Rolls: %d", mGame.rollsRemaining()));
    }

    private void updateScoreView() {
        mScoreView.setText(String.format("Score: %d", mGame.getTotalScore()));
    }
}
