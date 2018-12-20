package com.example.root.battleship;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Random;


public class Game extends AppCompatActivity{

    static final String OFFLINE_ONE_PLAYER = "offline_one_player";
    static final String OFFLINE_TWO_PLAYER = "offline_two_player";
    static final String ONLINE_TWO_PLAYER = "online_two_player";

    TextView turn_text;
    Battleship battle;
    Battleship enemyBattle;
    BattleshipView battleView;
    BattleshipView enemyView;
    String play_mode;
    String user_name = "You";
    String enemy_name = "Enemy";
    int active_player = 1;
    String playerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        Intent intent = getIntent();
        play_mode = intent.getStringExtra("play_mode");
        turn_text = (TextView)findViewById(R.id.turn_text);
        if(play_mode.equals(OFFLINE_TWO_PLAYER)){
            // TODO player_name
            turn_text.setText("player one");
            //TODO set name to know who
            getBattleFromSetUp();
        }
        else{
            if(play_mode.equals(OFFLINE_ONE_PLAYER)) {
                battle = new Battleship();
            }
            else if(play_mode.equals(ONLINE_TWO_PLAYER)){
                user_name = intent.getStringExtra("user_name");
            }
            // enemy is second player
            getBattleFromSetUp();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        DBConnection.getInstance().endGame();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        switch (requestCode){
            case 1:
                setUpResult(data);
                break;
            case 2:
                battleView.set_hit_colors(battle);
                enemyView.set_hit_colors(enemyBattle);
                break;
            case 3:
                getPlayerTwoBattle();
                break;
            case 4:
                enemyBattle = (Battleship) data.getSerializableExtra("battle");
                getPlayerOneConfirmation();
                break;
            case 5:
                battle = new Battleship();
                battle.set_mapString(data.getStringExtra("map"));
                set_up_map();
                break;
        }
    }

    private void setUpResult(Intent data){
        if(active_player == 1){
            enemyBattle = (Battleship) data.getSerializableExtra("battle");
            if(play_mode.equals(OFFLINE_TWO_PLAYER)){
                active_player = 2;
                getBattleFromSetUp();
                return;
            }
        }
        else if(active_player == 2){
            battle = (Battleship) data.getSerializableExtra("battle");
        }
        if(play_mode.equals(ONLINE_TWO_PLAYER)){
            DBConnection.getInstance().setGamedata(enemyBattle.get_mapString());
            DBConnection.getInstance().getNewMapdata();
            DBConnection.getInstance().setDBConnectionListener(new DBConnection.DBConnectionListener() {
                @Override
                public void userExists(boolean userExists) {}

                @Override
                public void getMap(String map) {
                    DBConnection.getInstance().getEnemyName();
                    DBConnection.getInstance().setDBConnectionListener(new DBConnection.DBConnectionListener() {
                        @Override
                        public void userExists(boolean userExists) {}

                        @Override
                        public void getMap(String map) {}

                        @Override
                        public void gameStarted(String playerInfo) {}

                        @Override
                        public void active() {}

                        @Override
                        public void getEnemyName(String name) {
                            enemy_name = name;
                        }

                        @Override
                        public void winnerResult() {}
                    });
                    battle = new Battleship();
                    battle.set_mapString(map);
                    set_up_map();
                }

                @Override
                public void gameStarted(String playerInfoStr) {
                    playerInfo = playerInfoStr;
                    waitForEnemy();
                }

                @Override
                public void active(){}

                @Override
                public  void winnerResult(){}

                @Override
                public void getEnemyName(String name){}
            });
            return;
        }
        set_up_map();
    }

    private void enemyTurn(){
        if(play_mode.equals(OFFLINE_ONE_PLAYER)){
            play_against_pc();
            battleView.enable_all();
        }
        else if(play_mode.equals(OFFLINE_TWO_PLAYER)){
            play_against_human();
            battleView.enable_all();
        }
        else if (play_mode.equals(ONLINE_TWO_PLAYER)){
            play_against_human_online();
        }

    }

    private void play_against_human(){
        // TODO: replace Player Names
        getPlayerTwoConfirmation();
    }

    private void play_against_human_online(){
        DBConnection.getInstance().setEnemyActive();
        DBConnection.getInstance().getNewMapdata();
        DBConnection.getInstance().getActiveInfo();
        DBConnection.getInstance().setDBConnectionListener(new DBConnection.DBConnectionListener() {
            @Override
            public void userExists(boolean userExists) {}

            @Override
            public void getMap(String map) {
                enemyBattle.set_mapString(map);
                enemyView.set_hit_colors(enemyBattle);
            }

            @Override
            public void gameStarted(String playerInfo) {}

            @Override
            public void active(){
                turn_text.setText("Your turn");
                battleView.enable_all();
            }

            @Override
            public  void winnerResult(){
                openTwoPlayerResult("loose");
            }

            @Override
            public void getEnemyName(String name){}
        });
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
                turn_text.setText("Your turn");
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
        battleView.setBattleshipViewListener(new BattleshipView.BattleshipViewListener(){
            @Override
            public void onFieldDestroyed(int destroyStatus) {
                if(play_mode.equals(ONLINE_TWO_PLAYER)){
                    DBConnection.getInstance().insertMapdata(battleView.getBattle().get_mapString());
                }
                // case 1 and 2 -> destroyed ship -> you're turn again
                switch (destroyStatus){
                    case 3:
                        battleView.disable_all();
                        turn_text.setText("You have won!");
                        enemyView.show_full_map();
                        if(play_mode.equals(ONLINE_TWO_PLAYER)){
                            DBConnection.getInstance().insertWindata();
                            openTwoPlayerResult("win");
                        }
                        else if (play_mode.equals(OFFLINE_TWO_PLAYER)){
                            openTwoPlayerResult("win");
                        }
                        break;
                    case 0:
                        battleView.disable_all();
                        if(play_mode.equals(OFFLINE_ONE_PLAYER)){
                            turn_text.setText("PC turn");
                        }
                        else if(play_mode.equals(ONLINE_TWO_PLAYER)){
                            turn_text.setText(enemy_name + " turn");
                        }
                        enemyTurn();
                        break;
                }
            }
        });
        if(play_mode.equals(OFFLINE_TWO_PLAYER)){
            getPlayerOneConfirmation();
        }
        else if(play_mode.equals(ONLINE_TWO_PLAYER)){
            System.out.println(playerInfo);
            if(playerInfo != null){
                //TODO test all
                if(playerInfo.equals("PlayerOne")){
                    battleView.disable_all();
                }
            }
        }
    }

    public void getBattleFromSetUp() {
        Intent setUpIntent = new Intent(this, SetUpMap.class);
        startActivityForResult(setUpIntent, 1);
    }

    public void getPlayerOneConfirmation(){
        Intent turnIntent = new Intent(this, TurnDisplay.class);
        // TODO: Player name
        turnIntent.putExtra("turn_text", "Player One");
        startActivityForResult(turnIntent, 2);
    }

    public void getPlayerTwoConfirmation(){
        Intent turnIntent = new Intent(this, TurnDisplay.class);
        // TODO: Player name
        turnIntent.putExtra("turn_text", "Player Two");
        startActivityForResult(turnIntent, 3);
    }

    public void getPlayerTwoBattle(){
        Intent player_two_intent = new Intent(this, PlayerTwoGame.class);
        // TODO: player name
        player_two_intent.putExtra("player_name", "Player Two");
        player_two_intent.putExtra("battle", enemyBattle);
        player_two_intent.putExtra("enemy_battle", battle);
        startActivityForResult(player_two_intent, 4);
    }

    public void openTwoPlayerResult(final String result){
        if(play_mode.equals(ONLINE_TWO_PLAYER)){
            DBConnection.getInstance().increaseUserResult(result);
            DBConnection.getInstance().setDBConnectionResultListener(new DBConnection.DBConnectionResultListener(){
                @Override
                public void resultInfo(int enemy_wins, int enemy_looses, int wins, int looses){
                    openResult(result, enemy_wins, enemy_looses, wins, looses);
                }
            });
        }
        else if(play_mode.equals(ONLINE_TWO_PLAYER)){
            openResult(result, 1, 1, 1, 1);
        }
    }

    public void openResult(String result, int enemy_wins, int enemy_looses, int wins, int looses){
        Intent resultIntent = new Intent(this, Result.class);
        resultIntent.putExtra("name", user_name);
        resultIntent.putExtra("enemy_name", enemy_name);
        if(result.equals("win")){
            resultIntent.putExtra("winner", user_name);
        }
        else if(result.equals("loose")){
            resultIntent.putExtra("winner", enemy_name);
        }
        resultIntent.putExtra("wins", wins);
        resultIntent.putExtra("looses", looses);
        resultIntent.putExtra("enemy_wins", enemy_wins);
        resultIntent.putExtra("enemy_looses", enemy_looses);
        startActivity(resultIntent);
    }

    public void waitForEnemy(){
        if(battle != null){
            return;
        }
        Intent waitingIntent = new Intent(this, Waiting.class);
        startActivityForResult(waitingIntent, 5);
    }
}
