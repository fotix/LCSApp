package com.twistedsin.app.lcsmashup.activities;

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.twistedsin.app.api.callbacks.MatchCallback;
import com.twistedsin.app.lcsmashup.C;
import com.twistedsin.app.lcsmashup.R;

import retrofit.RetrofitError;

/**
 * Created by Filipe Oliveira on 31-07-2014.
 */
public class ActivityAbout extends BaseActivity {

    TextView rateThisApp;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        context = getApplicationContext();
        ActionBar ab = getActionBar();

        ab.setTitle("About");

        rateThisApp = (TextView) findViewById(R.id.rate_this_app);

        /**
         * GOOGLE PLAY - RATE ME
         */
//        rateThisApp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
//                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                goToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                try {
//                    context.startActivity(goToMarket);
//                } catch (ActivityNotFoundException e) {
//                    C.logE("Error opening Market");
//                }
//            }
//        });

        /**
         * AMAZON STORE - RATE ME
         */

          rateThisApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.amazon.com/gp/mas/dl/android?p="+context.getPackageName()+"&showAll=1");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    C.logE("Error opening Market");
                }
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected int getSelfNavDrawerItem() {

        return NAVDRAWER_ITEM_ABOUT;
    }


}
