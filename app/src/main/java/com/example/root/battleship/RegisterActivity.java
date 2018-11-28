package com.example.root.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerBtn:

                EditText usernameTxtField = (EditText) findViewById(R.id.usernameRegisterField);
                EditText passwordTxtField = (EditText) findViewById(R.id.passwordRegisterField);
                EditText repasswordTxtField = (EditText) findViewById(R.id.repasswordRegisterField);

                if (passwordTxtField.getText().toString().equals(repasswordTxtField.getText().toString())) {
                    DBConnection connection = new DBConnection();
                    connection.insertNewUser(usernameTxtField.getText().toString(), passwordTxtField.getText().toString());
                    openGameMenuActivity();
                }
                break;
        }
    }

    private void openGameMenuActivity() {
        Intent gameMenuIntent = new Intent(this, MenuActivity.class);
        startActivity(gameMenuIntent);
    }
}