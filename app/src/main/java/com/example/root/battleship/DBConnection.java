package com.example.root.battleship;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class DBConnection {
    private static DBConnection instance = null;
    private DBConnectionListener listener;
    private DBConnectionResultListener resultlistener;
    private User currentUser;
    private String playerInfo;
    private String currentGameKey;
    private String enemyMap = "";

    public interface DBConnectionListener{
        void userExists(boolean userExists);
        void getMap(String map);
        void gameStarted(String playerInfo);
        void active();
        void getEnemyName(String name);
        void winnerResult();
    }

    public void setDBConnectionListener(DBConnection.DBConnectionListener listener) {
        this.listener = listener;
    }

    public interface DBConnectionResultListener{
        void resultInfo(int enemy_wins, int enenmy_looses, int wins, int looses);
    }

    public void setDBConnectionResultListener(DBConnection.DBConnectionResultListener listener) {
        this.resultlistener = listener;
    }

    private DBConnection() {}

    public static synchronized DBConnection getInstance() {
        if(instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    //Reference on whole Database path="https://battleship-fs.firebaseio.com"
    private DatabaseReference dbRootRef = FirebaseDatabase.getInstance().getReference();
    //Reference to "Table" userdata path="https://battleship-fs.firebaseio.com/userdata"
    protected DatabaseReference dbUserdataRef = dbRootRef.child("userdata");
    protected DatabaseReference dbGamedataRef = dbRootRef.child("gamedata");

    //Inserting User into Database - used in RegisterActivity
    protected void insertUserIntoDB(User user) {
        if(user != null) {
            dbUserdataRef.child(user.getName()).setValue(user);
            currentUser = user;
        }
    }

    protected void insertMapdata(String map) {
        dbGamedataRef.child(currentGameKey).child(playerInfo + "Map").setValue(map);
    }

    protected void insertWindata(){
        dbGamedataRef.child(currentGameKey).child(playerInfo + "Map").setValue("win");
    }

    protected void getNewMapdata(){
        dbGamedataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String map =  dataSnapshot.child(currentGameKey).child(getEnemy() + "Map").getValue(String.class);
                if (map != null){
                    if(map.equals("win")){
                        listener.winnerResult();
                        return;
                    }
                    if(!enemyMap.equals(map)){
                        enemyMap = map;
                        listener.getMap(map);
                    }
                }
                getNewMapdata();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void getMapdata(){
        dbGamedataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String map =  dataSnapshot.child(currentGameKey).child(getEnemy() + "Map").getValue(String.class);
                if (map != null){
                    listener.getMap(map);
                }
                else{
                    getNewMapdata();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void getActiveInfo(){
        dbGamedataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String active = dataSnapshot.child(currentGameKey).child("active").getValue(String.class);
                if(active == null){
                    active = "";
                }
                if (active.equals(playerInfo)){
                    listener.active();
                }
                else{
                    getActiveInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void setEnemyActive(){
        dbGamedataRef.child(currentGameKey).child("active").setValue(getEnemy());
    }

    protected void setGamedata(final String map) {
        dbGamedataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    String status = shot.child("open").getValue(String.class);
                    if(status == null){
                        status = "";
                    }
                    if(status.equals("true")){
                        String enemy = shot.child("playerOne").getValue(String.class);
                        if(enemy == null){
                            enemy = "Enemy";
                        }
                        System.out.println(enemy);
                        System.out.println(currentUser.getName());
                        if(!enemy.equals(currentUser.getName())){
                            listener.getEnemyName(enemy);
                            playerInfo = "playerTwo";
                            currentGameKey = shot.getKey();
                            insertIntoGame(map);
                            return;
                        }
                    }
                }
                playerInfo = "playerOne";
                insertNewGame(map);
                listener.gameStarted(playerInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void insertNewGame(String map){
        currentGameKey = dbGamedataRef.push().getKey();
        dbGamedataRef.child(currentGameKey).child("open").setValue("true");
        dbGamedataRef.child(currentGameKey).child("playerOne").setValue(currentUser.getName());
        insertMapdata(map);
        dbGamedataRef.child(currentGameKey).child("active").setValue(getEnemy());
    }

    protected void insertIntoGame(String map) {
        dbGamedataRef.child(currentGameKey).child("open").setValue("false");
        dbGamedataRef.child(currentGameKey).child("playerTwo").setValue(currentUser.getName());
        insertMapdata(map);
    }

    protected void signIn(final User user) {
        currentUser = user;
        dbUserdataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user.getName()).exists()) {
                    if (!user.getName().isEmpty()) {
                        User login = dataSnapshot.child(user.getName()).getValue(User.class);
                        if (login.getPassword().equals(user.getPassword())) {
                            listener.userExists(true);

                        } else {
                            listener.userExists(false);
                        }
                    } else {
                        listener.userExists(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    protected void comparingUserUiWithUserDatabase(final User userUI) {
        dbUserdataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userUI.getName()).exists()) {
                    listener.userExists(true);
                } else {
                    insertUserIntoDB(userUI);
                    listener.userExists(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    protected void increaseUserResult(final String result){
        dbUserdataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer current = dataSnapshot.child(currentUser.getName()).child(result).getValue(Integer.class);
                if(current == null){
                    current = 0;
                }
                current++;
                dbUserdataRef.child(currentUser.getName()).child(result).setValue(current);
                Integer wins = dataSnapshot.child(currentUser.getName()).child("win").getValue(Integer.class);
                Integer looses = dataSnapshot.child(currentUser.getName()).child("loose").getValue(Integer.class);
                String enemy_name = dataSnapshot.child(currentGameKey).child(getEnemy()).getValue(String.class);
                if(enemy_name == null){
                    enemy_name = "";
                }
                Integer enemy_wins = dataSnapshot.child(enemy_name).child("win").getValue(Integer.class);
                Integer enemy_looses = dataSnapshot.child(enemy_name).child("loose").getValue(Integer.class);
                if(wins == null){
                    wins = 0;
                }
                if(looses == null){
                    looses = 0;
                }
                if(enemy_wins == null){
                    enemy_wins = 0;
                }
                if(enemy_looses == null){
                    enemy_looses = 0;
                }
                resultlistener.resultInfo(enemy_wins, enemy_looses, wins, looses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void endGame(){
        //dbGamedataRef.child(currentGameKey).removeValue();
    }

    protected  void getEnemyName(){
        dbGamedataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.getEnemyName(dataSnapshot.child(currentGameKey).child(getEnemy()).getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private String getEnemy(){
        if(playerInfo.equals("playerOne")){
            return "playerTwo";
        }
        else{
            return "playerOne";
        }
    }
}