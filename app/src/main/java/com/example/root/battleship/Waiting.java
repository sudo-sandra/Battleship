package com.example.root.battleship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Waiting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);
        DBConnection.getInstance().getMapdata();
        DBConnection.getInstance().setDBConnectionListener(new DBConnection.DBConnectionListener() {
            @Override
            public void userExists(boolean userExists) {}

            @Override
            public void getMap(String map) {
                System.out.println("getMap");
                Intent gameIntent = new Intent();
                gameIntent.putExtra("map", map);
                setResult(Activity.RESULT_OK, gameIntent);
                finish();
            }

            @Override
            public void gameStarted() {
                System.out.println("gameStarted");
            }

            @Override
            public void active(){
                System.out.println("HAHAH active");
            }
        });
    }
}
