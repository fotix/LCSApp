package com.twistedsin.app.lcsmashup.caching;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.twistedsin.app.lcsmashup.C;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Filipe Oliveira on 01-09-2014.
 */
public class DataSource
{
    static final String CID = "DataSource";

    static private CacheDbHelper dbHelper;
    static private SQLiteDatabase database;

    static private final Lock mutex = new ReentrantLock(true);

    public DataSource(Context context)
    {
        if (dbHelper == null)
        {
            dbHelper = new CacheDbHelper(context);
        }
    }

    protected void open() throws SQLException
    {
        mutex.lock();

        if (database != null)
        {
            if (database.isOpen())
            {
                database.acquireReference();
            }
            else
            {
                database = null;
            }
        }

        if (database == null)
        {
            database = dbHelper.getWritableDatabase();
            database.acquireReference();
        }
    }

    void close()
    {
        try
        {
            dbHelper.close();

            mutex.unlock();
        }
        catch (Exception e)
        {
            if(C.LOG_MODE) C.logE(e.getMessage());
        }

    }

    public long insert(String table, String nullColumnHack, ContentValues values)
    {
        open();

        long id = -1;
        try
        {
            id = database.insert(table, nullColumnHack, values);
        }
        catch (Exception e)
        {
            if(C.LOG_MODE) C.logE(e.getMessage());
        }

        close();

        return id;
    }

    public int delete(String table, String whereClause, String[] whereArgs)
    {
        open();

        int deleted = 0;

        try
        {
            deleted = database.delete(table, whereClause, whereArgs);
        }
        catch (Exception e)
        {
            if(C.LOG_MODE) C.logE(e.getMessage());
        }

        close();

        return deleted;

    }

    public void query(DataSourceCursor dscursor, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
    {

        open();

        Cursor cursor = null;

        try
        {
            cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            dscursor.onCursor(cursor);
        }
        catch (Exception e)
        {
            if(C.LOG_MODE) C.logE(e.getMessage());
            dscursor.onError(e);
        }

        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }

        close();

    }

    public void executeTransaction(Runnable runnable)
    {
        open();

        try
        {
            database.beginTransaction();
            runnable.run();
            database.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            if(C.LOG_MODE) C.logE(e.getMessage());
        }

        try
        {
            database.endTransaction();
        }
        catch(Exception e)
        {
            if(C.LOG_MODE) C.logE(e.getMessage());
        }

        close();

    }

    public DatabaseUtils.InsertHelper getInsertHelper(String table)
    {
        return new DatabaseUtils.InsertHelper(database, table);
    }

    public static interface DataSourceCursor
    {
        public void onCursor(Cursor cursor);

        public void onError(Exception e);
    }

    public static class ResultHolder
    {
        public Object result;
    }
}

