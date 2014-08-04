package com.trappz.app.lcsmashup;

import android.util.Log;

/**
 * Created by Filipe Oliveira on 01-07-2014.
 */
public class C {

    public static final boolean DEBUG = true;

    public static final int NEWS_PER_REQUEST = 10;

    public static final String ICON_CHAMPION_URL = "http://lcsmashup-cdn.s3-eu-west-1.amazonaws.com/champions/";
    public static final String ICON_ITEMS_URL = "http://lcsmashup-cdn.s3-eu-west-1.amazonaws.com/items/";
    public static final String BASE_URL = "http://euw.lolesports.com";


    public static void Logger(String TAG, String Text) {
        if (DEBUG)
            Log.d(TAG, Text);
    }
}
