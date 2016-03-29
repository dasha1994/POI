package com.example.s4astya.poi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by S4ASTYA on 29.03.2016.
 */
public class Dao {

    DBHelper dbHelper;
    SQLiteDatabase db;

    Dao(Context context)
    {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public boolean insert(String name,String description,String latitude,String longitude)
    {
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        cv.put("description",description);
        cv.put("latitude",latitude);
        cv.put("longitude",longitude);
        db.insert("rtable", null, cv);
        boolean result = false;
        Cursor c = db.query("rtable", null, null, null, null, null, null);
        if (c.moveToLast()) {
            int idColIndex = c.getColumnIndex("name");
            String name2 = c.getString(idColIndex);
            if (name2.equals(name))
                result = true;
            else
                result = false;
        }
        dbHelper.close();
        return result;
    }
    public void delete()
    {

    }
    public void update()
    {

    }
    public POI getPoiByName(String name)
    {
        return null;
    }
    public ArrayList<String> getListNames()
    {
        
        return null;
    }
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "DBP", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table rtable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "description text,"
                    + "latitude text,"
                    + "longitude text"
                    + ");");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
