package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import com.trappz.lcsmashup.lcsmashup.R;

/**
 * Created by Filipe Oliveira on 31-07-2014.
 */
public class ActivityAbout extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar ab = getActionBar();

        ab.setTitle("About");
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
