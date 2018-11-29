package com.example.root.battleship;

public class User {
    private String key;
    private String name;
    private String password;

    public User (String username, String userpassword) {
        this.name = username;
        this.password = userpassword;
    }

    public User(User user) {
        this.name = user.getName();
        this.password = user.getPassword();
        this.key = user.getKey();
    }

    public String getName(){
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getKey() {
        return key;
    }
}
