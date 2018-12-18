package com.example.root.battleship;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
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
    private static final String COLUMN_PLAYER_ONE_SCORE = "playerOneScore";
    private static final String COLUMN_PLAYER_TWO = "playerTwo";
    private static final String COLUMN_PLAYER_TWO_SCORE = "playerTwoScore";

    private String playerOneName;
    private int playerOneScore;
    private String playerTwoName;
    private int playerTwoScore;

    SqLiteDatabseManager(Context activity) {
        super(activity, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "SqLiteDatabaseManager.onCreate...");
        String statement = "CREATE TABLE " + TABLE_USERDATA + "(" + COLUMN_PLAYER_ONE + " TEXT," + COLUMN_PLAYER_ONE_SCORE + " INTEGER,"
        + COLUMN_PLAYER_TWO + " TEXT," + COLUMN_PLAYER_TWO_SCORE + " INTEGER)";
        //Execute statement
        db.execSQL(statement);
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
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_ONE, playerOneName);
        values.put(COLUMN_PLAYER_TWO, playerTwoName);

        //Inserting Row
        db.insert(TABLE_USERDATA, null, values);

        //Closing database connection
        db.close();
    }

    public ArrayList<String> readDataFromSQLite(String username) {
        ArrayList<String> userdata = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectData = "SELECT * FROM " + TABLE_USERDATA;
        Cursor cursor = db.rawQuery(selectData, null);

        //fetching data saving into arraylist
        if(cursor.moveToFirst()) {
            userdata.add(cursor.getString(0));
            userdata.add(cursor.getString(1));
            userdata.add(cursor.getString(2));
            userdata.add(cursor.getString(3));
        }
        return userdata;
    }

    public void updateScore(String winner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (winner.equals("player_one")) {
            values.put(COLUMN_PLAYER_ONE, playerOneScore + 1);
            db.update(TABLE_USERDATA, values,COLUMN_PLAYER_ONE + " = ?", new String[]{String.valueOf(playerOneName)});
        } else {
            values.put(COLUMN_PLAYER_TWO, playerTwoScore + 1);
            db.update(TABLE_USERDATA, values,COLUMN_PLAYER_TWO + " = ?", new String[]{String.valueOf(playerTwoName)});
        }

        db.close();

    }
}
