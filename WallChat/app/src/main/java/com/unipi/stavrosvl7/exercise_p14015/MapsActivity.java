package com.unipi.stavrosvl7.exercise_p14015;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SQLiteDatabase db;
    ArrayList<LatLng> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        data = new ArrayList<LatLng>();
        db = openOrCreateDatabase("SpeedDatabase", MODE_PRIVATE, null);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getDatabase();
        for(int i=0;i<data.size();i++){
            createMarker(data.get(i));
        }
        if(data.size()>0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(data.get(0)));
        }
        showMessage(getString(R.string.title),getString(R.string.message));
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true).setTitle(title).setMessage(message).show();
    }

    public void getDatabase(){
        Cursor cursor = db.rawQuery("SELECT * FROM SpeedDatabase",null);
        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            LatLng latLng = new  LatLng(cursor.getDouble(1),cursor.getDouble(0));
            data.add(latLng);
        }
        cursor.close();
    }

    public void createMarker(LatLng latLng) {
        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

        // Placing a marker on the touched position
        mMap.addMarker(markerOptions);

    }

}
