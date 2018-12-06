package com.example.root.battleship;

public class User {
    private String name;
    private String password;

    public User (){
        this.name = "";
        this. password = "";
    }

    public User (String username, String userpassword) {
        this.name = username;
        this.password = userpassword;
    }

    public String getName(){
        return name;
    }

    public String getPassword() {
        return password;
    }
}
