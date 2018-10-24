package com.example.root.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.offlineGameBtn:
                openGameMenuActivity();
                break;
            case R.id.onlineGameBtn:
                openLoginActivity();
                break;
        }
    }

    public void openGameMenuActivity() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
    }

    public void openLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }
}
