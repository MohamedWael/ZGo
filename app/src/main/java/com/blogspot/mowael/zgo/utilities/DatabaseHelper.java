package com.blogspot.mowael.zgo.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.mowael.zgo.dataModel.MeasureData;

import java.util.ArrayList;

/**
 * Created by moham on 9/16/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static DatabaseHelper dbHelper;
    private static final String DATABASE_NAME = "mapsdb.db";
    private static final String DATABASE_TABLE = "directions";
    private static final String REQUEST_URL = "request_url";
    private static final String JSON_RESPONSE = "response_json";
    private static final String TRAVEL_DISTANCE = "travel_distance";
    private static final String DURATION = "duration";
    private static final String DURATION_IN_TRAFFIC = "duration_in_traffic";
    private static final String ORIGIN = "origin";
    private static final String DESTINATION = "destination";
    private static final String SUMMARY = "summary";
    private static final String ORIGIN_LATITUDE = "origin_latitude";
    private static final String ORIGIN_LONGITUDE = "origin_longitude";
    private static final String DESTINATION_LATITUDE = "destination_latitude";
    private static final String DESTINATION_LONGITUDE = "destination_longitude";
    private static final String KEY_ID = "id";
    private static final int DATABASE_VERSION = 1;
    private String[] coulumnNames = new String[]{KEY_ID, REQUEST_URL, JSON_RESPONSE,
            TRAVEL_DISTANCE, DURATION, DURATION_IN_TRAFFIC,
            ORIGIN, DESTINATION, SUMMARY, ORIGIN_LATITUDE, ORIGIN_LONGITUDE,
            DESTINATION_LATITUDE, DESTINATION_LONGITUDE};

    public DatabaseHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    public static DatabaseHelper newInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
            return dbHelper;
        } else return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + REQUEST_URL + " TEXT UNIQUE," + JSON_RESPONSE + " TEXT UNIQUE,"
                + TRAVEL_DISTANCE + " TEXT," + DURATION + " TEXT," + DURATION_IN_TRAFFIC + " TEXT,"
                + ORIGIN + " TEXT," + DESTINATION + " TEXT," + SUMMARY + " TEXT," + ORIGIN_LATITUDE + " DOUBLE,"
                + ORIGIN_LONGITUDE + " DOUBLE," + DESTINATION_LATITUDE + " DOUBLE," + DESTINATION_LONGITUDE + " DOUBLE" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
// Creating tables again
        onCreate(db);
    }

    public void addMeasureData(MeasureData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REQUEST_URL, data.getRequestUrl());
        values.put(JSON_RESPONSE, data.getResponseJsonStr());
        values.put(TRAVEL_DISTANCE, data.getTravelDistance());
        values.put(DURATION, data.getDuration());
        values.put(DURATION_IN_TRAFFIC, data.getDurationInTraffic());
        values.put(ORIGIN, data.getOrigin());
        values.put(DESTINATION, data.getDestination());
        values.put(SUMMARY, data.getSummary());
        values.put(ORIGIN_LATITUDE, data.getOriginLatitude());
        values.put(ORIGIN_LONGITUDE, data.getOriginLongitude());
        values.put(DESTINATION_LATITUDE, data.getDestinationLatitude());
        values.put(DESTINATION_LONGITUDE, data.getDestinationLongitude());
        // Inserting Row
        try {
            db.insert(DATABASE_TABLE, null, values);
        } catch (SQLiteConstraintException sqLiteConstraintException) {
            //this exception is issued from the frequent tries to redundant a unique peice of data "the ITEM_LINK"
        } catch (Exception e) {

        }
        db.close(); // Closing database connection
    }

    public ArrayList<MeasureData> getAllMeasureData() {
        ArrayList<MeasureData> measureDataArrayList = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

            /*
      this.requestUrl = requestUrl;                             0
      this.responseJsonStr = responseJsonStr;                   1
      this.travelDistance = travelDistance;                     2
      this.duration = duration;                                 3
      this.durationInTraffic = durationInTraffic;               4
      this.origin = origin;                                     5
      this.destination = destination;                           6
      this.summary = summary;                                   7
      this.originLatitude = originLatitude;                     8
      this.originLongitude = originLongitude;                   9
      this.destinationLatitude = destinationLatitude;           10
      this.destinationLongitude = destinationLongitude;         11
  * */

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MeasureData dataModel = new MeasureData(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getDouble(9), cursor.getDouble(10),
                        cursor.getDouble(11), cursor.getDouble(12));
                measureDataArrayList.add(dataModel);
            } while (cursor.moveToNext());
        }
        return measureDataArrayList;
    }


    // Getting contacts Count
    public int getHistoryCount() {
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    // Deleting single contact
    public void deleteNews(MeasureData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(data.getRequestUrl())});
        db.close();
    }
}
