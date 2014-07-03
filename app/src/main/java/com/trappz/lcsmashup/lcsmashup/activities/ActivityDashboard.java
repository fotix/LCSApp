package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.trappz.lcsmashup.api.messages.EventBusManager;
import com.trappz.lcsmashup.api.responses.NewsResponseNotification;
import com.trappz.lcsmashup.api.services.ApiServices;
import com.trappz.lcsmashup.lcsmashup.C;
import com.trappz.lcsmashup.lcsmashup.R;
import com.trappz.lcsmashup.lcsmashup.adapters.AdapterNews;

import java.util.ArrayList;


public class ActivityDashboard extends Activity {
    public static String TAG = "lcsactivity";
    private int offset=0;
    boolean isLoading = false;
    AdapterNews adapter = null;
    ListView newsListview;
    public static ArrayList newsList = new ArrayList();
    AbsListView.OnScrollListener mScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        EventBusManager.register(this);


        newsList = new ArrayList();
        newsListview = (ListView) findViewById(R.id.newsList);

        newsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), ActivityNewsDetail.class);
                i.putExtra("index",position);
                startActivity(i);
            }
        });

        newsListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(isLoading == false)
                    {
                        isLoading = true;
                        ApiServices.getNews(C.NEWS_PER_REQUEST,offset);
                    }
                }
            }
        });

        ApiServices.getNews(C.NEWS_PER_REQUEST,offset);

        adapter = new AdapterNews(getApplicationContext(),newsList);
        newsListview.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_dashboard, menu);
        return true;
    }

    @Subscribe
    public void processResponseNotification(NewsResponseNotification rn) {

        isLoading = false;
        offset = offset+10;
        newsList.addAll(rn.data);
        adapter.notifyDataSetChanged();

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
