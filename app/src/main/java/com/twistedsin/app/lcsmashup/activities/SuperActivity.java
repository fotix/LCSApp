package com.twistedsin.app.lcsmashup.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.twistedsin.app.lcsmashup.R;

/**
 * Created by Filipe Oliveira on 07-07-2014.
 */
public abstract class SuperActivity extends Activity {

    SlidingMenu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        menu = new SlidingMenu(this);
    }


    public void setupSlidingMenu() {

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


        View newslayout = findViewById(R.id.sliding_menu_news);
        newslayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                menu.toggle();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), ActivityDashboard.class);

                        startActivity(i);
                        finish();
                    }
                }, 400);

            }

        });


        View ll = findViewById(R.id.sliding_menu_schedule);
        ll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                menu.toggle();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), ActivitySchedule.class);

                        startActivity(i);


                    }
                }, 400);

            }

        });
    }



    public void toggleMenu() {
        menu.toggle();
    }

    public interface onLoadingStateChange {
        public void setLoading(boolean value);
    }

}
