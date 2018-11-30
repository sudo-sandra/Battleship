package com.example.root.battleship;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DBConnection {
    static boolean userStatus;

    //Reference on whole Database path="https://battleship-fs.firebaseio.com"
    private DatabaseReference dbRootRef = FirebaseDatabase.getInstance().getReference();
    //Reference to "Table" userdata path="https://battleship-fs.firebaseio.com/userdata"
    private DatabaseReference dbUserdataRef = dbRootRef.child("userdata");

    public void insertUserIntoDB(String username, String password) {
        User user = new User(username, password);
        dbUserdataRef.push().setValue(user);
    }

    public boolean selectUserFromDB(final User userUI) {
        dbUserdataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userDb = snapshot.getValue(User.class);
                    userList.add(userDb);
                    if(userDb.getName().equals(userUI.getName()) & userDb.getPassword().equals(userUI.getPassword())) {
                        userStatus = true;
                    } else {
                        userStatus = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return userStatus;
    }
}