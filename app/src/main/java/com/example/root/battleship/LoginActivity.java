package com.example.root.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                EditText usernameTxt = findViewById(R.id.usernameLoginField);
                EditText passwordTxt = findViewById(R.id.passwordLoginField);
                DBConnection connection = new DBConnection();
                connection.selectUserData(usernameTxt.toString(), passwordTxt.toString());
                openGameMenuActivity();
                break;
            case R.id.registerBtn:
                openRegisterActivity();
                break;
        }
    }

    public void openGameMenuActivity() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
    }

    public void openRegisterActivity() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}