package com.example.root.battleship;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private Connection connection;

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
        String dbName = "userdata";
        String createDB = "CREATE DATABASE IF NOT EXISTS `" + dbName + "`";
        String useDB = "USE `" + dbName + "`";
        String query = "SET SQL_MODE='NO_AUTO_VALUE_ON_ZERO'";

        Statement stmt = null;

        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
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

    public User selectUserData (String name) {
        String selectData = "SELECT username, password FROM userdata WHERE username = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(selectData);
            stmt.setString(1, name);
            ResultSet rsUserData = stmt.executeQuery();
            if(rsUserData.next() == true) {
                System.out.print("getting Data worked");
                User user = new User(rsUserData.getString(1), rsUserData.getString(2));
                return user;
            }
        }catch (SQLException e) {
            throw new NullPointerException("User not found");
        }
        return null;
    }
}
