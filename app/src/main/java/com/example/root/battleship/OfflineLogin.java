package com.example.root.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class OfflineLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_login);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_button:
                // TODO: sqlite database zeugs!!
                openGame();
                break;
        }
    }

    public void openGame() {
        Intent gameIntent = new Intent(this, Game.class);
        gameIntent.putExtra("play_mode", Game.OFFLINE_TWO_PLAYER);
        startActivity(gameIntent);
    }
}