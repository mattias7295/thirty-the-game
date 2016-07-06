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
import java.util.Locale;

/**
 * This class handles the lifecycle and other events when
 * displaying the gameboard to the user.
 *
 * @author Mattias Scherer
 * @version 1.0
 */
public class GameBoardActivity extends AppCompatActivity {

    /*
     * Debug tag for logging debug output to LogCat.
     */
    private final static String TAG = "GameBoardActivity";

    /**
     * Key used to receive the players score from an intent
     */
    public final static String LEADERBOARD_SCORE = "se.umu.cs.c12msr.thirtythegame.VALUES";

    /*
     * Keys used when saving/restoring the state of the game.
     */
    private final static String GAME_PLAYERS_DATA = "playersdata";
    private final static String GAME_CURRENT_PLAYER = "currentplayer";
    private final static String GAME_ROLLS_REMAINING = "rollsremaining";
    private final static String GAME_CURRENT_DICE_VALUES = "dicevalues";

    /*
     * The dices on this activity.
     */
    private ImageButton mDiceButtons[];

    /*
     * Displays the current player's score.
     */
    private TextView mScoreView;

    /*
     * Displays the current player's turn.
     */
    private TextView mTurnView;

    /*
     * Displays the current player's name.
     */
    private TextView mPlayerView;

    /*
     * Roll dices when pressed.
     */
    private Button mRollButton;

    /*
     * Displays choices.
     */
    private Spinner mChoicesSpinner;

    /*
     * Selected choice.
     */
    private String mSelectedChoice;

    /*
     * Adapter for mChoicesSpinner.
     */
    private ArrayAdapter<String> adapter;

    /*
     * The game state.
     */
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
        if (player.rollsRemaining() == GameConstants.NUM_ROLLS.getValue()) {
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

    /**
     * Rolls the dices when <tt>view</tt> is pressed
     * @param view the view that was pressed.
     */
    public void rollDice(View view) {
        Player currentPlayer = game.getCurrentPlayer();

        /* Re-enable the dices after the first roll */
        if (currentPlayer.rollsRemaining() == GameConstants.NUM_ROLLS.getValue()) {
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
    /**
     * Starts <tt>LeaderBoardActivity</tt> when <tt>view</tt> is pressed.
     * @param view the view that was pressed
     */
    public void leaderButtonPressed(View view) {
        if (view.getId() == R.id.leader_board_button) {
            showLeaderBoardActivity();
        }
    }


    private void showLeaderBoardActivity() {
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

    /*
     * Set the image of a button the corresponding dice value.
     */
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

    /**
     * Toggle disable/enable a dice when pressed.
     * @param view the view that was pressed
     */
    public void dicePressed(View view) {
        ImageButton ib = (ImageButton)view;
        ib.setSelected(!ib.isSelected());
    }

    /**
     * Calculate points and end the turn. Move the turn to the next
     * player and update the gameboard.
     * @param view the view that was pressed
     */
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

        if (game.getCurrentPlayer().isFinished()) {
            showLeaderBoardActivity();
            lockDiceButtons();
            mRollButton.setEnabled(false);
        } else {
            updateRollButton();
            updateGameBoard();
            mSelectedChoice = mChoicesSpinner.getSelectedItem().toString();
        }
    }

    private void updateGameBoard() {
        mTurnView.setText(String.format(Locale.ENGLISH,
                "Turn: %d", game.getCurrentPlayer().getTurn()));
        mPlayerView.setText(game.getCurrentPlayer().getPlayerName());
        mScoreView.setText(String.format(Locale.ENGLISH,
                "Score: %d", game.getCurrentPlayer().getTotalScore()));

        adapter.clear();
        adapter.addAll(game.getCurrentPlayer().getChoices());
    }


    private void updateRollButton() {
        mRollButton.setText(String.format(Locale.ENGLISH,
                "Rolls: %d", game.getCurrentPlayer().rollsRemaining()));
    }



}
