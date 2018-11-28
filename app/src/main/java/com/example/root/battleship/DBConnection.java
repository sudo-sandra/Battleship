package com.example.root.battleship;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DBConnection {

    public DatabaseReference dbRootRef = FirebaseDatabase.getInstance().getReference();
    public DatabaseReference dbUserdataRef = dbRootRef.child("userdata");


    public DBConnection() {
        openFirebaseConnection();
    }

    private void openFirebaseConnection () {
        System.out.println(dbRootRef);
    }

    public void insertNewUser(String username, String password) {
        User user = new User(username, password);
        DatabaseReference usersRef = dbRootRef.child("userdata");
        Map<String, Object> userInsert = new HashMap<>();
        userInsert.put("1", user);
        usersRef.updateChildren(userInsert);

        System.out.println("inserted");
    }
}