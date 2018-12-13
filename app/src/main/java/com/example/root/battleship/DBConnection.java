package com.example.root.battleship;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DBConnection {
    private static DBConnection instance = null;
    private DBConnectionListener listener;
    private User currentUser;
    private String playerInfo;
    private String currentGameKey;

    public interface DBConnectionListener{
        void userExists(boolean userExists);
        void getMap(String map);
        void gameStarted();
    }

    public void setDBConnectionListener(DBConnection.DBConnectionListener listener) {
        this.listener = listener;
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

    protected void getMapdata(){
        dbGamedataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String enemy;
                if(playerInfo.equals("playerOne")){
                    enemy = "playerTwo";
                }
                else{
                    enemy = "playerOne";
                }
                listener.getMap(dataSnapshot.child(currentGameKey).child(enemy + "Map").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void setGamedata(final String map) {
        dbGamedataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    if(shot.child("open").equals("true")){
                        playerInfo = "playerTwo";
                        currentGameKey = shot.getKey();
                        insertIntoGame(map);
                        listener.gameStarted();
                        return;
                    }
                }
                playerInfo = "playerOne";
                insertNewGame(map);
                listener.gameStarted();
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
        dbGamedataRef.child(currentGameKey).child("active").setValue(playerInfo);
    }

    protected void insertIntoGame(String map) {
        dbGamedataRef.child(currentGameKey).child("open").setValue("false");
        dbGamedataRef.child(currentGameKey).child("playerTwo").setValue(currentUser.getName());
        insertMapdata(map);
    }

    protected void signIn(final User user) {
            dbUserdataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    System.out.println("###############################################");
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
}