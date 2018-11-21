package com.example.root.battleship;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class OfflineMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_menu);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pc_button:
                openSetUpMap();
                break;
            case R.id.two_player_button:
                openOfflineLogin();
                break;
        }
    }

    public void openSetUpMap() {
        Intent menuIntent = new Intent(this, SetUpMap.class);
        startActivity(menuIntent);
    }

    public void openOfflineLogin() {
        Intent registerIntent = new Intent(this, OfflineLogin.class);
        startActivity(registerIntent);
    }
}