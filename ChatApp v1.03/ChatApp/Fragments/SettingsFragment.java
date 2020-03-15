package com.unipi.stavrosvl7.ChatApp.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.unipi.stavrosvl7.ChatApp.MainActivity;
import com.unipi.stavrosvl7.ChatApp.R;
import com.unipi.stavrosvl7.ChatApp.StartActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SettingsFragment extends Fragment implements LocationListener {
    Button settingsLogoutButton,settingsLocationButton;
    LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        settingsLogoutButton = view.findViewById(R.id.settingsLogoutButton);
        settingsLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        settingsLocationButton = view.findViewById(R.id.settingsLocationButton);
        settingsLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAddress();
            }
        });

        return view;
    }

    public boolean checkLocationPermission(){

        return (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

    }

    private void sendAddress(){
        if(checkLocationPermission()){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Geocoder geocoder;
            String address = "";
            geocoder = new Geocoder(getContext(), Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                address = addresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(getContext(), address, Toast.LENGTH_SHORT).show();
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onPause() {
        if(checkLocationPermission()){
            locationManager.removeUpdates(this);
        }
        super.onPause();
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
