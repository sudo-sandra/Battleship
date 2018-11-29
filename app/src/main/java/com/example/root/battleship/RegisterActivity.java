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

                //checking the username
                if(usernameTxtField.getText().toString().length() <= 3) {
                    System.out.println("The username can't be empty or smaller than 4 symbols!");
                }else if(usernameTxtField.getText().toString().equals("rnd")) { 
                    System.out.println("This username is already used!");
                }
                //checking the password
                else if(passwordTxtField.getText().toString().equals("") | passwordTxtField.getText().toString().length() <= 3){
                    System.out.println("The password can't be empty or smaller than 4 symbols!");
                }else if(repasswordTxtField.getText().toString().equals("")  | repasswordTxtField.getText().toString().length() <= 3) {
                    System.out.println("Please typ your password again!");
                }else if(!passwordTxtField.getText().toString().equals(repasswordTxtField.getText().toString())) {
                    System.out.println("The two passwords don't match!");
                }else if(passwordTxtField.getText().toString().equals(repasswordTxtField.getText().toString())) {
                    DBConnection connection = new DBConnection();
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

}