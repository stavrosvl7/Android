package com.unipi.stavrosvl7.exercise_p14015;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SQLiteDatabase db;
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Spinner spinner;
    long HourAgo;
    private static final int HOUR = 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_activity);

        recyclerView = findViewById(R.id.recyclerview);
        spinner = findViewById(R.id.spinner);

        // use this setting to improve performance if you know that changes
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db = openOrCreateDatabase("SpeedDatabase", MODE_PRIVATE, null);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        HourAgo = System.currentTimeMillis() - HOUR;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (pos){
                    case(0):
                        new Thread(new Runnable() {
                            public void run() {
                                mAdapter = new MyAdapter(getData());
                                recyclerView.post(new Runnable() {
                                    public void run() {
                                        recyclerView.setAdapter(mAdapter);
                                    }
                                });
                            }
                        }).start();
                        break;
                    case(1):
                        new Thread(new Runnable() {
                            public void run() {
                                mAdapter = new MyAdapter(getDataLastHour());
                                recyclerView.post(new Runnable() {
                                    public void run() {
                                        recyclerView.setAdapter(mAdapter);
                                    }
                                });
                            }
                        }).start();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public ArrayList<GpsData> getData(){
        Cursor cursor = db.rawQuery("SELECT * FROM SpeedDatabase",null);
        ArrayList<GpsData> data = new ArrayList<>();
        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            GpsData gpsData = new GpsData();
            gpsData.dataLongitude = getString(R.string.longitude) + cursor.getString(0);
            gpsData.dataLatitude = getString(R.string.latitude) + cursor.getString(1);
            gpsData.dataSpeed = getString(R.string.speed) + cursor.getString(2);
            gpsData.dataTime = getString(R.string.time) + cursor.getString(3);
            data.add(gpsData);
        }
        cursor.close();
        return data;
    }


    public ArrayList<GpsData> getDataLastHour(){
        Cursor cursor = db.rawQuery("SELECT * FROM SpeedDatabase",null);
        ArrayList<GpsData> data = new ArrayList<>();
        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            if(cursor.getLong(4)>HourAgo) {
                GpsData gpsData = new GpsData();
                gpsData.dataLongitude = getString(R.string.longitude) + cursor.getString(0);
                gpsData.dataLatitude = getString(R.string.latitude) + cursor.getString(1);
                gpsData.dataSpeed = getString(R.string.speed) + cursor.getString(2);
                gpsData.dataTime = getString(R.string.time) + cursor.getString(3);
                data.add(gpsData);
            }
        }
        cursor.close();
        return data;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
