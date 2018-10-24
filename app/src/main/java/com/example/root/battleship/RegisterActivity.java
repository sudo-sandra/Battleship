package com.example.root.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerBtn:
                openGameMenuActivity();
                break;
        }
    }

    public void openGameMenuActivity() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
    }
}
