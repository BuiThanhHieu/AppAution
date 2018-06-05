package com.example.hieu.appaution;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "datadiadiem.sqlite";
    public static final String DBLOCATION = "/data/data/com.example.hieu.appaution/databases/";
 /* private static final String BASE_URL = "http://192.168.1.82:5000/";

    private static AsyncHttpClient client = new AsyncHttpClient();*/
    private Context context;
    private SQLiteDatabase database;
    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;
        File appDatabase = this.context.getApplicationContext().getDatabasePath(DBNAME);
        if (!appDatabase.exists()) {
            getReadableDatabase();
            if (copyDatabase(this.context)){
                Toast.makeText(context, "DB copied success", Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(context, "DB copied failed", Toast.LENGTH_SHORT);
            }
        }
        Log.e("Path 1", DBLOCATION);
    }

    public boolean copyDatabase(Context context) {
        try{
            InputStream inputStream = context.getAssets().open(DBNAME);
            String outFileName = DBLOCATION + DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDatabase() {
        boolean dbExist= checkDataBase();
        String dbPath;
        try {
             dbPath = this.context.getDatabasePath(DBNAME).getPath();
            if (database != null && database.isOpen()){
                return;
            }
            database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch (SQLiteException e){}

    }
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DBLOCATION + DBNAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }


    public void closeDatabase() {
        if (database != null){
            database.close();
        }
    }



    public List<City> getListCity() {
        City city;
        List<City> cities = new ArrayList<>();
        openDatabase();
        Cursor cursor;

             cursor = database.rawQuery("SELECT * FROM thanhpho", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                city = new City();
                city.setId(cursor.getInt(0));
                city.setName(cursor.getString(1));
                cities.add(city);
                cursor.moveToNext();
            }



        closeDatabase();
        return cities;
    }
    public List<Distrist> getListDistrist(String tentp){
        Distrist distrist;
        List<Distrist> distrists=new ArrayList<>();
        openDatabase();

        Cursor cursor =database.rawQuery("SELECT idquanhuyen,quanhuyen.idtp,tenquanhuyen FROM quanhuyen,thanhpho WHERE quanhuyen.idtp=thanhpho.id and thanhpho.tentp='"+tentp+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            distrist =new Distrist();
            distrist.setId(cursor.getInt(0));
            distrist.setIdtp(cursor.getInt(1));
            distrist.setName(cursor.getString(2));
            distrists.add(distrist);
            cursor.moveToNext();
        }
        closeDatabase();
        return distrists;

}
        }
