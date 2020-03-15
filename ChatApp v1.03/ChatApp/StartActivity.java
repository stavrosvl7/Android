package com.unipi.stavrosvl7.ChatApp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;



public class StartActivity extends AppCompatActivity implements LocationListener,View.OnClickListener {
    static final int REQ_CODE = 1;
    private FirebaseAuth mAuth;
    Button buttonRegister,buttonLogin;
    private SharedPreferences sharedPreferences;
    LocationManager locationManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mAuth = FirebaseAuth.getInstance();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        context = this;

        buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(StartActivity.this);
        buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(StartActivity.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }


    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();

        //updateUI(currentUser);

        if (mAuth.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            String welcome = getString(R.string.welcomeString) + " " + sharedPreferences.getString("email","");
            Toast.makeText(this,welcome,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        }



    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case(R.id.button_register):
                Intent intentRegister = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case(R.id.button_login):
                Intent intentLogin = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                break;
        }
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
            Toast.makeText(this,getString(R.string.welcomeString), Toast.LENGTH_LONG).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
