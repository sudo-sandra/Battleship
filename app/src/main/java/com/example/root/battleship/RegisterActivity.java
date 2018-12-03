package com.example.root.battleship;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


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

                DBConnection connection = new DBConnection();
                boolean userExists = connection.checkUsername(usernameTxtField.getText().toString());

                //checking the username
                if(usernameTxtField.getText().toString().isEmpty() | usernameTxtField.getText().toString().length() <= 3) {
                    openErrorMessage(getString(R.string.invalidUsernameLength));
                }else if(userExists == true) {
                    openErrorMessage(getString(R.string.usernameAlreadyUsed));
                }
                //checking the password
                else if(passwordTxtField.getText().toString().isEmpty() | passwordTxtField.getText().toString().equals("") | passwordTxtField.getText().toString().length() <= 3){
                    openErrorMessage(getString(R.string.invalidPasswordLength));
                }else if(repasswordTxtField.getText().toString().equals("")  | repasswordTxtField.getText().toString().length() <= 3) {
                    openErrorMessage(getString(R.string.invalidPasswordLength));
                }else if(!passwordTxtField.getText().toString().equals(repasswordTxtField.getText().toString())) {
                    openErrorMessage(getString(R.string.missMatchOfPassword));
                }else if(passwordTxtField.getText().toString().equals(repasswordTxtField.getText().toString())) {
                    connection.insertUserIntoDB(usernameTxtField.getText().toString(), passwordTxtField.getText().toString());
                    openGameMenuActivity();
                }
                break;
        }
    }

    private void openGameMenuActivity() {
        Intent gameMenuIntent = new Intent(this, MenuActivity.class);
        startActivity(gameMenuIntent);
    }

    private void openErrorMessage(String msg) {
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
    }

}