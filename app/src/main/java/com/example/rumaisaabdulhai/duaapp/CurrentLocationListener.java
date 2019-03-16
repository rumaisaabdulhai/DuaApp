package com.example.rumaisaabdulhai.duaapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class CurrentLocationListener implements LocationListener {
    private final MosqueFinderService mosqueFinderService;
    private final Context context;

    public CurrentLocationListener (Context context) {
        this.context = context;
        this.mosqueFinderService = new MosqueFinderService(context.getResources().getString(R.string.google_maps_key));
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
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
