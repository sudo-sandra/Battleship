package com.example.root.battleship;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DBConnection {
    private static DBConnection instance = null;
    static boolean userExists = false;

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
    private DatabaseReference dbUserdataRef = dbRootRef.child("userdata");

    //Inserting User into Database - used in RegisterActivity
    protected void insertUserIntoDB(String username, String password) {
        User user = new User(username, password);
        dbUserdataRef.push().setValue(user);
    }

    //Making sure that user exists in Database - used in LoginActivity
    protected boolean selectUserFromDB(final User userUI) {
        dbUserdataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userDb = snapshot.getValue(User.class);
                    userList.add(userDb);
                    for (User user : userList) {
                        if (user.getName().equals(userUI.getName()) && user.getPassword().equals(userUI.getPassword())){
                            userExists = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return userExists;
    }

    //Making sure that username can't be used twice - used in RegisterActivity
    public boolean checkUsername(final String usernameUI) {
        dbUserdataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userDb = snapshot.getValue(User.class);
                    userList.add(userDb);
                    if (userDb != null) {
                        userExists = userDb.getName().equals(usernameUI);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return userExists;
    }
}