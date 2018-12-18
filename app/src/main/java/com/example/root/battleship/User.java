package com.example.root.battleship;

public class User {
    private String name;
    private String password;
    private int wins;
    private int looses;

    public User (){
        this.name = "";
        this. password = "";
    }

    public User (String username, String userpassword) {
        this.name = username;
        this.password = userpassword;
    }

    public User (String username, String userpassword, int wins, int looses) {
        this.name = username;
        this.password = userpassword;
        this.wins = wins;
        this.looses = looses;
    }

    public String getName(){
        return name;
    }

    public String getPassword() {
        return password;
    }
}
