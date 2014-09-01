package com.twistedsin.app.lcsmashup.caching;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.twistedsin.app.lcsmashup.C;
import com.twistedsin.app.lcsmashup.notifications.DataNotification;

import java.util.GregorianCalendar;

/**
 * Created by Filipe Oliveira on 01-09-2014.
 */
public class AlertsDataSource extends DataSource{

    public static String CID = "AlertsDataSource";
    SQLiteDatabase database;

    private String[] allColumns =
            { CacheDbHelper.COLUMN_ID, CacheDbHelper.COLUMN_HASH, CacheDbHelper.COLUMN_TITLE, CacheDbHelper.COLUMN_STARTDATE };

    public AlertsDataSource(Context context)
    {
        super(context);
    }

    public long createAlert(DataNotification alert)
    {
        ContentValues values = new ContentValues();
        values.put(CacheDbHelper.COLUMN_HASH, alert.hash);
        values.put(CacheDbHelper.COLUMN_TITLE, alert.title);
        values.put(CacheDbHelper.COLUMN_STARTDATE, alert.startDate.getTimeInMillis());

        return insert(CacheDbHelper.TABLE_ALERTS, null, values);
    }

    public void deleteOldAlerts()
    {
        delete(CacheDbHelper.TABLE_ALERTS, CacheDbHelper.COLUMN_STARTDATE + " < " + GregorianCalendar.getInstance().getTimeInMillis(), null);
    }

    public void deleteAlert(long id)
    {
        delete(CacheDbHelper.TABLE_ALERTS, CacheDbHelper.COLUMN_ID + " = " + id, null);
    }

    public DataNotification getAlertById(final String id)
    {
        final ResultHolder holder=new ResultHolder();

        DataSourceCursor dscursor=new DataSourceCursor()
        {

            @Override
            public void onCursor(Cursor cursor)
            {
                if (cursor.getCount() > 0)
                {
                    cursor.moveToFirst();
                    holder.result = cursorToAlert(cursor);
                }
            }

            @Override
            public void onError(Exception e)
            {
                // TODO Auto-generated method stub

            }
        };
        query(dscursor,CacheDbHelper.TABLE_ALERTS, allColumns, CacheDbHelper.COLUMN_ID + " = '" + id + "' LIMIT 1", null, null, null, null);

        return (DataNotification) holder.result;
    }

    public DataNotification getAlertByHash(String hash)
    {

        final ResultHolder holder=new ResultHolder();

        DataSourceCursor dscursor=new DataSourceCursor()
        {

            @Override
            public void onCursor(Cursor cursor)
            {
                if (cursor.getCount() > 0)
                {
                    cursor.moveToFirst();
                    holder.result = cursorToAlert(cursor);
                }
            }

            @Override
            public void onError(Exception e)
            {
                // TODO Auto-generated method stub

            }
        };

        query(dscursor,CacheDbHelper.TABLE_ALERTS, allColumns, CacheDbHelper.COLUMN_HASH + " = '" + hash + "' LIMIT 1", null, null, null, null);

        return (DataNotification) holder.result;
    }

    private static DataNotification cursorToAlert(Cursor cursor)
    {
        DataNotification alert = new DataNotification();
        try
        {
            alert.id = cursor.getLong(0);
            alert.hash = cursor.getString(1);
            alert.title = cursor.getString(2);
            alert.startDate = (new GregorianCalendar());
        }
        catch (Exception e)
        {
            if(C.LOG_MODE) C.logE(e.getMessage());
        }
        return alert;
    }
}
