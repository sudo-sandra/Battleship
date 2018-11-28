package com.example.root.battleship;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DBConnection {

    public DatabaseReference dbRootRef = FirebaseDatabase.getInstance().getReference();
    public DatabaseReference dbUserdataRef = dbRootRef.child("userdata");

    public void insertNewUser(String username, String password) {
        User user = new User(username, password);
        dbUserdataRef.push().setValue(user);
    }

    public void selectUser(User user) {
        String userpwd = user.getPassword();
        String username = user.getName();

        Query query = dbUserdataRef.orderByChild("name").equalTo(username);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}