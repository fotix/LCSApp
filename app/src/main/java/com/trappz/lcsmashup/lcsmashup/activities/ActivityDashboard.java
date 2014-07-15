package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.otto.Subscribe;
import com.trappz.lcsmashup.api.messages.EventBusManager;
import com.trappz.lcsmashup.api.models.Match.Match;
import com.trappz.lcsmashup.api.models.Match.PreviousGames;
import com.trappz.lcsmashup.api.models.Programming.ProgrammingBlock;
import com.trappz.lcsmashup.api.models.Programming.ProgrammingWeek;
import com.trappz.lcsmashup.api.responses.MatchResponseNotification;
import com.trappz.lcsmashup.api.responses.NewsResponseNotification;
import com.trappz.lcsmashup.api.responses.ProgrammingBlockResponseNotification;
import com.trappz.lcsmashup.api.responses.ProgrammingWeekResponseNotification;
import com.trappz.lcsmashup.api.services.ApiServices;
import com.trappz.lcsmashup.lcsmashup.C;
import com.trappz.lcsmashup.lcsmashup.R;
import com.trappz.lcsmashup.lcsmashup.adapters.AdapterNews;

import java.util.ArrayList;


public class ActivityDashboard extends SuperActivity {
    public static String TAG = "lcsactivity";
    private int offset = 0;
    boolean isLoading = false;
    AdapterNews adapter = null;
    ListView newsListview;
    public static ArrayList newsList = new ArrayList();
    AbsListView.OnScrollListener mScrollListener;
    SlidingMenu menu;

    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        EventBusManager.register(this);

        getActionBar().setHomeButtonEnabled(true);

        setupSlidingMenu();
//        ApiServices.getProgrammingBlock("1787");
//        ApiServices.getMatch("2352");



        newsList = new ArrayList();
        newsListview = (ListView) findViewById(R.id.newsList);

        newsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent i = new Intent(getApplicationContext(), ActivityNewsDetail.class);
                i.putExtra("index", position);
                startActivity(i);
            }
        });

        newsListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (isLoading == false) {
                        isLoading = true;
                        ApiServices.getNews(C.NEWS_PER_REQUEST, offset);
                    }
                }
            }
        });

        ApiServices.getNews(C.NEWS_PER_REQUEST, offset);

        adapter = new AdapterNews(getApplicationContext(), newsList);
        newsListview.setAdapter(adapter);
    }





    @Override
    public void onPause() {

        super.onPause();
        EventBusManager.unregister(this);
    }

    @Override
    public void onResume() {

        super.onResume();
        EventBusManager.register(this);
    }

    @Subscribe
    public void processResponseNewsNotification(NewsResponseNotification rn) {

        isLoading = false;
        offset = offset + 10;
        newsList.addAll(rn.data);
        adapter.notifyDataSetChanged();

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            this.toggleMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
