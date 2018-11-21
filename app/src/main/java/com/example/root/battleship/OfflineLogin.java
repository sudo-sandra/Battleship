package com.example.root.battleship;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class OfflineLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_login);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_button:
                // TODO: sqlite database zeugs!!
                openSetUpMap();
                break;
        }
    }

    public void openSetUpMap() {
        Intent menuIntent = new Intent(this, SetUpMap.class);
        startActivity(menuIntent);
    }
}