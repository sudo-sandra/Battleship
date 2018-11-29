package com.example.root.battleship;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DBConnection {

    //Reference on whole Database path="https://battleship-fs.firebaseio.com"
    private DatabaseReference dbRootRef = FirebaseDatabase.getInstance().getReference();
    //Reference to "Table" userdata path="https://battleship-fs.firebaseio.com/userdata"
    private DatabaseReference dbUserdataRef = dbRootRef.child("userdata");

    public void insertUserIntoDB(String username, String password) {
        User user = new User(username, password);
        dbUserdataRef.push().setValue(user);
    }

    public void selectUserFromDB(User user) {

        dbUserdataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    System.out.println(user.getName()+ " Leer " + user.getPassword());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        /*Query query = dbUserdataRef.orderByChild("name").equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<User> userList = new ArrayList<>();
                for(DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                    //User user = new User(singleSnap.getValue(User.class));
                    //System.out.print(user.getName() + " | " + user.getPassword() + "\n");
                    //userList.add(user);
                    System.out.println(singleSnap.child("password").getValue());
                    System.out.println(singleSnap.child("name").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });*/

    }
}