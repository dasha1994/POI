package com.example.s4astya.poi;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Add_POI extends AppCompatActivity implements View.OnClickListener{

    private static int RESULT_LOAD_IMAGE = 1;

    private GoogleMap mMap;
    private LinearLayout linphoto;
    private EditText eName;
    private EditText eDescription;
    private EditText eCoords;

    private Button saveB;
    private Button cancelB;
    private Button addPhoto;
    private String sLatitude = "";
    private String sLongitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__poi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add POI");

        linphoto = (LinearLayout)findViewById(R.id.linlayout);

        eName = (EditText) findViewById(R.id.name);
        eDescription = (EditText) findViewById(R.id.description);
        eCoords = (EditText) findViewById(R.id.coords);

        saveB = (Button) findViewById(R.id.saveBtn);
        cancelB = (Button) findViewById(R.id.cancelBtn);
        addPhoto = (Button) findViewById(R.id.addphoto);
        addPhoto.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        LatLng odessa = new LatLng(46.28, 30.44);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(odessa));
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
                    case R.id.saveBtn:
                        if (savePOI()) {
                            Intent intent = new Intent(Add_POI.this, MainActivity.class);
                            startActivity(intent);
                            // Toast.makeText(getApplicationContext(), "Saved",
                            //       Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Failed",
                                    Toast.LENGTH_LONG).show();
                    case R.id.cancelBtn:
                        //  clearFields();
                        finish();
                }
            }
        };
        /*if(TextUtils.isEmpty(eName.getText().toString())) {
            eName.setError("Should not be empty");
            saveB.setEnabled(false);
        }
        else saveB.setEnabled(true);
        */
        saveB.setOnClickListener(listener);
        cancelB.setOnClickListener(listener);
    }


    public boolean savePOI() {
        Dao poiDao = new Dao(this);
        return poiDao.insert(String.valueOf(eName.getText()), String.valueOf(eDescription.getText()),
                sLatitude, sLongitude);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            linphoto.addView(imageView,lp);

        }
    }
    @Override
    public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
}
