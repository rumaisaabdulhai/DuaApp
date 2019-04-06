package com.example.rumaisaabdulhai.duaapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class NavigationUtils {

    public static void addNavigation(final AppCompatActivity activity) {
        BottomNavigationView navigation = activity.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent a = new Intent(activity, MainActivity.class);
                        activity.startActivity(a);
                        break;
                    case R.id.favorites:
                        Intent b = new Intent(activity, FavoriteDuaActivity.class);
                        activity.startActivity(b);
                        break;
                }
                return false;
            }
        });
    }
}