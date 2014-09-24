package com.twistedsin.app.lcsmashup.analyticsdata;


import java.util.ArrayList;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.google.android.gms.analytics.HitBuilders.EventBuilder;

import com.twistedsin.app.lcsmashup.C;


import android.content.Context;
import android.util.Log;

/**
 * Created by Filipe Oliveira on 23-09-2014.
 */
public class GATracker {



    private static GATracker instance = null;
    private static String trackerID = "UA-54690253-1";
    protected GATracker(){

    }

    public static GATracker getInstance(){
        if(instance  == null){
            instance = new GATracker();
        }
        return instance;

    }

    /**
     * This method should be used to send Events without secondary dimensions
     * @param type
     * @param ctx
     * @param category
     * @param action
     * @param label
     * @param value
     * @param screenName name
     */
    public void sendAnalyticsData(DataType type,Context ctx,String category, String action,String label,Long value,String screenName){
        sendAnalyticsData(type, ctx, category, action, label, value,screenName,null);
    }

    /**
     * This method should be used to send Events WITH secondary dimensions using an ArrayList filled with Tuple(DimensionIndex,Value)
     * @param type The data type
     * @param ctx  The application context
     * @param category
     * @param action
     * @param label
     * @param value
     * @param screenName
     * @param secondaryDimensions  HashMap with the secondary dimension as the Key value and Value represented as a String with the actual value to send
     */
    public void sendAnalyticsData(DataType type,Context ctx,String category, String action,String label,Long value,String screenName,ArrayList<Tuple> secondaryDimensions){
        if(secondaryDimensions != null){
            for(int i = 0; i<secondaryDimensions.size(); i++){
               if(C.LOG_MODE) C.logD("Analytics","LABEL: "+label+"SEC DIMENSION; "+secondaryDimensions.get(0).value);
            }
        }
        sendEvent(ctx, category, action, label, value,screenName,secondaryDimensions);
    }

    /**
     * This method should be used to send screen events
     * @param type 		Data Type
     * @param ctx 	Application context
     * @param screenName String with the identification of the screen
     *
     */
    public void sendAnalyticsData(DataType type,Context ctx,String screenName){
        sendScreen(ctx, screenName);
    }


    private void sendEvent(Context ctx,String category, String action,String label,Long value,String screenName,ArrayList<Tuple> secDimension){
        Log.d("Analytics","Sending event: Category: "+category+"  Action: "+action+"  Label: "+label);

        Tracker t = GoogleAnalytics.getInstance(ctx).newTracker(trackerID);
        t.setScreenName(screenName);



        if(secDimension != null){

            EventBuilder eb =  new HitBuilders.EventBuilder();
            eb.setCategory(category)
                    .setAction(action)
                    .setLabel(label);


            for(int i = 0; i < secDimension.size() ; i++){
                eb.setCustomDimension(secDimension.get(0).index, secDimension.get(0).value);
            }


            t.send(eb.build());


        }else{
            t.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .setLabel(label)
                    .build());
        }
    }

    private void sendScreen(Context ctx,String screenName){

        Log.d("Analytics","Sending screen: "+screenName);
        Tracker t = GoogleAnalytics.getInstance(ctx).newTracker(trackerID);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
