package com.example.root.battleship;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Random;


public class Game extends AppCompatActivity implements View.OnClickListener{

    static final String OFFLINE_ONE_PLAYER = "offline_one_player";
    static final String OFFLINE_TWO_PLAYER = "offline_two_player";
    static final String ONLINE_TWO_PLAYER = "online_two_player";

    TextView turn_text;
    Battleship battle;
    Battleship enemyBattle;
    BattleshipView battleView;
    BattleshipView enemyView;
    String play_mode;
    int active_player = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        Intent intent = getIntent();
        play_mode = intent.getStringExtra("play_mode");
        if(play_mode.equals(OFFLINE_TWO_PLAYER)){
            //TODO set name to know who
            getBattleFromSetUp();
        }
        else{
            if(play_mode.equals(OFFLINE_ONE_PLAYER)) {
                battle = new Battleship();
            }
            // enemy is second player
            getBattleFromSetUp();
        }
        turn_text = (TextView)findViewById(R.id.turn_text);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                if(active_player == 1){
                    enemyBattle = (Battleship) data.getSerializableExtra("battle");
                    if(play_mode.equals(OFFLINE_TWO_PLAYER)){
                        active_player = 2;
                        getBattleFromSetUp();
                        return;
                    }
                }
                else if(active_player == 2){
                    Intent intent = getIntent();
                    battle = (Battleship) intent.getSerializableExtra("battle");
                }
                //TODO differ between modes
                //TODO Database stuff
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
                                enemyView.show_full_map();
                                break;
                            case 0:
                                battleView.disable_all();
                                if(play_mode.equals(OFFLINE_ONE_PLAYER)){
                                    turn_text.setText("PC turn");
                                }
                                enemyTurn();
                                break;
                        }
                    }
                });
                // play_against_pc();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                return;
            }
        }
    }

    private void enemyTurn(){
        if(play_mode.equals(OFFLINE_ONE_PLAYER)){
            play_against_pc();
        }
        turn_text.setText("Your turn");
        battleView.enable_all();

    }

    private void play_against_pc(){
        // TODO: improve so after hit next to hit
        Random random = new Random();
        Integer[][] map = enemyView.getBattle().get_map();
        int x_pos = random.nextInt(10);
        int y_pos = random.nextInt(10);
        while(map[y_pos][x_pos] > 1){
            x_pos = random.nextInt(10);
            y_pos = random.nextInt(10);
        }
        // TODO: wait 1 sec
        int destroyStatus = enemyView.destroyField(y_pos, x_pos);
        switch (destroyStatus){
            case 3:
                battleView.show_full_map();
                battleView.disable_all();
                turn_text.setText("You have lost!");
                break;
            case 0:
                break;
            default:
                play_against_pc();
                break;

        }
    }

    private void set_up_map(){
        battleView = new BattleshipView(this, battle, (TableLayout)
                findViewById(R.id.map_layout));
        enemyView = new BattleshipView(this, enemyBattle,
                (TableLayout) findViewById(R.id.own_map_layout), false);
    }

    @Override
    public void onClick(View v){
        System.out.println("HERE  " + v.getId());
        switch(v.getId()){
            case R.id.button_end:
                finish();
                break;
        }
    }

    public void getBattleFromSetUp() {
        Intent setUpIntent = new Intent(this, SetUpMap.class);
        startActivityForResult(setUpIntent, 1);
    }
}
