package com.example.rumaisaabdulhai.duaapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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

    public void populateDatabase(Context context, SQLiteDatabase database) {
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
                    "favorite INTEGER DEFAULT 0, " +
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
                    "values(?, ?, ?, ?, ?, ?, ?)";
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

    public void markFavorite(String category, String name, boolean favorite,SQLiteDatabase database) {
        String sql = "update duas set favorite=? where category=? and name=?";
        List list = new ArrayList<>();
        list.add(favorite ? 1:0);
        list.add(category);
        list.add(name);
        database.execSQL(sql, list.toArray());

    }

    public SQLiteDatabase populateDatabase(Context context) {
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            if (!checkDataBase()) {
                databaseHelper.createDataBase();
                populateDatabase(context,db);
            }
            return db;

        } catch (Exception e) {
            Log.e("Error happened", "Reading database", e);
        }
        return null;

    }

    public SQLiteDatabase openDatabase() {
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(this.myContext);
            return databaseHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.e("Error happened", "Reading database", e);
        }
        return null;
    }
}
