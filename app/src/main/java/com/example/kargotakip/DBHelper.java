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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CARGO_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TablesInfo.CargoEntry.TABLE_NAME);

        onCreate(db);
    }

    public void addCargo(Cargo cargo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TablesInfo.CargoEntry.COLUMN_ID, cargo.getCargo_id());
        cv.put(TablesInfo.CargoEntry.COLUMN_NO, cargo.getCargo_no());
        cv.put(TablesInfo.CargoEntry.COLUMN_NAME, cargo.getCargo_name());
        cv.put(TablesInfo.CargoEntry.COLUMN_STATUS, cargo.getCargo_status());
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
                TablesInfo.CargoEntry.COLUMN_STATUS};

        Cursor c = db.query(TablesInfo.CargoEntry.TABLE_NAME, projection, null, null, null, null, null);
        while (c.moveToNext()) {
            data.add(new Cargo(c.getString(c.getColumnIndex(TablesInfo.CargoEntry.COLUMN_ID)), c.getString(c.getColumnIndex(TablesInfo.CargoEntry.COLUMN_NAME)), c.getString(c.getColumnIndex(TablesInfo.CargoEntry.COLUMN_NO)), c.getString(c.getColumnIndex(TablesInfo.CargoEntry.COLUMN_STATUS))));
        }

        c.close();
        db.close();

        return data;
    }
}