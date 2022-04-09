package com.study.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cowin.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper databaseHelperInstance;

    private DatabaseHelper(Context paramContext) {
        super(paramContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getDatabaseHelperInstance(
            Context context) {
        if (databaseHelperInstance == null) {
            databaseHelperInstance = new DatabaseHelper(context);
        }

        return databaseHelperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
        paramSQLiteDatabase.execSQL(CowinDao.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ArrayList<String> arrTblNames = new ArrayList<>();
        try (Cursor c = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' order by name",
                null)) {

            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    arrTblNames.add(c.getString(c.getColumnIndex("name")));
                    c.moveToNext();
                }
            }
        } catch (Exception e) {
        }
        try {
            for (String tableName : arrTblNames) {
                db.execSQL("DROP TABLE IF EXISTS " + tableName);
            }
        } catch (Exception e) {
        }
        onCreate(db);
    }
}
