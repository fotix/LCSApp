package com.twistedsin.app.lcsmashup;

import android.app.Application;
import android.content.Context;

import com.twistedsin.app.lcsmashup.caching.Cache;

/**
 * Created by Filipe Oliveira on 01-09-2014.
 */
public class Base extends Application {

    public static final String CID = "Base";
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();

    }

    static boolean inited=false;
    public static boolean isInitialized()
    {
        return inited;
    }

    public static void initializeInfrastructure(final Context context,boolean silent) {

        final Base base=(Base) context.getApplicationContext();

            base.initializeInfrastructure(context);

    }


    public void initializeInfrastructure(Context context) {

        Cache.initCache(Base.this);
        inited=true;
    }

  }
