package com.example.root.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;


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
                EditText playerOneNameField = findViewById(R.id.playerOneLoginField);
                EditText playerTwoNameField = findViewById(R.id.playerTwoLoginField);

                SqLiteDatabseManager dbManager = new SqLiteDatabseManager(this);
                System.out.println("Open SQLite Connection");
                dbManager.insertDataIntoSQLite(playerOneNameField.getText().toString(), playerTwoNameField.getText().toString());

                ArrayList<String> dbData = dbManager.readDataFromSQLite();
                for (int i = 0; i <= dbData.size(); i++) {
                    System.out.println(dbData.get(i));
                }

                openGame();
                break;
        }
    }

    private void openGame() {
        Intent gameIntent = new Intent(this, Game.class);
        gameIntent.putExtra("play_mode", Game.OFFLINE_TWO_PLAYER);
        startActivity(gameIntent);
    }
}