package com.example.rumaisaabdulhai.duaapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.rumaisaabdulhai.duaapp.Constants.DUA_GROUP;
import static com.example.rumaisaabdulhai.duaapp.Constants.DUA_NAME;

public class DuaDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dua_detail_view);

        Intent intent = getIntent();
        String group = intent.getStringExtra(DUA_GROUP);
        String dua = intent.getStringExtra(DUA_NAME);

        TextView titleView = findViewById(R.id.duaTitleText);
        titleView.setText(group + " - " + dua);

        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(this.getApplicationContext());
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            String selectSql = "select arabic, transliteration, translation, reference from duas " +
                    "where category = ? and name = ?";
            String[] parameters = {group, dua};
            Cursor res = db.rawQuery(selectSql, parameters);
            while (res.moveToNext()) {
                String arabicText = res.getString(0);
                String transliteration = res.getString(1);
                String translation = res.getString(2);
                String reference = res.getString(3);

                Log.i("database values" , arabicText + " " + transliteration + " " + translation);

                TextView arabicTextView = findViewById(R.id.duaArabicText);
                arabicTextView.setText(arabicText);

                TextView transliterationTextView = findViewById(R.id.duaTransliterationText);
                transliterationTextView.setText(transliteration);

                TextView translationTextView = findViewById(R.id.duaTranslationText);
                translationTextView.setText(translation);

                TextView referenceTextView = findViewById(R.id.duaReferenceText);
                referenceTextView.setText(reference);
            }
        } catch (Exception e) {
            Log.e("Error happened", "Reading database", e);
        }


    }

}
