package com.example.root.battleship;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SqLiteDatabseManager extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    //Database Version
    private static final int DATABASE_VERSION = 1;
    //Database Name
    private static final String DATABASE_NAME = "battleshipDB";
    //Table name: userdata
    private static final String TABLE_USERDATA = "userdata";
    //Table columns
    private static final String COLUMN_PLAYER_ONE = "playerOne";
    private static final String COLUMN_PLAYER_ONE_WINS = "playerOneWins";
    private static final String COLUMN_PLAYER_ONE_LOSES = "playerOneLoses";
    private static final String COLUMN_PLAYER_TWO = "playerTwo";
    private static final String COLUMN_PLAYER_TWO_WINS = "playerTwoWins";
    private static final String COLUMN_PLAYER_TWO_LOSES = "playerTwoLoses";
    //create table statement
    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_USERDATA +
            "(" + COLUMN_PLAYER_ONE + " TEXT NOT NULL," +
            COLUMN_PLAYER_ONE_WINS + " INTEGER DEFAULT 0," +
            COLUMN_PLAYER_ONE_LOSES + " INTEGER DEFAULT 0," +
            COLUMN_PLAYER_TWO + " TEXT NOT NULL," +
            COLUMN_PLAYER_TWO_WINS + " INTEGER DEFAULT 0," +
            COLUMN_PLAYER_TWO_LOSES + " INTEGER DEFAULT 0);";

    private static final String LOG_TAG = SqLiteDatabseManager.class.getSimpleName();
    private SQLiteDatabase database = this.getWritableDatabase();

    private String playerOneName;
    private int playerOneWins;
    private int playerOneLoses;
    private String playerTwoName;
    private int playerTwoWins;
    private int playerTwoLoses;

    SqLiteDatabseManager(Context activity) {
        super(activity, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "SqLiteDatabaseManager.onUpgrade...");
        //Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDATA);

        //Create table again
        onCreate(db);
    }

    public void insertDataIntoSQLite(String playerOneName, String playerTwoName){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_ONE, playerOneName);
        values.put(COLUMN_PLAYER_TWO, playerTwoName);

        //Inserting Row
        database.insert(TABLE_USERDATA, null, values);

        System.out.println("Inserted");

    }

    public ArrayList<String> readDataFromSQLite() {
        ArrayList<String> userdata = new ArrayList<>();

        String selectData = "SELECT * FROM " + TABLE_USERDATA;
        Cursor cursor = database.rawQuery(selectData, null);

        //fetching data saving into arraylist
        if(cursor.moveToFirst()) {
            userdata.add(cursor.getString(0));
            userdata.add(cursor.getString(1));
            userdata.add(cursor.getString(2));
            userdata.add(cursor.getString(3));
        }
        
        cursor.close();
        return userdata;
    }

    public void updateScore(String winner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (winner.equals("playerOne")) {
            values.put(COLUMN_PLAYER_ONE_WINS, playerOneWins + 1);
            values.put(COLUMN_PLAYER_TWO_LOSES, playerTwoLoses + 1);
            db.update(TABLE_USERDATA, values,COLUMN_PLAYER_ONE + " = ?", new String[]{String.valueOf(playerOneName)});
        } else {
            values.put(COLUMN_PLAYER_TWO_WINS, playerTwoWins + 1);
            values.put(COLUMN_PLAYER_ONE_LOSES, playerOneLoses + 1);
            db.update(TABLE_USERDATA, values,COLUMN_PLAYER_TWO + " = ?", new String[]{String.valueOf(playerTwoName)});
        }

    }
}