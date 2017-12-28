package com.mapgenerator.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;

import com.mapgenerator.handlers.entities.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aviv on 06-Dec-17.
 */

public class DatabaseHandler {
    private SQLiteDatabase db;
    public DatabaseHandler(AppCompatActivity parentActivity, String databaseType){
        SQLiteOpenHelper mDbHelper = new DbHelper(parentActivity, databaseType);
        // Gets the data repository in write mode
        db = mDbHelper.getWritableDatabase();
    }

    public void addCoordinates(int latitude, int longitude)
    {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Entries.COLUMN_NAME_LATITUDE, latitude);
        values.put(Entries.COLUMN_NAME_LONGITUDE, longitude);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(Entries.TABLE_NAME, null, values);
    }

    public List<Coordinate> getNearCoordinates(int latitudeMin, int latitudeMax, int longitudeMin,
                                   int longitudeMax)
    {
        String[] selectionArgs = {
                String.valueOf(latitudeMin),
                String.valueOf(latitudeMax),
                String.valueOf(longitudeMin),
                String.valueOf(longitudeMax) };

        Cursor cursor = db.query(
                Entries.TABLE_NAME,                         // The table to query
                projection,                                 // The columns to return
                selection,                                  // The columns for the WHERE clause
                selectionArgs,                              // The values for the WHERE clause
                null,                               // don't group the rows
                null,                                // don't filter by row groups
                null                                // don't sort the order
        );

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        while(cursor.moveToNext()) {
            coordinates.add(
                    new Coordinate(
//                            cursor.getInt(cursor.getColumnIndexOrThrow(Entries._ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(Entries.COLUMN_NAME_LATITUDE)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(Entries.COLUMN_NAME_LONGITUDE))));
        }
        cursor.close();

        return coordinates;
    }

    public void stop()
    {
        db.close();
    }

    /* Inner class that defines the table contents */
    private static class Entries implements BaseColumns {
        public static final String TABLE_NAME = "coordinates";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        //public static final String COLUMN_NAME_TIME = "time";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Entries.TABLE_NAME + " (" +
                    Entries._ID + " INTEGER PRIMARY KEY," +
                    Entries.COLUMN_NAME_LATITUDE + " INTEGER," +
                    Entries.COLUMN_NAME_LONGITUDE + " INTEGER)";
                    //Entries.COLUMN_NAME_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Entries.TABLE_NAME;

    public class DbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME_PREFIX = "map_generator.";
        public static final String DATABASE_NAME_SUFFIX = ".db";

        public DbHelper(Context context, String databaseType) {
            super(context, DATABASE_NAME_PREFIX + databaseType + DATABASE_NAME_SUFFIX,
                    null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private final String[] projection = {
            Entries._ID,
            Entries.COLUMN_NAME_LATITUDE,
            Entries.COLUMN_NAME_LONGITUDE
    };

    private final String selection = Entries.COLUMN_NAME_LATITUDE + " >= ? AND " +
            Entries.COLUMN_NAME_LATITUDE + " <= ? AND " +
            Entries.COLUMN_NAME_LONGITUDE + " >= ? AND " +
            Entries.COLUMN_NAME_LONGITUDE + " <= ?";
}
