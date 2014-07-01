package com.trappz.lcsmashup.lcsmashup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.trappz.lcsmashup.api.messages.EventBusManager;
import com.trappz.lcsmashup.api.responses.NewsResponseNotification;
import com.trappz.lcsmashup.api.services.ApiServices;
import com.trappz.lcsmashup.lcsmashup.adapters.AdapterNews;


public class ActivityDashboard extends Activity {
    public static String TAG = "lcsactivity";
    private int offset=0;

    AdapterNews adapter = null;
    ListView newsListview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        EventBusManager.register(this);

        newsListview = (ListView) findViewById(R.id.newsList);
        ApiServices.getNews(C.NEWS_PER_REQUEST,offset);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_dashboard, menu);
        return true;
    }

    @Subscribe
    public void processResponseNotification(NewsResponseNotification rn) {

        offset = offset+10;
        adapter = new AdapterNews(getApplicationContext(),rn.data);
        newsListview.setAdapter(adapter);

        Log.d(TAG,"Got a response notification");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
