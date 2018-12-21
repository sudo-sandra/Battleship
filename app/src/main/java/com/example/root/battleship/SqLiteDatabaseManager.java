package com.example.root.battleship;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SqLiteDatabaseManager extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    //Database Version
    private static final int DATABASE_VERSION = 1;
    //Database Name
    private static final String DATABASE_NAME = "battleshipDB";
    //Table name: userdata
    private static final String TABLE_USERDATA = "userdata";
    //Table columns
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_WIN = "win";
    private static final String COLUMN_LOSE = "lose";
    //create table statement
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERDATA +
            "(" + COLUMN_NAME + " TEXT NOT NULL," +
            COLUMN_WIN + " INTEGER DEFAULT 0," +
            COLUMN_LOSE + " INTEGER DEFAULT 0);";

    private static final String LOG_TAG = SqLiteDatabaseManager.class.getSimpleName();
    private SQLiteDatabase database = this.getWritableDatabase();

    SqLiteDatabaseManager(Context activity) {
        super(activity, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE + " angelegt.");
            database.execSQL(SQL_CREATE_TABLE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + e.getMessage());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "SqLiteDatabaseManager.onUpgrade...");
        //Drop older table if exists
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDATA);

        //Create table again
        onCreate(db);
    }

    protected void insertUserIntoSQLite(String playerName){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, playerName.trim());
        if(!checkIfUserExists(playerName.trim())) {
            //Inserting Row
            database.insert(TABLE_USERDATA, null, values);
        }

    }

    private boolean checkIfUserExists(String username) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_USERDATA + " WHERE " + COLUMN_NAME + "=?;", new String[]{username.trim()});
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public ArrayList<User> readDataFromSQLite() {
        ArrayList<User> userdata = new ArrayList<>();

        String selectData = "SELECT * FROM " + TABLE_USERDATA;
        Cursor cursor = database.rawQuery(selectData, null);
        User user;
        //fetching data saving into arraylist
        if(cursor.moveToFirst()) {
            do {
                user = new User(cursor.getString(0), cursor.getInt(1), cursor.getInt(2));
                userdata.add(user);

             }while(cursor.moveToNext());
        }

        cursor.close();
        return userdata;
    }

    public void addWin(String playerName) {
        ContentValues values = new ContentValues();
        ArrayList<Integer> playerScore = readScoreOfPlayer(playerName);
        Integer win = playerScore.get(0)+1;
        values.put(COLUMN_WIN, win);
        System.out.println("W " + win);
        System.out.println(playerName);
        database.update(TABLE_USERDATA, values, COLUMN_NAME + "=?", new String[]{playerName});
        System.out.println("Inserted Win" + readScoreOfPlayer(playerName));
    }

    public void addLoose(String playerName) {
        ContentValues values = new ContentValues();
        ArrayList<Integer> playerScore = readScoreOfPlayer(playerName);
        Integer loose = playerScore.get(1)+1;
        values.put(COLUMN_LOSE, loose);
        System.out.println("L " + loose);
        System.out.println(playerName);
        database.update(TABLE_USERDATA, values, COLUMN_NAME + "=?", new String[]{playerName});
        System.out.println("Inserted Loose" + readScoreOfPlayer(playerName));
    }


    public ArrayList<Integer> readScoreOfPlayer(String playerName) {
        ArrayList<Integer> playerScore = new ArrayList<>();

        Integer wins;
        Integer looses;
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_WIN + ", " + COLUMN_LOSE + " FROM " + TABLE_USERDATA + " WHERE " + COLUMN_NAME + " =?;", new String[]{playerName.trim()});
        System.out.println("Size of cursor " + cursor.getCount());
        if(cursor.moveToNext()) {
            wins = cursor.getInt(0);
            System.out.println("getScore Wins: " + wins);
            playerScore.add(wins);
            looses = cursor.getInt(1);
            System.out.println("getScore Looses: " + looses);
            playerScore.add(looses);
        }

        cursor.close();
        return playerScore;
    }
}