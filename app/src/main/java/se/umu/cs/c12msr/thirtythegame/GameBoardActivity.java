package se.umu.cs.c12msr.thirtythegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameBoardActivity extends AppCompatActivity {

    private final static String TAG = "GameBoardActivity";


    public final static String LEADERBOARD_SCORE = "se.umu.cs.c12msr.thirtythegame.VALUES";

    private final static String GAME_PLAYERS_DATA = "playersdata";
    private final static String GAME_CURRENT_PLAYER = "currentplayer";
    private final static String GAME_ROLLS_REMAINING = "rollsremaining";
    private final static String GAME_CURRENT_DICE_VALUES = "dicevalues";

    private ImageButton mDiceButtons[];

    private TextView mScoreView;
    private TextView mTurnView;
    private TextView mPlayerView;

    private Button mRollButton;

    private Spinner mChoicesSpinner;

    private String mSelectedChoice;
    private ArrayAdapter<String> adapter;
    private Game game;


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

        initChoicesSpinner();


        if (savedInstanceState == null) {
            Log.i(TAG, "creating new game");
            Intent intent = getIntent();
            int numPlayers = intent.getIntExtra(MenuActivity.NUM_PLAYERS, 1);
            String[] choices = getResources().getStringArray(R.array.choices_array);
            game = new Game(numPlayers, choices);

            lockDiceButtons();
            updateGameBoard();
            updateRollButton();
        }
    }

    private void initChoicesSpinner() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChoicesSpinner.setAdapter(adapter);

        mChoicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedChoice = (String) parent.getItemAtPosition(position);
                Log.i(TAG, "item: " + mSelectedChoice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "method=onSaveInstanceState");
        PlayerDataParcel[] dataToSave = game.getPlayersDataParcel();
        outState.putParcelableArray(GAME_PLAYERS_DATA, dataToSave);
        outState.putInt(GAME_CURRENT_PLAYER, game.getCurrentPlayerIndex());
        outState.putInt(GAME_ROLLS_REMAINING, game.getCurrentPlayer().rollsRemaining());
        outState.putIntArray(GAME_CURRENT_DICE_VALUES, game.getDiceValues());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "method=onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);

        PlayerDataParcel[] savedPlayers = (PlayerDataParcel[]) savedInstanceState.getParcelableArray(GAME_PLAYERS_DATA);
        String[] choices = getResources().getStringArray(R.array.choices_array);
        int currentPlayer = savedInstanceState.getInt(GAME_CURRENT_PLAYER);
        int[] diceValues = savedInstanceState.getIntArray(GAME_CURRENT_DICE_VALUES);
        int rolls = savedInstanceState.getInt(GAME_ROLLS_REMAINING);
        game = Game.restoreGame(savedPlayers, choices, currentPlayer, diceValues, rolls);

        updateGameBoard();
        updateRollButton();
        updateDiceButtons();
        mSelectedChoice = (String) mChoicesSpinner.getSelectedItem();
        Player player = game.getCurrentPlayer();
        if (player.rollsRemaining() == 3) {
            lockDiceButtons();
        } else if (!player.canRoll()) {
            mRollButton.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG,"method=onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(TAG,"method=onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "method=onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.i(TAG,"method=onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG,"method=onRestart");
        super.onRestart();
    }

    public void rollDice(View view) {
        Player currentPlayer = game.getCurrentPlayer();

        if (currentPlayer.rollsRemaining() == 3) {
            releaseDiceButtons();
        }

        List<Integer> dices = new ArrayList<>();
        for (int i = 0; i < mDiceButtons.length; i++) {
            if (!mDiceButtons[i].isSelected()) {
                dices.add(i);
            }
        }
        currentPlayer.roll(dices.toArray(new Integer[dices.size()]));

        updateDiceButtons();
        updateRollButton();

        if (!currentPlayer.canRoll()) {
            mRollButton.setEnabled(false);
        }
    }

    public void showLeaderBoardActivity(View view) {
        PlayerDataParcel[] dataParcel = game.getPlayersDataParcel();
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        intent.putExtra(LEADERBOARD_SCORE, dataParcel);
        startActivity(intent);
    }



    private void releaseDiceButtons() {
        for (ImageButton b :
                mDiceButtons) {
            b.setEnabled(true);
        }
    }

    private void lockDiceButtons() {
        for (ImageButton b :
                mDiceButtons) {
            b.setEnabled(false);
        }   
    }
    
    private void deselectDiceButtons() {
        for (ImageButton b :
                mDiceButtons) {
            b.setSelected(false);
        }
    }

    private void updateDiceButtons() {
        int value;
        for (int i = 0; i < mDiceButtons.length; i++) {
            value = game.getCurrentPlayer().getDiceValue(i);
            game.getDiceValues()[i] = value;
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
        Log.i(TAG, "Choice: " + mSelectedChoice);
        if (!mSelectedChoice.equals("None")) {
            game.getCurrentPlayer().calculatePoints(mSelectedChoice);
        }

        game.getCurrentPlayer().endTurn();

        mRollButton.setEnabled(true);
        deselectDiceButtons();
        lockDiceButtons();

        game.nextPlayer();

        updateRollButton();
        updateGameBoard();
        mSelectedChoice = mChoicesSpinner.getSelectedItem().toString();
    }

    private void updateGameBoard() {
        mTurnView.setText(String.format("Turn: %d", game.getCurrentPlayer().getTurn()));
        mPlayerView.setText(game.getCurrentPlayer().getPlayerName());
        mScoreView.setText(String.format("Score: %d", game.getCurrentPlayer().getTotalScore()));

        adapter.clear();
        adapter.addAll(game.getCurrentPlayer().getChoices());
    }


    private void updateRollButton() {
        mRollButton.setText(String.format("Rolls: %d", game.getCurrentPlayer().rollsRemaining()));
    }



}
