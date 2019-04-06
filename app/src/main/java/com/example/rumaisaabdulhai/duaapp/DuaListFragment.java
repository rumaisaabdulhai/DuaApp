package com.example.rumaisaabdulhai.duaapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.rumaisaabdulhai.duaapp.Constants.DUA_GROUP;
import static com.example.rumaisaabdulhai.duaapp.Constants.DUA_NAME;

public class DuaListFragment extends Fragment {
    private Map<String, List<String>> expandableListDetail = new HashMap<>();
    private ExpandableListView expandableListView;
    private DuaListViewAdapter duaListViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       try {
           DatabaseHelper databaseHelper = new DatabaseHelper(this.getActivity().getApplicationContext());
           SQLiteDatabase db = databaseHelper.openDatabase();

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

       expandableListView = this.getActivity().findViewById(R.id.expandListView);
       duaListViewAdapter = new DuaListViewAdapter(this.getContext(), expandableListDetail);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dua_list, container, false);
    }
}