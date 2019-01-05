package com.example.rumaisaabdulhai.duaapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
            databaseHelper.createDataBase();
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            populateDatabase(this.getApplicationContext(), db);

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
    }

    private void populateDatabase(Context context, SQLiteDatabase database) {
// load data
        AssetManager assetManager = context.getAssets();
        try {
            String dropTableSql = "drop table if exists duas";
            database.execSQL(dropTableSql);
            String createTableSql = "create table if not exists duas(category varchar(255) not null, " +
                    "name varchar(255) not null, " +
                    "audioFileName varchar(255), " +
                    "arabic text not null, " +
                    "transliteration text, " +
                    "translation text, " +
                    "reference text)";
            database.execSQL(createTableSql);
            String deleteTableSql = "delete from duas";
            database.execSQL(deleteTableSql);

            InputStream inputStream = assetManager.open("duas.csv");
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line;
            String[] values;
            String insertSql = "insert into duas(category, name, audioFileName, arabic, transliteration, translation, reference)" +
                    "values(?, ?, ?, ?, ?, ?)";
            while ((line = bufferedReader.readLine()) != null) {
                values = line.split("\\|");
                List<String> list = new ArrayList<>();
                list.add(values[0]);
                list.add(values[1]);
                list.add(values[2]);
                list.add(values[3]);
                list.add(values[4]);
                list.add(values[5]);
                list.add(values[6]);
                database.execSQL(insertSql, list.toArray());
            }
        } catch (IOException e) {
            Log.e("TBCAE", "Failed to open data input file");
            e.printStackTrace();
        }
    }

}
