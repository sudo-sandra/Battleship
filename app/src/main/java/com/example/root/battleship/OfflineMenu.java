package com.example.root.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class OfflineMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_menu);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pc_button:
                openGame();
                break;
            case R.id.two_player_button:
                openOfflineLogin();
                break;
        }
    }

    public void openGame() {
        Intent gameIntent = new Intent(this, Game.class);
        gameIntent.putExtra("play_mode", Game.OFFLINE_ONE_PLAYER);
        startActivity(gameIntent);
    }

    public void openOfflineLogin() {
        // TODO offline login
        Intent registerIntent = new Intent(this, OfflineLogin.class);
        startActivity(registerIntent);
    }
}