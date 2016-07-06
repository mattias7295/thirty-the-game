package se.umu.cs.c12msr.thirtythegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * This class handles the lifecycle and other events when
 * displaying the menu for the user.
 *
 * @author Mattias Scherer
 * @version 1.0
 */
public class MenuActivity extends AppCompatActivity {

    /**
     * Key used to retrieve the number of players
     * from an intent.
     */
    public final static String NUM_PLAYERS = "se.umu.cs.c12msr.thirtythegame.NUMPLAYERS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    /**
     * Get the number of players from the
     * view that was pressed.
     * @param view the view that was pressed
     */
    public void playerButtonPressed(View view) {
        int numPlayers;
        switch (view.getId()) {
            case R.id.player_1_button: numPlayers = 1; break;
            case R.id.player_2_button: numPlayers = 2; break;
            case R.id.player_3_button: numPlayers = 3; break;
            case R.id.player_4_button: numPlayers = 4; break;
            default: numPlayers = 1; break;
        }
        showGameBoardActivity(numPlayers);
    }

    private void showGameBoardActivity(int numPlayers) {
        Intent intent = new Intent(this, GameBoardActivity.class);
        intent.putExtra(NUM_PLAYERS, numPlayers);
        startActivity(intent);
    }
}
