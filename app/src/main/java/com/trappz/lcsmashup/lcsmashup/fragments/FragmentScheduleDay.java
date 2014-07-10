package com.trappz.lcsmashup.lcsmashup.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.trappz.lcsmashup.lcsmashup.activities.ActivityGame;
import com.trappz.lcsmashup.lcsmashup.adapters.AdapterSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Filipe Oliveira on 09-07-2014.
 */
public class FragmentScheduleDay extends Fragment {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

    public String lastRequest = null;

    public static String TAG = "lcsSchedule";
    public static AdapterSchedule adapter;
    ListView lv;
    TextView noMatches;
    HashMap<String,ProgrammingBlock> BlockList;
    public static ArrayList<Match> MatchList;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_schedule_day, container, false);

        BlockList = new HashMap<String, ProgrammingBlock>();
        MatchList = new ArrayList<Match>();

        lv = (ListView) v.findViewById(R.id.activity_schedule_lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), ActivityGame.class);

                i.putExtra("index",position);

                startActivity(i);
            }
        });


        noMatches = (TextView) v.findViewById(R.id.fragment_schedule_day_nomatches);

        adapter = new AdapterSchedule(getActivity().getApplicationContext(),MatchList);

        lv.setAdapter(adapter);

        return v;
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

    public void setNewDate(String date){

        MatchList.clear();
        adapter.notifyDataSetChanged();

        ApiServices.getProgrammingWeek(date, "0000");

    }


    @Subscribe
    public void processResponseProgrammingWeekNotification(ProgrammingWeekResponseNotification pwrn){

        ProgrammingWeek p = pwrn.data;

        if(p.getDays().get(0).getBlockNum() > 0){
            noMatches.setVisibility(View.INVISIBLE);
            for(int i = 0 ; i < p.getDays().get(0).getBlockIds().size() ; i++){
                String aux = ApiServices.getProgrammingBlock(p.getDays().get(0).getBlockIds().get(i));
                BlockList.put(aux , new ProgrammingBlock());
            }
        }else {
            noMatches.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void processResponseProgrammingBlockNotification(ProgrammingBlockResponseNotification pbrn){
        ProgrammingBlock p = pbrn.data;

        if(p != null){
            Log.e(TAG, p.getLabel());
            BlockList.put(p.getTournamentId(),p);

            for(int i = 0 ; i<p.getMatches().size() ; i++){
                lastRequest = ApiServices.getMatch(p.getMatches().get(i));

            }

        }
    }

    @Subscribe
    public void processResponseMatchNotification(MatchResponseNotification mrn){
        Match m = mrn.data;

        if(m != null){
            Log.w(TAG,m.getName() +"    "+m.getMatchId()+"   "+m.getResult().get("game0").getId());

            m.setColor(BlockList.get(m.getTournament().getId()).getLeagueColor());


            MatchList.add(m);
            Collections.sort(MatchList, new Comparator<Match>() {

                public int compare(Match thisMatch, Match another) {

                    try {
                        Date thisMatchDate;
                        Date anotherDate;

                        thisMatchDate = format.parse(thisMatch.getDateTime());
                        anotherDate = format.parse(another.getDateTime());

                        Log.i(TAG, "COMPARING: " + thisMatchDate.toString() + "     -    " + anotherDate.toString());

                        if (thisMatchDate.after(anotherDate))
                            return 1;

                        if (thisMatchDate.equals(anotherDate))
                            return 0;

                        if (thisMatchDate.before(anotherDate))
                            return -1;

                    } catch (ParseException e) {
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
