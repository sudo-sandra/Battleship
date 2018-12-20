package com.example.root.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    static boolean validUser, validPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerBtn:

                User user = readInputFields();
                if(!validUser) {
                    break;
                } else {
                    DBConnection.getInstance().comparingUserUiWithUserDatabase(user);
                    Toast.makeText(RegisterActivity.this, "Loading...", Toast.LENGTH_LONG).show();
                    DBConnection.getInstance().setDBConnectionListener(new DBConnection.DBConnectionListener() {
                        @Override
                        public void userExists(boolean userExists) {
                            if (userExists) {
                                Toast.makeText(RegisterActivity.this, "Please change your username", Toast.LENGTH_LONG).show();
                            } else {
                                openLoginActivity();
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
                }
                break;
        }
    }

    private void openLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void openErrorMessage(String msg) {
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private boolean checkingUsernameOnValidity(User user) {
        if (user.getName().isEmpty() | user.getName().equals("") | user.getName().length() <= 3) {
            openErrorMessage(getString(R.string.invalidUsernameLength));
            validUser = false;
        } else {
            validUser = true;
        }
        return validUser;
    }

    private boolean checkingPasswordOnValidity(String password, String repassword) {
        if (password.isEmpty() | password.length() <= 3) {
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
        User user = new User(username, password);

        checkingUsernameOnValidity(user);
        checkingPasswordOnValidity(password, repassword);
        return user;
    }
}