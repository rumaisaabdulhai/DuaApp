package com.example.rumaisaabdulhai.duaapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "duas_database.sqlite";
    private final Context myContext;
    public final static String DATABASE_PATH = "/data/data/com.example.rumaisaabdulhai.duaapp/databases/";


    private void copyDataBase() throws IOException {
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        try (InputStream myInput = myContext.getAssets().open(DATABASE_NAME); OutputStream myOutput = new FileOutputStream(outFileName)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer,0,length);
            }

            myOutput.flush();
        }
    }

    public void deleteDataBase() {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if (file.exists()) {
            file.delete();
            System.out.println("delete database file.");
        }
    }
    private boolean checkDataBase() {
        return new File(DATABASE_PATH + DATABASE_NAME).exists();
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.myContext = context;

    }

    public void createDataBase() {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            deleteDataBase();
        }
        dbExist = checkDataBase();
        if (dbExist) {
            Log.v("DB Exists", "db exists");
        }
        else {
            this.getReadableDatabase();
            try {
                this.close();
                copyDataBase();
          } catch (IOException e){
                throw new Error("Error copying database");
                }
            }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion) {
        deleteDataBase();
        }

    }
}
