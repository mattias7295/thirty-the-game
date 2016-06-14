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
    private TextView mTurnView;
    private TextView mPlayerView;

    private Button mRollButton;

    private Spinner mChoicesSpinner;

    private String mSelectedChoice;
    private ArrayAdapter<String> adapter;
    private ArrayList<Game> mPlayersGameState;
    private int mCurrentPlayer;

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
        mTurnView = (TextView) findViewById(R.id.turn_text_view);
        mPlayerView = (TextView) findViewById(R.id.player_text_view);

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
        mSelectedChoice = (String) mChoicesSpinner.getSelectedItem();

        Intent intent = getIntent();
        int numPlayers = intent.getIntExtra(MenuActivity.NUM_PLAYERS, 1);
        mPlayersGameState = new ArrayList<>();
        ThirtyPointCalculator calc = new ThirtyPointCalculator();
        for (int i = 0; i < numPlayers; i++) {
            mPlayersGameState.add(new Game(String.format("Player %d", i+1), calc));
        }
        updateRollButton();
    }

    public void rollDice(View view) {
        List<Integer> dices = new ArrayList<>();

        for (int i = 0; i < mDiceButtons.length; i++) {
            if (!mDiceButtons[i].isSelected()) {
                dices.add(i);
            }
        }
        mPlayersGameState.get(mCurrentPlayer).roll(dices.toArray(new Integer[dices.size()]));

        updateDiceButtons();
        updateRollButton();

        if (mPlayersGameState.get(mCurrentPlayer).rollsRemaining() == 0) {
            mRollButton.setEnabled(false);
        }
    }

    public void showLeaderBoardActivity(View view) {
        String[] playerNames = new String[mPlayersGameState.size()];
        int[] playersScore = new int[mPlayersGameState.size()*10];
        int[] playersTotalScore = new int[mPlayersGameState.size()];
        for (int i = 0; i < mPlayersGameState.size(); i++) {
            Game player = mPlayersGameState.get(i);
            playerNames[i] = player.getPlayerName();
            System.arraycopy(player.getScore(), 0, playersScore, i*10, player.getScore().length);
            playersTotalScore[i] = mPlayersGameState.get(i).getTotalScore();
        }

        PlayerScoreParcel dataParcel = new PlayerScoreParcel(playerNames, playersScore, playersTotalScore);
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        intent.putExtra(SCORE, dataParcel);

        startActivity(intent);
    }

    private void updateDiceButtons() {
        int value;
        for (int i = 0; i < mDiceButtons.length; i++) {
            value = mPlayersGameState.get(mCurrentPlayer).getDiceValue(i);
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
            mPlayersGameState.get(mCurrentPlayer).calculatePoints(mSelectedChoice);
            adapter.remove(mSelectedChoice);
        }
        mSelectedChoice = (String)mChoicesSpinner.getSelectedItem();
        mPlayersGameState.get(mCurrentPlayer).endTurn();
        updateRollButton();
        updateScoreView();
        mRollButton.setEnabled(true);
    }

    private void updateTurnTextView() {

    }

    private void updatePlayerTextView() {

    }

    private void updateRollButton() {
        mRollButton.setText(String.format("Rolls: %d", mPlayersGameState.get(mCurrentPlayer).rollsRemaining()));
    }

    private void updateScoreView() {
        mScoreView.setText(String.format("Score: %d", mPlayersGameState.get(mCurrentPlayer).getTotalScore()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {


        super.onSaveInstanceState(outState);
    }
}
