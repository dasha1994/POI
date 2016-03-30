package com.example.s4astya.poi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.s4astya.poi.model.POI;

import java.util.ArrayList;

/**
 * Created by S4ASTYA on 29.03.2016.
 */
public class Dao {

    DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public Dao(Context cont) {
        context = cont;
    }

    public void insert(String name, String description, String latitude, String longitude, ArrayList<String> path) {
        open();
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("description", description);
        cv.put("latitude", latitude);
        cv.put("longitude", longitude);
        db.insert("rtable", null, cv);
        for (String p : path) {
            ContentValues cvphoto = new ContentValues();
            cvphoto.put("path", p);
            cvphoto.put("namepoi", name);
            db.insert("tphoto", null, cvphoto);
        }
        close();
    }

    public void delete(String name) {
        open();
        db = dbHelper.getWritableDatabase();
        db.delete("rtable", "name =? ", new String[]{name});
        close();
    }

    public void update(String pname, String newname, String description, String latitude, String longitude) {
        open();
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", newname);
        cv.put("description", description);
        cv.put("latitude", latitude);
        cv.put("longitude", longitude);
        db.update("rtable", cv, "name=?", new String[]{pname});
        close();
    }

    public POI getPoiByName(String name2) {
        open();
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query("rtable", new String[]{"name", "description", "latitude", "longitude"}, "name=?", new String[]{name2}, null, null, null);

        POI poi = new POI();
        if (c.moveToFirst()) {
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
        Cursor c2 = db.query("tphoto", new String[]{"path"}, "namepoi=?", new String[]{name2}, null, null, null);
        ArrayList<String> paths = new ArrayList<>();
        if (c2.moveToFirst()) {
            int pathColIndex = c2.getColumnIndex("path");
            do {
                paths.add(c2.getString(pathColIndex));
            } while (c2.moveToNext());
        }
        c2.close();
        poi.setPaths(paths);
        close();
        return poi;
    }

    public ArrayList<POI> getAllPOIs() {
        open();
        db = dbHelper.getReadableDatabase();
        ArrayList<POI> poi = new ArrayList<>();
        Cursor c = db.query("rtable", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            int desColIndex = c.getColumnIndex("description");
            int latColIndex = c.getColumnIndex("latitude");
            int longColIndex = c.getColumnIndex("longitude");
            do {
                POI p = new POI();
                p.setName(c.getString(nameColIndex));
                p.setDescription(c.getString(desColIndex));
                p.setLatitude(c.getString(latColIndex));
                p.setLongitude(c.getString(longColIndex));
                poi.add(p);
            } while (c.moveToNext());
        }
        close();
        return poi;
    }

    public ArrayList<String> getListNames() {
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

    private void open() {
        dbHelper = new DBHelper(context);
    }

    private void close() {
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
            db.execSQL("create table tphoto ("
                    + "id integer primary key autoincrement,"
                    + "path text,"
                    + "namepoi text"
                    + ");");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
