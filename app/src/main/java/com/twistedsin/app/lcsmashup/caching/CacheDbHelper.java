package com.twistedsin.app.lcsmashup.caching;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.twistedsin.app.lcsmashup.C;

/**
 * Created by Filipe Oliveira on 01-09-2014.
 */
public class CacheDbHelper extends SQLiteOpenHelper {

    public static final String CID = "CacheDbHelper";

    public static final String DATABASE_NAME = "Cache.db";
    private static final int DATABASE_VERSION = 1;


    public static final String TABLE_ALERTS = "alerts";

    public static final String COLUMN_HASH = "hash";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STARTDATE = "startDate";

    private static final String DATABASE_CREATE_ALERTS = "create table " + TABLE_ALERTS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_HASH + " text not null, "
            + COLUMN_TITLE + " text, "
            + " text," + COLUMN_STARTDATE + " integer);";

    public CacheDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {

            database.execSQL(DATABASE_CREATE_ALERTS);

            database.execSQL("Create Index " + TABLE_ALERTS + "_" + COLUMN_HASH + "_idx ON "
                    + TABLE_ALERTS + "(" + COLUMN_HASH + ");");
            database.execSQL("Create Index " + TABLE_ALERTS + "_" + COLUMN_STARTDATE + "_idx ON "
                    + TABLE_ALERTS + "(" + COLUMN_STARTDATE + ");");

        } catch (Exception e) {
           if(C.LOG_MODE) C.logE(e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTS);

        onCreate(db);
    }
}
