package com.example.root.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OfflineLogin extends AppCompatActivity {
    private String playerOneName;
    private String playerTwoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_login);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_button:
                EditText playerOneNameField = findViewById(R.id.playerOneLoginField);
                EditText playerTwoNameField = findViewById(R.id.playerTwoLoginField);

                playerOneName = playerOneNameField.getText().toString();
                playerTwoName = playerTwoNameField.getText().toString();
                if(!playerOneName.isEmpty() && !playerTwoName.isEmpty()) {
                    SqLiteDatabaseManager dbManager = new SqLiteDatabaseManager(this);
                    dbManager.insertUserIntoSQLite(playerOneNameField.getText().toString());
                    dbManager.insertUserIntoSQLite(playerTwoNameField.getText().toString());

                    openGame();
                } else {
                    Toast.makeText(OfflineLogin.this, "Please set usernames!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void openGame() {
        Intent gameIntent = new Intent(this, Game.class);
        gameIntent.putExtra("play_mode", Game.OFFLINE_TWO_PLAYER);
        gameIntent.putExtra("player_one", playerOneName);
        gameIntent.putExtra("player_two", playerTwoName);
        startActivity(gameIntent);
    }
}