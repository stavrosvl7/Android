package com.unipi.stavrosvl7.exercise_p14015;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements  LocationListener,View.OnClickListener {

    SQLiteDatabase database;
    MyTextToSpeech myTextToSpeech;
    TextView textView;
    Button button_start, button_stop, button_converter, button_speechrecognition;
    SharedPreferences sharedPreferences;
    ConstraintLayout constraintLayout;

    LocationManager locationManager;
    static final int REQ_CODE = 1;
    static final int RESULT_SPEECH = 2;
    int status = 0;


    private HandlerThread handlerThread = new HandlerThread("handlerThread");
    private Handler threadHandler;

    boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textviewOfSpeed);
        button_start = findViewById(R.id.button_start);
        button_stop = findViewById(R.id.button_stop);
        button_converter = findViewById(R.id.button_converter);
        button_converter.setTag(0);
        button_speechrecognition = findViewById(R.id.button_speechrecognition);
        button_start.setOnClickListener(MainActivity.this);
        button_stop.setOnClickListener(MainActivity.this);
        button_converter.setOnClickListener(MainActivity.this);
        button_speechrecognition.setOnClickListener(MainActivity.this);

        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        database = openOrCreateDatabase("SpeedDatabase", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS SpeedDatabase(longitude DOUBLE, latitude DOUBLE, speed FLOAT, time STRING, timestamp LONG);");

        myTextToSpeech = new MyTextToSpeech(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);



        handlerThread.start();
        threadHandler = new Handler(handlerThread.getLooper());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case(R.id.button_start):
                if (!checkLocationPermission()) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE);
                }
                else {
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, MainActivity.this);
                }
                break;
            case(R.id.button_stop):
                locationManager.removeUpdates(MainActivity.this);
                constraintLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                textView.setText("0.0");
                myTextToSpeech.stop();
                check = true;
                break;
            case(R.id.button_converter):
                if(status == 1){
                    button_converter.setText(R.string.button_converter_miles);
                    view.setTag(0);
                }
                else {
                    button_converter.setText(R.string.button_converter_km);
                    view.setTag(1);
                }
                status = (Integer) view.getTag();
                break;
            case(R.id.button_speechrecognition):
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.intent_message));
                startActivityForResult(intent,RESULT_SPEECH);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==RESULT_SPEECH && resultCode==RESULT_OK){
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (int i=0;i<results.size();i++) {
                if(results.get(i).equals("map")){
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume(){
        super.onResume();
        check = true;

    }

    @Override
    public void onPause(){
        super.onPause();
        check = false;

    }

    @Override
    public void onDestroy() {
        handlerThread.quit();
        if (myTextToSpeech != null) {
            myTextToSpeech.stop();
            myTextToSpeech.shutdown();
        }
        super.onDestroy();
    }

    public boolean checkLocationPermission(){

        return (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (checkLocationPermission()) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 0, this);
        }
        else{
            Toast.makeText(this,getString(R.string.location_toast), Toast.LENGTH_LONG).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (status == 1) {
            textView.setText(String.format("%.1f " + getString(R.string.miles) + "/h", (location.getSpeed() * 3600 / 1000) * 0.6214));
        } else {
            textView.setText(String.format("%.1f " + getString(R.string.kilometers) + "/h", location.getSpeed() * 3600 / 1000));
        }

        if(location.getSpeed()* 3600 / 1000>Float.parseFloat(sharedPreferences.getString("speedlimit", "300"))) {
            threadHandler.post(new InsertToDatabase(database,location));
            constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            if(check){
                myTextToSpeech.speak("be careful");
                check = false;
            }
        }
        else {
            constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            check = true;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case(R.id.menu_database):
                Intent intent2 = new Intent(this, RecyclerViewActivity.class);
                startActivity(intent2);
                break;
            case(R.id.menu_speedlimiter):
                Intent intent1 = new Intent(this, Preferences_activity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

}
