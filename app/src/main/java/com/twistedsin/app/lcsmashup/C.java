package com.twistedsin.app.lcsmashup;

import android.util.Log;

/**
 * Created by Filipe Oliveira on 01-07-2014.
 */
public class C {

    /**
     * Logging Constants
     */
    public static boolean LOG_MODE = true;
    public static final boolean LOG_MODE_EXCEPTIONS = true;
    public static final String TAG = "LCSMashup";

    public static final int NEWS_PER_REQUEST = 10;
    public static final int TWEETS_PER_REQUEST = 20;

    public static final String ICON_CHAMPION_URL = "http://lcsmashup-cdn.s3-eu-west-1.amazonaws.com/champions/";
    public static final String ICON_ITEMS_URL = "http://lcsmashup-cdn.s3-eu-west-1.amazonaws.com/items/";
    public static final String BASE_URL = "http://euw.lolesports.com";


    public static final String ALARM_NAME = "com.twistedsin.app.lcsmashup.ALERT";
    /* SETTINGS */
    public static boolean spoilers = true;


    /**
     * Log e.
     *
     * @param module
     *           the module
     * @param msg
     *           the msg
     */
    public static void logE(String module, String msg) {
        if (LOG_MODE) {
            if (msg!=null)
            {
                Log.e(TAG,getLogClass()+msg);
            }

        }
    }

    public static void logE(String msg) {
        if (LOG_MODE) {
            if (msg!=null)
            {
                Log.e(TAG,getLogClass()+msg);
            }

        }
    }


    public static void logE(Throwable th) {
        if (LOG_MODE) {
            if (th!=null)
            {
                Log.e(TAG,getLogClass()+th.getCause()+" "+th.getMessage());
                Log.w(TAG,th);
            }

        }
    }

    /**
     * Log w.
     *
     * @param module
     *           the module
     * @param msg
     *           the msg
     */

    public static void logW(String module, String msg) {
        if (LOG_MODE) {
            Log.d(TAG,getLogClass()+msg);
        }
    }

    public static void logW(String msg) {
        if (LOG_MODE) {
            Log.d(TAG,getLogClass()+msg);
        }
    }



    /**
     * Log d.
     *
     * @param module
     *           the module
     * @param msg
     *           the msg
     */
    public static void logD(String module, String msg) {
        if (LOG_MODE) {
            if (msg!=null)
            {
                Log.d(TAG,getLogClass()+msg);
            }
        }
    }

    public static void logD(String msg) {
        if (LOG_MODE)
        {
            if (msg!=null)
            {
                Log.d(TAG,getLogClass()+msg);
            }
        }
    }


    /**
     * Log i.
     *
     * @param module
     *           the module
     * @param msg
     *           the msg
     */
    public static void logI(String module, String msg) {
        if (LOG_MODE) {
            if (msg!=null)
            {
                Log.i(TAG,getLogClass()+msg);
            }
        }
    }

    public static void logI(String msg) {
        if (LOG_MODE)
        {
            if (msg!=null)
            {
                Log.i(TAG,getLogClass()+msg);
            }
        }
    }



    /**
     * Log exception.
     *
     * @param module
     *           the module
     * @param e
     *           the e
     */
    public static void logException(String module, Exception e) {
        if (LOG_MODE && LOG_MODE_EXCEPTIONS) {
            Log.e(TAG,getLogClass()+e.getCause()+" "+e.getMessage());
            e.printStackTrace();
        }
    }


    static String getLogClass()
    {
        try
        {
            StackTraceElement[] str = Thread.currentThread().getStackTrace();

            StackTraceElement callingstr = str[4];

            String cls = callingstr.getClassName();
            String method=str[5].getMethodName();
            int num = callingstr.getLineNumber();

            return cls + ":"+ method+":"+ num + " ";
        }
        catch (Exception e)
        {

        }

        return "";

    }
}
