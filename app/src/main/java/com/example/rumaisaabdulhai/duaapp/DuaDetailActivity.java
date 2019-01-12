package com.example.rumaisaabdulhai.duaapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

import static com.example.rumaisaabdulhai.duaapp.Constants.DUA_GROUP;
import static com.example.rumaisaabdulhai.duaapp.Constants.DUA_NAME;

public class DuaDetailActivity extends Activity {
    private MediaPlayer player;

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

            String selectSql = "select arabic, transliteration, translation, reference, audioFileName from duas " +
                    "where category = ? and name = ?";
            String[] parameters = {group, dua};
            Cursor res = db.rawQuery(selectSql, parameters);
            while (res.moveToNext()) {
                String arabicText = res.getString(0);
                String transliteration = res.getString(1);
                String translation = res.getString(2);
                String reference = res.getString(3);
                String audioFileName = res.getString(4);

                Log.i("database values", arabicText + " " + transliteration + " " + translation + " " + audioFileName);

                TextView arabicTextView = findViewById(R.id.duaArabicText);
                arabicTextView.setText(arabicText);

                TextView transliterationTextView = findViewById(R.id.duaTransliterationText);
                transliterationTextView.setText(transliteration);

                TextView translationTextView = findViewById(R.id.duaTranslationText);
                translationTextView.setText(translation);

                TextView referenceTextView = findViewById(R.id.duaReferenceText);
                referenceTextView.setText(reference);

                player = new MediaPlayer();
                AssetManager assets = this.getAssets();
                //InputStream inputStream = assets.open(audioFileName);
                AssetFileDescriptor fd = assets.openFd(audioFileName);
                Log.e("Asset file descriptor", fd.getFileDescriptor().toString());
                player.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                player.prepare();
            }
        } catch (Exception e) {
            Log.e("Error happened", "Reading database", e);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

    public void returnMain(View view) {
        super.onBackPressed();
    }

    public void playAudio(View view) {
        if (!player.isPlaying()) {
            //player.seekTo(0); //rewind to beginning
            player.start();
            changeIcon(true);
        } else {
            player.pause();
            changeIcon(false);
        }
    }

    public void changeIcon(boolean playing) {
        ImageButton button = findViewById(R.id.playButton);
        button.setImageResource(playing ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
    }

}
