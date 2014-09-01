package com.twistedsin.app.lcsmashup.caching;

import com.twistedsin.app.lcsmashup.Base;

/**
 * Created by Filipe Oliveira on 01-09-2014.
 */
public class Cache {

    public static final String CID = "Cache";

    //CACHE DATA

    //NOTIFICATIONS
    public static AlertsDataSource alertsCache;

    public static void initCache(Base b){
        alertsCache = new AlertsDataSource(b);
        alertsCache.deleteOldAlerts();
    }
}
