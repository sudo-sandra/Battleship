package com.example.root.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                User user = readInputFields();
                DBConnection.getInstance().signIn(user);
                Toast.makeText(LoginActivity.this, "Loading...", Toast.LENGTH_LONG).show();
                DBConnection.getInstance().setDBConnectionListener(new DBConnection.DBConnectionListener() {
                    @Override
                    public void userExists(boolean userExists) {
                        if (userExists) {
                            Toast.makeText(LoginActivity.this, "Login was successful", Toast.LENGTH_SHORT).show();
                            openGameMenuActivity();
                        }
                        else {Toast.makeText(LoginActivity.this, "Please check your username and password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void gameStarted(String playerInfo){}

                    @Override
                    public void getMap(String map){}

                    @Override
                    public void active(){}

                    @Override
                    public  void winnerResult(){}

                    @Override
                    public void getEnemyName(String name){}
                });

                break;
            case R.id.registerBtn:
                openRegisterActivity();
                break;
        }
    }

    private void openGameMenuActivity() {
        Intent gameIntent = new Intent(this, Game.class);
        gameIntent.putExtra("play_mode", Game.ONLINE_TWO_PLAYER);
        EditText usernameTxt = findViewById(R.id.usernameLoginField);
        gameIntent.putExtra("user_name", usernameTxt.getText().toString());
        startActivity(gameIntent);
    }

    private void openRegisterActivity() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private User readInputFields() {
        EditText usernameTxt = findViewById(R.id.usernameLoginField);
        EditText passwordTxt = findViewById(R.id.passwordLoginField);
        User user = new User(usernameTxt.getText().toString(), passwordTxt.getText().toString());
        return user;
    }

}