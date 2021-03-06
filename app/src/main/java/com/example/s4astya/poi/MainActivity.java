package com.example.s4astya.poi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.s4astya.poi.dao.Dao;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView pois;
    private ArrayList names;
    private ArrayAdapter<String> adapter;
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("POIs");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_input_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPOI.class);
                startActivity(intent);
            }
        });

        dao = new Dao(this);
        names = dao.getListNames();

        pois = (ListView) findViewById(R.id.listView);
        pois.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, names);
        pois.setAdapter(adapter);
        pois.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final String itemTitle = (String) ((TextView) view).getText();
                builder.setTitle(itemTitle);

                builder.setNeutralButton("Open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, EditPOI.class);
                        intent.putExtra("poiName", itemTitle);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog resetDialog = builder.create();
                resetDialog.show();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mapItem:
                Intent intent = new Intent(MainActivity.this, Map.class);
                startActivity(intent);
                break;
            case R.id.delete:
                SparseBooleanArray sbArray = pois.getCheckedItemPositions();
                for (int i = 0; i < sbArray.size(); i++) {
                    int key = sbArray.keyAt(i);
                    if (sbArray.get(key)) {

                        dao.delete(names.get(key).toString());
                        names.remove(key);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.deleteAll:
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final String itemTitle = "Do you want to delete all POIs?";
                builder.setTitle(itemTitle);

                builder.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < names.size(); i++) {
                            dao.delete(names.get(i).toString());
                            names.remove(i);
                        }
                 //       adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_multiple_choice, dao.getListNames());
                       adapter.notifyDataSetChanged();
                    //    pois.setAdapter(adapter);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog resetDialog = builder.create();
                resetDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}


