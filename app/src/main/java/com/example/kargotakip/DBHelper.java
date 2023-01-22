package com.example.kargotakip;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "my_cargo.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String TABLE_CARGO_CREATE = "CREATE TABLE " + TablesInfo.CargoEntry.TABLE_NAME + " (" +
                    TablesInfo.CargoEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TablesInfo.CargoEntry.COLUMN_NAME + " TEXT," +
                    TablesInfo.CargoEntry.COLUMN_NO + " TEXT, " +
                    TablesInfo.CargoEntry.COLUMN_CREATE_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP," +
                    TablesInfo.CargoEntry.COLUMN_STATUS + " string " +
                    ")";
    private static final String TABLE_USER_CREATE = "CREATE TABLE " + TablesInfo.UserEntry.TABLE_NAME + " (" +
                    TablesInfo.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TablesInfo.UserEntry.COLUMN_NAME + " TEXT, " +
                    TablesInfo.UserEntry.COLUMN_PASSWORD + " TEXT " +
                    ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CARGO_CREATE);
        db.execSQL(TABLE_USER_CREATE);
        ContentValues cv = new ContentValues();
        cv.put(TablesInfo.UserEntry.COLUMN_ID, "1");
        cv.put(TablesInfo.UserEntry.COLUMN_NAME, "admin");
        cv.put(TablesInfo.UserEntry.COLUMN_PASSWORD, "12345");

        db.insert(TablesInfo.UserEntry.TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TablesInfo.CargoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablesInfo.UserEntry.TABLE_NAME);

        onCreate(db);
    }

    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TablesInfo.UserEntry.COLUMN_NAME, user.getUsername());
        cv.put(TablesInfo.UserEntry.COLUMN_PASSWORD, user.getPassword());
        long result = db.update(TablesInfo.UserEntry.TABLE_NAME,cv,TablesInfo.UserEntry.COLUMN_ID+"=?",new String[]{"1"} );
        if (result > -1)
            Log.i("DBHelper", "Kullanıcı başarıyla kaydedildi "+ cv);
        else
            Log.i("DBHelper", "Kullanıcı kaydedilemedi");

        db.close();
    }
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TablesInfo.UserEntry.COLUMN_ID, user.getId());
        cv.put(TablesInfo.UserEntry.COLUMN_NAME, user.getUsername());
        cv.put(TablesInfo.UserEntry.COLUMN_PASSWORD, user.getPassword());

        long result = db.insert(TablesInfo.UserEntry.TABLE_NAME, null, cv);

        if (result > -1)
            Log.i("DBHelper", "Kullanıcı başarıyla kaydedildi "+ cv);
        else
            Log.i("DBHelper", "Kullanıcı kaydedilemedi");

        db.close();
    }
    public void addCargo(Cargo cargo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TablesInfo.CargoEntry.COLUMN_ID, cargo.getCargo_id());
        cv.put(TablesInfo.CargoEntry.COLUMN_NO, cargo.getCargo_no());
        cv.put(TablesInfo.CargoEntry.COLUMN_NAME, cargo.getCargo_name());
        cv.put(TablesInfo.CargoEntry.COLUMN_STATUS, cargo.getCargo_status());
        cv.put(TablesInfo.CargoEntry.COLUMN_CREATE_DATE, cargo.getCargo_date());
        long result = db.insert(TablesInfo.CargoEntry.TABLE_NAME, null, cv);

        if (result > -1)
            Log.i("DBHelper", "Kargo başarıyla kaydedildi "+ cv);
        else
            Log.i("DBHelper", "Kargo kaydedilemedi");

        db.close();
    }

    public void deleteCargo(String cargoID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TablesInfo.CargoEntry.TABLE_NAME, TablesInfo.CargoEntry.COLUMN_ID + "=?", new String[]{cargoID});
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<Cargo> getCargoList() {
        ArrayList<Cargo> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                TablesInfo.CargoEntry.COLUMN_ID,
                TablesInfo.CargoEntry.COLUMN_NAME,
                TablesInfo.CargoEntry.COLUMN_NO,
                TablesInfo.CargoEntry.COLUMN_STATUS,
                TablesInfo.CargoEntry.COLUMN_CREATE_DATE};

        Cursor c = db.query(TablesInfo.CargoEntry.TABLE_NAME, projection, null, null, null, null, null);
        while (c.moveToNext()) {
            data.add(new Cargo(c.getString(c.getColumnIndex(TablesInfo.CargoEntry.COLUMN_ID)), c.getString(c.getColumnIndex(TablesInfo.CargoEntry.COLUMN_NAME)), c.getString(c.getColumnIndex(TablesInfo.CargoEntry.COLUMN_NO)), c.getString(c.getColumnIndex(TablesInfo.CargoEntry.COLUMN_STATUS)), c.getString(c.getColumnIndex(TablesInfo.CargoEntry.COLUMN_CREATE_DATE))));
        }

        c.close();
        db.close();

        return data;
    }
    @SuppressLint("Range")
    public User getUser(){
        ArrayList<User> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                TablesInfo.UserEntry.COLUMN_ID,
                TablesInfo.UserEntry.COLUMN_NAME,
                TablesInfo.UserEntry.COLUMN_PASSWORD,};
        Cursor c = db.query(TablesInfo.UserEntry.TABLE_NAME, projection, null, null, null, null, null);
        while (c.moveToNext()) {
            data.add(new User(c.getString(c.getColumnIndex(TablesInfo.UserEntry.COLUMN_ID)), c.getString(c.getColumnIndex(TablesInfo.UserEntry.COLUMN_NAME)), c.getString(c.getColumnIndex(TablesInfo.UserEntry.COLUMN_PASSWORD))));
        }
        User user = new User(data.get(0).getId(),data.get(0).getUsername(),data.get(0).getPassword());
        db.close();
        c.close();
        return user;
    }
}
