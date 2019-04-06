package com.example.rumaisaabdulhai.duaapp;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDuaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_dua);

        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(this.getApplicationContext());
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            String selectSql = "select name from duas where favorite = 1";
            String[] parameters = {};
            Cursor res = db.rawQuery(selectSql, parameters);
            List<String> names = new ArrayList<>();
            while (res.moveToNext()) {
                String name = res.getString(0);
                names.add(name);
            }

            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.favorite_list_item, names);
            ListView listView = findViewById(R.id.favoriteList);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("Error happened", "Reading database", e);
        }

        NavigationUtils.addNavigation(this);
    }
}