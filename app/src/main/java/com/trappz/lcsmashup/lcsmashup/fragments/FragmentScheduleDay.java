package com.trappz.lcsmashup.lcsmashup.fragments;


import android.app.Activity;
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
import android.widget.RelativeLayout;
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
import com.trappz.lcsmashup.lcsmashup.activities.ActivitySchedule;
import com.trappz.lcsmashup.lcsmashup.adapters.AdapterSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    OnLoadingStateChanged loadingState;

    public static String TAG = "lcsSchedule";
    public static AdapterSchedule adapter;
    ListView lv;
    TextView noMatches;
    RelativeLayout loadingLayout;
    HashMap<String,ProgrammingBlock> BlockList;
    public static ArrayList<Match> MatchList;
    public static HashMap<String,Boolean> MatchIdList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            loadingState = (OnLoadingStateChanged) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoadingStateChanged");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_schedule_day, container, false);

        BlockList = new HashMap<String, ProgrammingBlock>();
        MatchList = new ArrayList<Match>();
        MatchIdList = new HashMap<String, Boolean>();


        lv = (ListView) v.findViewById(R.id.activity_schedule_lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), ActivityGame.class);

                i.putExtra("index",position);

                startActivity(i);
            }
        });

        loadingLayout = (RelativeLayout) v.findViewById(R.id.fragment_schedule_loadinglayout);
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
        MatchIdList.clear();
        loadingLayout.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

//        loadingState.setLoading(true);
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
            loadingLayout.setVisibility(View.INVISIBLE);
            noMatches.setVisibility(View.VISIBLE);
//            loadingState.setLoading(false);
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
                MatchIdList.put(lastRequest,false);
            }

        }
    }
    private boolean sameDay(Date d1, Date d2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
       return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
    @Subscribe
    public void processResponseMatchNotification(MatchResponseNotification mrn){
        Match m = mrn.data;
        Date matchDate = new Date();
        try {
             matchDate = format.parse(m.getDateTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(!sameDay(matchDate,ActivitySchedule.currentDate)){
            Log.i(TAG, m.getName() + " - " + m.getDateTime() + "   NOT the current date");
            MatchIdList.put(mrn.requestId,true);

            checkLoadingState();

            return;
        }else
        {
            Log.i(TAG,m.getName()+" - "+m.getDateTime()+"   IS the current date");
        }

/*        if(!ActivitySchedule.getDayOfWeek(matchDate).equals(ActivitySchedule.getDayOfWeek(ActivitySchedule.currentDate)))
        {
            return;
        }*/

        if(m != null){
            Log.w(TAG,m.getName() +"    "+m.getMatchId()+"   "+m.getResult().get("game0").getId());

            m.setColor(BlockList.get(m.getTournament().getId()).getLeagueColor());


            MatchList.add(m);
            MatchIdList.put(mrn.requestId,true);
            Collections.sort(MatchList, new Comparator<Match>() {

                public int compare(Match thisMatch, Match another) {

                    try {
                        Date thisMatchDate;
                        Date anotherDate;

                        thisMatchDate = format.parse(thisMatch.getDateTime());
                        anotherDate = format.parse(another.getDateTime());

//                        Log.i(TAG, "COMPARING: " + thisMatchDate.toString() + "     -    " + anotherDate.toString());

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



        }else {
            MatchIdList.put(mrn.requestId,true);
            Log.w(TAG, "Match is null ....");
        }


        checkLoadingState();

    }


    public void checkLoadingState(){
        boolean endLoading = true;

        for(String requestID: MatchIdList.keySet()){
            if(!MatchIdList.get(requestID))
                endLoading = false;
        }

        if(endLoading){
            loadingLayout.setVisibility(View.INVISIBLE);
//                loadingState.setLoading(false);
        }
    }

    public interface OnLoadingStateChanged{
        public void setLoading(boolean value);
    }
}
