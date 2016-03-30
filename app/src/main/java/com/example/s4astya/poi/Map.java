package com.example.s4astya.poi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.s4astya.poi.dao.Dao;
import com.example.s4astya.poi.model.POI;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Map extends AppCompatActivity {

    private GoogleMap mMap;
    private  Dao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitle("Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();

        dao = new Dao(this);
        ArrayList<POI> pois = dao.getAllPOIs();
        for(POI p : pois) {
            if(!p.getLongitude().equals("") && !p.getLatitude().equals("")) {
                double lat = Double.parseDouble(p.getLatitude());
                double lon = Double.parseDouble(p.getLongitude());
                LatLng point = new LatLng(lat, lon);
                mMap.addMarker(new MarkerOptions().position(point).title(p.getName()));
            }
            else continue;
        }
    }
}
