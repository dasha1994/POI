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
    Context context;
    Dao(Context cont)
    {
       context = cont;
    }
    public boolean insert(String name,String description,String latitude,String longitude)
    {
        open();
        db = dbHelper.getWritableDatabase();
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
        close();
        return result;
    }
    public void delete()
    {

    }
    public void update()
    {

    }
    public POI getPoiByName(String name2)
    {
        open();
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query("rtable",new String[]{"name","description","latitude","longitude"},"name=?",new String[]{name2},null,null,null);

        POI poi = new POI();
        if(c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            int desColIndex = c.getColumnIndex("description");
            int latColIndex = c.getColumnIndex("latitude");
            int longColIndex = c.getColumnIndex("longitude");
            poi.setName(c.getString(nameColIndex));
            poi.setDescription(c.getString(desColIndex));
            poi.setLatitude(c.getString(latColIndex));
            poi.setLongitude(c.getString(longColIndex));
            c.close();
        }
        close();
        return poi;
    }
    public ArrayList<String> getListNames()
    {
        open();
        db = dbHelper.getReadableDatabase();
        ArrayList<String> names = new ArrayList<>();
        Cursor c = db.query("rtable", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            do {
                names.add(c.getString(nameColIndex));
            } while (c.moveToNext());
        }
        close();
        return names;
    }
    private void open()
    {
        dbHelper = new DBHelper(context);
        //db = dbHelper.getWritableDatabase();
    }
    private void close()
    {
        dbHelper.close();
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
