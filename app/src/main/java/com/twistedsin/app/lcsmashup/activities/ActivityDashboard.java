package com.twistedsin.app.lcsmashup.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.otto.Subscribe;
import com.twistedsin.app.api.messages.EventBusManager;
import com.twistedsin.app.api.models.News.News;
import com.twistedsin.app.api.responses.NewsResponseNotification;
import com.twistedsin.app.api.services.ApiServices;
import com.twistedsin.app.lcsmashup.C;
import com.twistedsin.app.lcsmashup.R;
import com.twistedsin.app.lcsmashup.adapters.AdapterNews;

import java.util.ArrayList;


public class ActivityDashboard extends BaseActivity {
    public static String TAG = "lcsactivity";
    private int offset = 0;
    boolean isLoading = false;
    AdapterNews adapter = null;
    ListView newsListview;
    public static ArrayList<News> newsList = new ArrayList<News>();
    AbsListView.OnScrollListener mScrollListener;

    RelativeLayout loadingLayout,noWifiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard);

        EventBusManager.register(this);

//        getLPreviewUtils().trySetActionBar();

        ActionBar ab = getActionBar();

        ab.setTitle(getString(R.string.app_name));
        noWifiLayout = (RelativeLayout) findViewById(R.id.activity_dashboard_nowifi);

          if(isNetworkAvailable(getApplicationContext()))
            noWifiLayout.setVisibility(View.GONE);

//        getActionBar().setHomeButtonEnabled(true);

//        setupSlidingMenu();
//        ApiServices.getProgrammingBlock("1787");
//        ApiServices.getMatch("2352");

        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        loadingLayout = (RelativeLayout) findViewById(R.id.activity_dashboard_loadinglayout);
        newsList = new ArrayList();
        newsListview = (ListView) findViewById(R.id.newsList);

        newsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;
                if (newsList.get(position).getYoutubeID() != null) {
                    i = new Intent(getApplicationContext(), ActivityNewsVideo.class);
                } else {
                    i = new Intent(getApplicationContext(), ActivityNewsDetail.class);
                }
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected int getSelfNavDrawerItem() {

        return NAVDRAWER_ITEM_NEWS;
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

        if(!isNetworkAvailable(getApplicationContext()) && noWifiLayout != null)
            noWifiLayout.setVisibility(View.VISIBLE);
        else if(noWifiLayout != null)
            noWifiLayout.setVisibility(View.GONE);


    }

    @Subscribe
    public void processResponseNewsNotification(NewsResponseNotification rn) {

        isLoading = false;
        offset = offset + 10;
        for (int i = 0; i < rn.data.size(); i++) {
            if (!rn.data.get(i).getTaxonomyId().equalsIgnoreCase("36"))
                newsList.add(rn.data.get(i));
        }
//        newsList.addAll(rn.data);
        adapter.notifyDataSetChanged();

        loadingLayout.setVisibility(View.GONE);

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//
////            if (super.menu.isMenuShowing()) {
////                super.menu.toggle();
////                return true;
////            } else {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("Are you sure you want to exit?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                ActivityDashboard.this.finish();
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alert = builder.create();
//                alert.show();
//
//            }
//
//
////        }
//
//        return super.onKeyDown(keyCode, event);
//    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
////        if (id == android.R.id.home) {
//////            this.toggleMenu();
////            return true;
////        }
//        return super.onOptionsItemSelected(item);
//    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
