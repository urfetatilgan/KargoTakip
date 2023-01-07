package com.example.kargotakip;

import android.provider.BaseColumns;

public class TablesInfo {
    public static final class CargoEntry implements BaseColumns {
        public static final String TABLE_NAME = "cargos";

        public static final String COLUMN_ID = "cargo_id";
        public static final String COLUMN_NAME = "cargo_name";
        public static final String COLUMN_NO = "cargo_no";
        public static final String COLUMN_CREATE_DATE = "cargo_date";
        public static final String COLUMN_STATUS = "cargo_status";
    }
}
