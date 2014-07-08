package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.ListView;

import com.squareup.otto.Subscribe;
import com.trappz.lcsmashup.api.messages.EventBusManager;
import com.trappz.lcsmashup.api.models.Match.Match;
import com.trappz.lcsmashup.api.models.Programming.ProgrammingBlock;
import com.trappz.lcsmashup.api.models.Programming.ProgrammingWeek;
import com.trappz.lcsmashup.api.responses.MatchResponseNotification;
import com.trappz.lcsmashup.api.responses.ProgrammingBlockResponseNotification;
import com.trappz.lcsmashup.api.responses.ProgrammingWeekResponseNotification;
import com.trappz.lcsmashup.api.services.ApiServices;
import com.trappz.lcsmashup.lcsmashup.R;
import com.trappz.lcsmashup.lcsmashup.adapters.AdapterSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Filipe Oliveira on 08-07-2014.
 *
 */
public class ActivitySchedule extends Activity {
    SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

    public String lastRequest = null;

    public static String TAG = "lcsSchedule";
    AdapterSchedule adapter;
    ListView lv;
    HashMap<String,ProgrammingBlock> BlockList;
    ArrayList<Match> MatchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        EventBusManager.register(this);

        BlockList = new HashMap<String, ProgrammingBlock>();
        MatchList = new ArrayList<Match>();

        lv = (ListView) findViewById(R.id.activity_schedule_lv);

        adapter = new AdapterSchedule(getApplicationContext(),MatchList);

        lv.setAdapter(adapter);

        Time t = new Time(Time.getCurrentTimezone());
        t.setToNow();
        String date = t.format("%d-%m-%Y");
        Log.e(TAG,"CURRENT DATE: "+date);

        ApiServices.getProgrammingWeek("02-07-2014", "0000");
    }


    @Subscribe
    public void processResponseProgrammingWeekNotification(ProgrammingWeekResponseNotification pwrn){

        ProgrammingWeek p = pwrn.data;

        if(p.getContainsMatch()){

            for(int i = 0 ; i < p.getDays().get(0).getBlockIds().size() ; i++){
                String aux = ApiServices.getProgrammingBlock(p.getDays().get(0).getBlockIds().get(i));
                BlockList.put(aux , new ProgrammingBlock());
            }
        }else {
            //show no matches layout
        }
    }

    @Subscribe
    public void processResponseProgrammingBlockNotification(ProgrammingBlockResponseNotification pbrn){
        ProgrammingBlock p = pbrn.data;

        if(p != null){
            Log.e(TAG, p.getLabel());
            BlockList.put(pbrn.requestId,p);

            for(int i = 0 ; i<p.getMatches().size() ; i++){
                lastRequest = ApiServices.getMatch(p.getMatches().get(i));

            }

        }
    }

    @Subscribe
    public void processResponseMatchNotification(MatchResponseNotification mrn){
        Match m = mrn.data;

        if(m != null){
            Log.w(TAG,m.getName());

           MatchList.add(m);
           Collections.sort(MatchList,new Comparator<Match>(){

                public int compare(Match thisMatch, Match another) {

                    try {
                        Date thisMatchDate;
                        Date anotherDate;

                        thisMatchDate = format.parse(thisMatch.getDateTime());
                        anotherDate = format.parse(another.getDateTime());

                        Log.i(TAG,"COMPARING: "+thisMatchDate.toString()+"     -    "+anotherDate.toString());

                        if(thisMatchDate.after(anotherDate))
                            return 1;

                        if(thisMatchDate.equals(anotherDate))
                            return 0;

                        if(thisMatchDate.before(anotherDate))
                            return -1;

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    return 0;
                }
           });


            adapter.notifyDataSetChanged();

        }else
            Log.w(TAG,"Match is null ....");
    }
}
