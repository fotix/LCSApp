package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.trappz.lcsmashup.api.models.Match.Match;
import com.trappz.lcsmashup.lcsmashup.R;
import com.trappz.lcsmashup.lcsmashup.fragments.FragmentScheduleDay;

/**
 * Created by Filipe Oliveira on 10-07-2014.
 */
public class ActivityGame extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        int value = getIntent().getExtras().getInt("index");

        Match m = FragmentScheduleDay.MatchList.get(value);

        Log.e("LCSGAME",m.getName());
    }
}
