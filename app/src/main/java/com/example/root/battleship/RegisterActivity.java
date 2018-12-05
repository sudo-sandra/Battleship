package com.example.root.battleship;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerBtn:

                User user = readInputFields();
                //Checking Validity of Username and Password, if true insert User into Database and forward User to Game Menu
                if(checkingUsernameOnValidity(user)) {
                    DBConnection.getInstance().insertUserIntoDB(user);
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

    private boolean checkingUsernameOnValidity(User user) {
        boolean validUser;
        if (user.getName().isEmpty() | user.getName().equals("") | user.getName().length() <= 3) {
            openErrorMessage(getString(R.string.invalidUsernameLength));
            validUser = false;
        } else if (DBConnection.getInstance().comparingUserUiWithUserDatabase(user)) {
            openErrorMessage(getString(R.string.usernameAlreadyUsed));
            validUser = false;
        } else {
            validUser = true;
        }
        return validUser;
    }

    private boolean checkingPasswordOnValidity(String password, String repassword) {
        boolean validPassword = false;
        if (password.isEmpty() | password.equals("") | password.length() <= 3) {
            openErrorMessage(getString(R.string.invalidPasswordLength));
            validPassword = false;
        }else if(repassword.isEmpty() | repassword.equals("")  | repassword.length() <= 3) {
            openErrorMessage(getString(R.string.invalidPasswordLength));
            validPassword = false;
        }else if(!password.equals(repassword)) {
            openErrorMessage(getString(R.string.missMatchOfPassword));
            validPassword = false;
        }else if(password.equals(repassword)) {
            validPassword = true;
        }
        return validPassword;
    }

    private User readInputFields() {
        EditText usernameTxtField = findViewById(R.id.usernameRegisterField);
        EditText passwordTxtField = findViewById(R.id.passwordRegisterField);
        EditText repasswordTxtField = findViewById(R.id.repasswordRegisterField);

        String username = usernameTxtField.getText().toString();
        String password = passwordTxtField.getText().toString();
        String repassword = repasswordTxtField.getText().toString();
        User user = new User();

        if(checkingPasswordOnValidity(password, repassword)) {
            user = new User(username, password);
        } else {
            Toast.makeText(RegisterActivity.this, "Please check that your passwords are the same", Toast.LENGTH_SHORT).show();
        }
        return user;
    }
}