package com.example.rumaisaabdulhai.duaapp;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MosqueFinderService {

    private static final String PLACE_API_URL_FORMAT = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%s&radius=%d&types=mosque&key=%s";
    private final String mapApiKey;

    public MosqueFinderService(String mapApiKey){
        this.mapApiKey = mapApiKey;
    }

    public List<String> findNearbyMosques(Location location) {
        String str = String.format("%s,%s", Location.convert(location.getLatitude(), Location.FORMAT_DEGREES),
            Location.convert(location.getLongitude(), Location.FORMAT_DEGREES));
        return findNearbyMosque(str);
    }

    public List<String> findNearbyMosque(String location) {

        // Build Url
        String formattedUrl = String.format(PLACE_API_URL_FORMAT, location, 1000, mapApiKey);
        List<String> result = new ArrayList<>();

        try {
            // convert Url string to Url object
            URL url = new URL(formattedUrl);

            // Create connection to Url
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Create reader to read from connection
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line;

            // Read all lines into string buffer
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }

            // convert output into JSON obj
            JSONObject json = new JSONObject(stringBuffer.toString());
            JSONArray results = json.getJSONArray("results");

            //extract name from returned results
            for (int ii = 0; ii < results.length(); ii++) {
                JSONObject obj = results.getJSONObject(ii);
                result.add(obj.getString("name"));
            }
        }

        catch (Exception e) {
            e.printStackTrace();
            Log.e("Error getting data from url", formattedUrl);
        }

        return result;
    }
}
