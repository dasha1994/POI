package com.example.s4astya.poi;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.*;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Add_POI extends AppCompatActivity {

    private GoogleMap mMap;

    EditText eName;
    EditText eDescription;
    EditText eCoords;

    Button saveB;
    Button cancelB;
    String sLatitude;
    String sLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__poi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add POI");

        eName = (EditText)findViewById(R.id.name);
        eDescription = (EditText)findViewById(R.id.description);
        eCoords = (EditText)findViewById(R.id.coords);

        saveB = (Button)findViewById(R.id.saveBtn);
        cancelB = (Button)findViewById(R.id.cancelBtn);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
                eCoords.setText("Latitude:" + point.latitude + "\nLongitude:" + point.longitude);
                sLatitude = String.valueOf(point.latitude);
                sLongitude = String.valueOf(point.longitude);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.saveBtn :
                        if(savePOI())
                        {
                            Intent intent = new Intent(Add_POI.this,MainActivity.class);
                            startActivity(intent);
                           // Toast.makeText(getApplicationContext(), "Saved",
                             //       Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Failed",
                                    Toast.LENGTH_LONG).show();
                    case R.id.cancelBtn :
                        clearFields();
                }
            }
        };

        saveB.setOnClickListener(listener);
        cancelB.setOnClickListener(listener);
    }
    public void clearFields()
    {
        eName.setText("");
        eDescription.setText("");
        eCoords.setText("");
        mMap.clear();
    }
    public boolean savePOI()
    {
        Dao poiDao = new Dao(this);
        return  poiDao.insert(String.valueOf(eName.getText()),String.valueOf(eDescription.getText()),
                sLatitude,sLongitude);
    }
}
