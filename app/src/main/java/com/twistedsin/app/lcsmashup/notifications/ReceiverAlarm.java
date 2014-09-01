package com.twistedsin.app.lcsmashup.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.twistedsin.app.lcsmashup.R;
import com.twistedsin.app.lcsmashup.activities.ActivityDashboard;
import com.twistedsin.app.lcsmashup.caching.Cache;

/**
 * Created by Filipe Oliveira on 01-09-2014.
 */
public class ReceiverAlarm extends BroadcastReceiver {

    NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Cache.alertsCache != null)
            Cache.alertsCache.deleteOldAlerts();

        String title = intent.getStringExtra("title");


        Intent liveStreamIntent = new Intent(context, ActivityDashboard.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, liveStreamIntent, 0);

        Notification notif = new Notification(R.drawable.image32, title, System.currentTimeMillis());

        notif.flags |= Notification.FLAG_AUTO_CANCEL;

        notif.setLatestEventInfo(context, title, "This Game is about to start !", contentIntent);
        nm.notify((int) Math.random(), notif);

    }
}
