package com.example.s4astya.poi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Edit_POI extends AppCompatActivity implements View.OnClickListener{

    GoogleMap mMap;
    EditText eName;
    EditText eDescription;
    EditText eCoords;
    Button editBtn;
    Button saveBtn;
    Button cancelBtn;
    Dao dao;
    KeyListener kname;
    KeyListener kdescription;

    String oldname;
    double sLatitude;
    double sLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__poi);
        setTitle("POI:");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        oldname =intent.getStringExtra("poiName");
        eName = (EditText)findViewById(R.id.ename);
        eDescription = (EditText)findViewById(R.id.edescription);
        eCoords = (EditText)findViewById(R.id.ecoords);

        kname = eName.getKeyListener();
        eName.setKeyListener(null);

        kdescription = eDescription.getKeyListener();
        eDescription.setKeyListener(null);


        editBtn = (Button)findViewById(R.id.editBtn);
        saveBtn = (Button)findViewById(R.id.esaveBtn);
        cancelBtn =(Button)findViewById(R.id.edeleteBtn);
        editBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        dao = new Dao(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        getPoi(intent.getStringExtra("poiName"));

    }
    public POI getPoi(String name)
    {
        POI p = dao.getPoiByName(name);
        eName.setText(p.getName());
        eDescription.setText(p.getDescription());
        if(!p.getLongitude().equals("") && !p.getLatitude().equals("")) {
            sLatitude = Double.parseDouble(p.getLatitude());
            sLongitude = Double.parseDouble(p.getLongitude());
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng point) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(point));
                }
            });
        }
        eCoords.setText("Latitude:  " + p.getLatitude()+"\nLongitude:  " + p.getLongitude());
        return p;
    }

    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.editBtn :
               editPoi();
               break;
           case R.id.esaveBtn :
               savePoi();
               break;
           case R.id.edeleteBtn :
               deletePoi();
               break;
       }
    }
    public void editPoi()
    {
        eName.setKeyListener(kname);
        eDescription.setKeyListener(kdescription);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
                eCoords.setText("Latitude:" + point.latitude + "\nLongitude:" + point.longitude);
                sLatitude = point.latitude;
                sLongitude = point.longitude;
            }
        });
    }
    public void savePoi()
    {
        dao.update(oldname, String.valueOf(eName.getText()), String.valueOf(eDescription.getText()),
                String.valueOf(sLatitude), String.valueOf(sLongitude));
        eName.setKeyListener(null);
        eDescription.setKeyListener(null);
    }
    public void deletePoi()
    {
        dao.delete(String.valueOf(eName.getText()));
        finish();
    }
}
