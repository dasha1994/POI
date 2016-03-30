package com.example.s4astya.poi;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.s4astya.poi.dao.Dao;
import com.example.s4astya.poi.model.POI;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Edit_POI extends AppCompatActivity implements View.OnClickListener {

    private GoogleMap mMap;
    private LinearLayout ll;
    private EditText eName;
    private  EditText eDescription;
    private EditText eCoords;
    private Button saveBtn;
    private Dao dao;
    private POI poi;

    private String oldname;
    private double sLatitude;
    private double sLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__poi);
        setTitle("POI:");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        oldname = intent.getStringExtra("poiName");

        eName = (EditText) findViewById(R.id.ename);
        eDescription = (EditText) findViewById(R.id.edescription);
        eCoords = (EditText) findViewById(R.id.ecoords);
        ll = (LinearLayout) findViewById(R.id.linlayout);

        saveBtn = (Button) findViewById(R.id.esaveBtn);
        saveBtn.setOnClickListener(this);

        dao = new Dao(this);
        poi = getPoi(intent.getStringExtra("poiName"));

        eName.setText(poi.getName());
        eDescription.setText(poi.getDescription());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
                eCoords.setText("Latitude:" + point.latitude + "\nLongitude:" + point.longitude);
            }
        });
        if (!poi.getLongitude().equals("") && !poi.getLatitude().equals("")) {
            sLatitude = Double.parseDouble(poi.getLatitude());
            sLongitude = Double.parseDouble(poi.getLongitude());
            mMap.addMarker(new MarkerOptions().position(new LatLng(sLatitude, sLongitude)));
            eCoords.setText("Latitude:  " + poi.getLatitude() + "\nLongitude:  " + poi.getLongitude());
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300, 300);
        for (String picturePath : poi.getPaths()) {
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            ll.addView(imageView, lp);

        }
    }

    public POI getPoi(String name) {
        POI p = dao.getPoiByName(name);
        return p;
    }

    @Override
    public void onClick(View v) {
        savePoi();
        finish();
    }

    public void savePoi() {
        dao.update(oldname, String.valueOf(eName.getText()), String.valueOf(eDescription.getText()),
                String.valueOf(sLatitude), String.valueOf(sLongitude));
    }
}
