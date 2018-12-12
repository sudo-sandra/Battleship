package com.example.root.battleship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

public class PlayerTwoGame extends AppCompatActivity{

    TextView turn_text;
    Battleship battle;
    Battleship enemyBattle;
    BattleshipView battleView;
    BattleshipView enemyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_two_game);

        Intent intent = getIntent();
        turn_text = (TextView)findViewById(R.id.turn_text);
        turn_text.setText(intent.getStringExtra("player_name"));
        battle = (Battleship) intent.getSerializableExtra("battle");
        enemyBattle = (Battleship) intent.getSerializableExtra("enemy_battle");
        set_up_map();
        battleView.setBattleshipViewListener(new BattleshipView.BattleshipViewListener(){
            @Override
            public void onFieldDestroyed(int destroyStatus) {
                // TODO: write new map to database
                // case 1 and 2 -> destroyed ship -> you're turn again
                switch (destroyStatus){
                    case 3:
                        battleView.disable_all();
                        turn_text.setText("You have won!");
                        openTwoPlayerResult();
                        enemyView.show_full_map();
                        break;
                    case 0:
                        returnBattleToGame();
                        break;
                }
            }
        });
        enemyView.set_hit_colors(enemyBattle);
        battleView.set_hit_colors(battle);
    }

    private void set_up_map(){
        battleView = new BattleshipView(this, battle, (TableLayout)
                findViewById(R.id.map_layout));
        enemyView = new BattleshipView(this, enemyBattle,
                (TableLayout) findViewById(R.id.own_map_layout), false);
    }

    private void returnBattleToGame() {
        Intent gameIntent = new Intent();
        gameIntent.putExtra("battle", battle);
        setResult(Activity.RESULT_OK, gameIntent);
        finish();
    }

    public void openTwoPlayerResult(){
        Intent resultIntent = new Intent(this, Result.class);
        // TODO: player name
        resultIntent.putExtra("winner", "Player Two");
        startActivity(resultIntent);
    }
}
