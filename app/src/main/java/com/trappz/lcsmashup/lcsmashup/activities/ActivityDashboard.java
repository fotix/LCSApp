package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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


public class ActivityDashboard extends Activity {
    public static String TAG = "lcsactivity";
    private int offset=0;
    boolean isLoading = false;
    AdapterNews adapter = null;
    ListView newsListview;
    public static ArrayList newsList = new ArrayList();
    AbsListView.OnScrollListener mScrollListener;
    SlidingMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        EventBusManager.register(this);

        if(getActionBar() != null)
            getActionBar().setHomeButtonEnabled(true);

        setupSlidingMenu();


        ApiServices.getProgrammingBlock("1787");
        ApiServices.getMatch("2352");

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

    void setupSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
        // menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        menu.setFadeDegree(0.35f);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.sliding_menu);

        View tvLayout = findViewById(R.id.sliding_menu_news);
        tvLayout.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                menu.toggle();

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            Intent i = new Intent(getApplicationContext(), ActivityDashboard.class);

                            startActivity(i);
                            finish();

                        }
                    }, 400);

                }

        });


        View ll = findViewById(R.id.sliding_menu_wishlist);
        ll.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                menu.toggle();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        Intent i = new Intent(getApplicationContext(), ActivitySchedule.class);

                        startActivity(i);


                    }
                }, 400);

            }

        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_dashboard, menu);
        return true;
    }

    @Subscribe
    public void processResponseNewsNotification(NewsResponseNotification rn) {

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

        if(id == android.R.id.home)
        {
            menu.toggle();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
