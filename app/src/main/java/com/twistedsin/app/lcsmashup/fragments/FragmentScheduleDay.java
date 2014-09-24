package com.twistedsin.app.lcsmashup.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Subscribe;
import com.twistedsin.app.api.messages.EventBusManager;
import com.twistedsin.app.api.models.Match.Match;
import com.twistedsin.app.api.models.Programming.ProgrammingBlock;
import com.twistedsin.app.api.models.Programming.ProgrammingWeek;
import com.twistedsin.app.api.responses.MatchResponseNotification;
import com.twistedsin.app.api.responses.ProgrammingBlockResponseNotification;
import com.twistedsin.app.api.responses.ProgrammingWeekResponseNotification;
import com.twistedsin.app.api.services.ApiServices;
import com.twistedsin.app.lcsmashup.Base;
import com.twistedsin.app.lcsmashup.C;
import com.twistedsin.app.lcsmashup.R;
import com.twistedsin.app.lcsmashup.Utils.TimeZones;
import com.twistedsin.app.lcsmashup.activities.ActivityGame;
import com.twistedsin.app.lcsmashup.activities.ActivitySchedule;
import com.twistedsin.app.lcsmashup.adapters.AdapterSchedule;
import com.twistedsin.app.lcsmashup.analytics.DataType;
import com.twistedsin.app.lcsmashup.analytics.GATracker;

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

    String currentTimeZone = "0000";
    TimeZones ctz = TimeZones.GMT;

    ListView lv;
    TextView noMatches;
    RelativeLayout loadingLayout;
    HashMap<String, ProgrammingBlock> BlockList;
    public static ArrayList<Match> MatchList;
    public static HashMap<String, Boolean> MatchIdList;

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

        setTimeZone(TimeZones.GMT);

        lv = (ListView) v.findViewById(R.id.activity_schedule_lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), ActivityGame.class);
                i.putExtra("gamenumber",1);
                i.putExtra("index", position);
                GATracker.getInstance().sendAnalyticsData(DataType.EVENT,getActivity().getBaseContext(),"ScheduleScreen",MatchList.get(position).getName(),null,null,"FragmentScheduleDay");
                startActivity(i);
            }
        });

        loadingLayout = (RelativeLayout) v.findViewById(R.id.fragment_schedule_loadinglayout);
        noMatches = (TextView) v.findViewById(R.id.fragment_schedule_day_nomatches);

        adapter = new AdapterSchedule(getActivity().getApplicationContext(), MatchList);

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

    public void setNewDate(String date) {

        MatchList.clear();
        MatchIdList.clear();
        loadingLayout.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();


//        loadingState.setLoading(true);
        ApiServices.getProgrammingWeek(date, currentTimeZone);

        if(C.LOG_MODE) C.logI("Programming week: " + date + " - " + currentTimeZone);

    }

    public void setTimeZone(TimeZones tz) {

        ctz = tz;

        switch (tz) {
            case PDT:
                currentTimeZone = "-0700";
                break;
            case EDT:
                currentTimeZone = "-0400";
                break;
            case GMT:
                currentTimeZone = "0000";
                break;
            case CEST:
                currentTimeZone = "0200";
                break;
            case IST:
                currentTimeZone = "0530";
                break;
            case KST:
                currentTimeZone = "0900";
                break;
            case AEST:
                currentTimeZone = "1100";
                break;
        }
    }

    public String getTimeZone(TimeZones t) {
        switch (t) {
            case PDT:
                return "-07:00";

            case EDT:
                return "-04:00";

            case GMT:
                return "00:00";

            case CEST:
                return "02:00";

            case IST:
                return "05:30";

            case KST:
                return "09:00";

            case AEST:
                return "11:00";

        }

        return "00:00";
    }

    @Subscribe
    public void processResponseProgrammingWeekNotification(ProgrammingWeekResponseNotification pwrn) {

        ProgrammingWeek p = pwrn.data;

        if (p.getDays().get(0).getBlockNum() > 0) {
            loadingLayout.setVisibility(View.VISIBLE);
            noMatches.setVisibility(View.INVISIBLE);



            for (int i = 0; i < p.getDays().get(0).getBlockIds().size(); i++) {
                String aux = ApiServices.getProgrammingBlock(p.getDays().get(0).getBlockIds().get(i));
                BlockList.put(aux, new ProgrammingBlock());
                if(C.LOG_MODE) C.logE("GETTING PROGRAMMING BLOCK "+p.getDays().get(0).getBlockIds().get(i));
            }
        } else {
            loadingLayout.setVisibility(View.INVISIBLE);
            noMatches.setVisibility(View.VISIBLE);
//            loadingState.setLoading(false);
        }
    }

    @Subscribe
    public void processResponseProgrammingBlockNotification(ProgrammingBlockResponseNotification pbrn) {
        ProgrammingBlock p = pbrn.data;

        if (p != null) {
            if(C.LOG_MODE) C.logE(p.getLabel());
            if(p.getTournamentId() == null || p.getTournamentId().equals(" "))
            {
                checkLoadingState();
                return;
            }

            if(!p.getMatches().isEmpty()) {
                BlockList.put(p.getTournamentId(), p);

                for (int i = 0; i < p.getMatches().size(); i++) {
                    lastRequest = ApiServices.getMatch(p.getMatches().get(i));
                    MatchIdList.put(lastRequest, false);
                }
            }
            else {
                if(C.LOG_MODE) C.logW("No matches");
                checkLoadingState();

            }

        }
    }

    private boolean sameDay(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    @Subscribe
    public void processResponseMatchNotification(MatchResponseNotification mrn) {
        Match m = mrn.data;
        Date matchDate = new Date();
        if(m.getDateTime().equals("0"))
        {
            MatchIdList.put(mrn.requestId, true);
            checkLoadingState();
            return;
        }

        try {
            if(C.LOG_MODE) C.logD("MATCH: "+m.getMatchId()+"   -   "+m.getDateTime());

            matchDate = format.parse(m.getDateTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(matchDate);

            String s = getTimeZone(ctz);

            calendar.add(Calendar.HOUR, Integer.valueOf(s.split(":")[0]));
            calendar.add(Calendar.MINUTE, Integer.valueOf(s.split(":")[1]));

            Date d = calendar.getTime();

            m.setDateTime(ActivitySchedule.getDateAsStringISO(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (!sameDay(matchDate, ActivitySchedule.currentDate)) {
            if(C.LOG_MODE) C.logI(m.getName() + " - " + m.getDateTime() + "   NOT the current date");
            MatchIdList.put(mrn.requestId, true);
            checkLoadingState();
            return;
        } else {
            if(C.LOG_MODE) C.logI(m.getName() + " - " + m.getDateTime() + "   IS the current date");
        }

/*        if(!ActivitySchedule.getDayOfWeek(matchDate).equals(ActivitySchedule.getDayOfWeek(ActivitySchedule.currentDate)))
        {
            return;
        }*/

        if (m != null) {
            if(C.LOG_MODE) C.logW( m.getName() + "    " + m.getMatchId() + "   " + m.getResult().get("game0").getId());

            if(BlockList.get(m.getTournament().getId()) != null)
                m.setColor(BlockList.get(m.getTournament().getId()).getLeagueColor());


            MatchList.add(m);
            MatchIdList.put(mrn.requestId, true);
            Collections.sort(MatchList, new Comparator<Match>() {

                public int compare(Match thisMatch, Match another) {

                    try {
                        Date thisMatchDate;
                        Date anotherDate;

                        thisMatchDate = format.parse(thisMatch.getDateTime());
                        anotherDate = format.parse(another.getDateTime());



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


        } else {
            MatchIdList.put(mrn.requestId, true);
            if(C.LOG_MODE) C.logW( "Match is null ....");
        }


        checkLoadingState();

    }


    public void checkLoadingState() {
        boolean endLoading = true;

        for (String requestID : MatchIdList.keySet()) {
            if (!MatchIdList.get(requestID))
                endLoading = false;
        }

        if (endLoading) {
            loadingLayout.setVisibility(View.INVISIBLE);
            if(MatchIdList.isEmpty())
                noMatches.setVisibility(View.VISIBLE);
//                loadingState.setLoading(false);
        }
    }

    public interface OnLoadingStateChanged {
        public void setLoading(boolean value);
    }
}
