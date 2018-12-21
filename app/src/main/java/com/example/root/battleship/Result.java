package com.example.root.battleship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Result extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        Intent intent = getIntent();
        TextView win_txt = findViewById(R.id.win_txt);
        String winner = intent.getStringExtra("winner") + " has won!";
        win_txt.setText(winner);
        String name = intent.getStringExtra("name");
        String enemy_name = intent.getStringExtra("enemy_name");
        TextView player_one = findViewById(R.id.player_one);
        player_one.setText(name);
        String wins = String.valueOf(intent.getIntExtra("wins", 0));
        String loses = String.valueOf(intent.getIntExtra("loses", 0));
        String enemy_wins = String.valueOf(intent.getIntExtra("enemy_wins", 0));
        String enemy_loses = String.valueOf(intent.getIntExtra("enemy_loses", 0));
        TextView player_one_wins = findViewById(R.id.player_one_wins);
        TextView player_one_loses = findViewById(R.id.player_one_loses);
        TextView player_two = findViewById(R.id.player_two);
        player_two.setText(enemy_name);
        TextView player_two_wins = findViewById(R.id.player_two_wins);
        TextView player_two_loses = findViewById(R.id.player_two_loses);
        player_one_wins.setText(wins);
        player_one_loses.setText(loses);
        player_two_wins.setText(enemy_wins);
        player_two_loses.setText(enemy_loses);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.back_btn:
                openMenu();
                break;
        }
    }

    public void openMenu() {
        Intent menuIntent = new Intent(this, StartActivity.class);
        startActivity(menuIntent);
    }
}
