package com.example.rumaisaabdulhai.duaapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import static com.example.rumaisaabdulhai.duaapp.Constants.DUA_GROUP;
import static com.example.rumaisaabdulhai.duaapp.Constants.DUA_NAME;

public class CurrentLocationListener implements LocationListener {
    private final MosqueFinderService mosqueFinderService;
    private final AppCompatActivity appCompatActivity;

    public CurrentLocationListener (AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        this.mosqueFinderService = new MosqueFinderService(appCompatActivity.getResources().getString(R.string.google_maps_key));
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("Received location",location.toString());

        List<String> mosques = mosqueFinderService.findNearbyMosques(location);
        String message = "nearby mosques: " + mosques;
        Toast.makeText(appCompatActivity, message, Toast.LENGTH_LONG).show();

        if (!mosques.isEmpty()) {
            Intent myIntent = new Intent(appCompatActivity, DuaDetailActivity.class);
            myIntent.putExtra(DUA_GROUP, "Mosque");
            myIntent.putExtra(DUA_NAME, "Upon entering Mosque");
//            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            appCompatActivity.startActivity(myIntent);
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
}
