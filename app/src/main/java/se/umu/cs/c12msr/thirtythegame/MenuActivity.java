package se.umu.cs.c12msr.thirtythegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    public final static String NUM_PLAYERS = "se.umu.cs.c12msr.thirtythegame.NUMPLAYERS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

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
