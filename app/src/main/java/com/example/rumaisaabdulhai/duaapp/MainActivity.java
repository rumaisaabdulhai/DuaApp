package com.example.rumaisaabdulhai.duaapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static com.example.rumaisaabdulhai.duaapp.Constants.DUA_GROUP;
import static com.example.rumaisaabdulhai.duaapp.Constants.DUA_NAME;

public class MainActivity extends AppCompatActivity {

    private List<String> expandableListTitle;
    private Map<String, List<String>> expandableListDetail = new HashMap<>();
    private ExpandableListView expandableListView;
    private DuaListViewAdapter duaListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(this.getApplicationContext());
            SQLiteDatabase db = databaseHelper.populateDatabase(this.getApplicationContext());

            Cursor res = db.rawQuery("select name, category from DUAS", null);
            while (res.moveToNext()) {
                String name = res.getString(0);
                String category = res.getString(1);
                Log.e("Database row", name + "=>" + category);
                expandableListDetail.putIfAbsent(category, new ArrayList<String>());
                expandableListDetail.get(category).add(name);
            }
        } catch (Exception e) {
            Log.e("Error happened", "Reading database", e);
        }

        expandableListView = (ExpandableListView) findViewById(R.id.expandListView);
        duaListViewAdapter = new DuaListViewAdapter(this, expandableListDetail);
        expandableListView.setAdapter(duaListViewAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String group = duaListViewAdapter.getGroup(groupPosition).toString();
                String dua = duaListViewAdapter.getChild(groupPosition, childPosition).toString();
//                Toast.makeText(getApplicationContext(),"clicked " + group + "- '" + dua + "'", Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(parent.getContext(), DuaDetailActivity.class);
                myIntent.putExtra(DUA_GROUP, group);
                myIntent.putExtra(DUA_NAME, dua);
                startActivityForResult(myIntent,0);

                return false;
            }
        });

        NavigationUtils.addNavigation(this);
    }

    public void showMap(View view) {
        Intent myIntent = new Intent(this.getApplicationContext(), MapsActivity.class);
        startActivityForResult(myIntent,0);
        Log.e("Main", "showing Map");

    }
}
