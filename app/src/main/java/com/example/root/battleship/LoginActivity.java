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

    //Beispiel Login : Dodo - 1234 -> vlt zweimal Login Btn drücken
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                User user = readInputFields();
                boolean userExists = DBConnection.getInstance().selectUserFromDB(user);

                if (userExists) {
                    openGameMenuActivity();
                }else {
                    String msg = getString(R.string.loginFailed);
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.registerBtn:
                openRegisterActivity();
                break;
        }
    }

    private void openGameMenuActivity() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
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