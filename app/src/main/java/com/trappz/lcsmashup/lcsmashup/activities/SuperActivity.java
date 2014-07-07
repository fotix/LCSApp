package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.Activity;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.trappz.lcsmashup.lcsmashup.R;

/**
 * Created by Filipe Oliveira on 07-07-2014.
 */
public abstract class SuperActivity extends Activity{

    SlidingMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setupSlidingMenu();

    }


    void setupSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
        // menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        menu.setFadeDegree(0.35f);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.sliding_menu);
    }

}
