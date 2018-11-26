package com.example.root.battleship;

/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;*/

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBConnection {

    public DBConnection() {

    }

    public FirebaseDatabase database = FirebaseDatabase.getInstance();

    private void openFirebaseConnection () {
        DatabaseReference dbRef = database.getReference("Battleship-2018");
    }

    public void insertNewUser(String username, String password) {
        User user = new User(username, password);
        DatabaseReference usersRef = database.getReference("Battleship-2018").child("userdata");
        System.out.println("inserted");
        usersRef.setValue(user);
    }

    /*private Connection connection;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            System.err.println("Error loading the jdbc-driver");
            System.exit(1);
        }
    }

    public DBConnection() {
        openConnection();
        createDatabase();
        Thread shutDownHook = new Thread() {
            public void run() {
                if(connection == null) System.out.print("Connection to database already closed");
                try {
                    if(connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                }catch (SQLException e) {
                    System.err.print("Shutdown hook couldn't close database connection");
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(shutDownHook);
    }

    //Connection to Database
    public void openConnection() {
        String url = "jdbc:mysql://localhost/?rewriteBatchStatements=true";
        String user = "root";
        String passwd = "";

        try {
            connection = DriverManager.getConnection(url, user, passwd);
        } catch (SQLException e) {
            System.err.print("Couldn't create DBConnection");
            System.exit(1);
        }
    }

    //Creating Database if not exists
    private void createDatabase() {
        String dbName = "battleship";
        String createDB = "CREATE DATABASE IF NOT EXISTS `" + dbName + "`";
        String useDB = "USE `" + dbName + "`";
        String query = "SET SQL_MODE='NO_AUTO_VALUE_ON_ZERO'";

        try {
            connection.setAutoCommit(false);
            Statement stmt = connection.createStatement();
            stmt.addBatch(createDB);
            stmt.addBatch(useDB);
            stmt.addBatch(query);
            stmt.executeBatch();
            connection.commit();
            System.out.print("Connection worked");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Connection failed");
        }
    }

    public void insertUserData(String username, String password) {
        String insertData = "INSERT INTO userdata (username, password) VALUES (?,?);";

        try {
            PreparedStatement stmt = connection.prepareStatement(insertData);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeQuery();
            System.out.print("Insert successful");
        }catch (SQLException e) {
            throw new NullPointerException("No Data to insert");
        }
    }

    public User selectUserData (String name, String passwd) {
        String selectData = "SELECT username, password FROM userdata WHERE username = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(selectData);
            stmt.setString(1, name);
            ResultSet rsUserData = stmt.executeQuery();
            if(rsUserData.next()) {
                System.out.print("getting Data worked");
                return new User(rsUserData.getString(1), rsUserData.getString(2));
            }
        }catch (SQLException e) {
            throw new NullPointerException("User not found");
        }
        return null;
    }*/
}