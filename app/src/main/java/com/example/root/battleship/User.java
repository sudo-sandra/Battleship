package com.example.root.battleship;

public class User {
    private String name;
    private String password;
    private int wins;
    private int loses;

    public User (){
        this.name = "";
        this. password = "";
    }

    public User (String username, String userpassword) {
        this.name = username;
        this.password = userpassword;
    }

    public User (String username, String userpassword, int wins, int loses) {
        this.name = username;
        this.password = userpassword;
        this.wins = wins;
        this.loses = loses;
    }

    public User (String username, int wins, int loses) {
        this.name = username;
        this.wins = wins;
        this.loses = loses;
    }

    public String getName(){
        return name;
    }

    public String getPassword() {
        return password;
    }
}
