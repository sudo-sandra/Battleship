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
    protected DatabaseReference dbUserdataRef = dbRootRef.child("userdata");

    //Inserting User into Database - used in RegisterActivity
    protected void insertUserIntoDB(User user) {
        if(user != null) {
            dbUserdataRef.child(user.getName()).setValue(user);
        }
    }

    protected boolean signIn(final User user) {
            dbUserdataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(user.getName()).exists()) {
                        if (!user.getName().isEmpty()) {
                            User login = dataSnapshot.child(user.getName()).getValue(User.class);
                            if (login.getPassword().equals(user.getPassword())) {
                                userExists = true;

                            } else {
                                userExists = false;
                            }
                        } else {
                            userExists = false;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });
        return userExists;
    }

    protected boolean comparingUserUiWithUserDatabase(final User userUI) {
        dbUserdataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userUI.getName()).exists()) {
                    userExists = true;
                } else {
                    insertUserIntoDB(userUI);
                    userExists = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        return userExists;
    }
}