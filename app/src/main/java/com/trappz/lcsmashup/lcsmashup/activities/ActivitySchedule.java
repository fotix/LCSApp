package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;

import com.trappz.lcsmashup.lcsmashup.R;
import com.trappz.lcsmashup.lcsmashup.fragments.FragmentScheduleBar;
import com.trappz.lcsmashup.lcsmashup.fragments.FragmentScheduleDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hirondelle.date4j.DateTime;

/**
 * Created by Filipe Oliveira on 08-07-2014.
 *
 */
public class ActivitySchedule extends Activity implements FragmentScheduleBar.OnDateChangedListener {
    FragmentScheduleDay fDay;
    FragmentScheduleBar fBar;


    Date currentDate;
    Calendar cal;

    public static final String TAG= "ScheduleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        fDay = (FragmentScheduleDay) getFragmentManager().findFragmentById(R.id.fragment_list);

        fBar = (FragmentScheduleBar) getFragmentManager().findFragmentById(R.id.fragment_bar);


//
//        TimeZone tz = TimeZone.getTimeZone("GMT");
//
//        format.setTimeZone(tz);
//        String date = format.format(new Date());

        cal = Calendar.getInstance();
        currentDate = new Date();

        Log.e(TAG, "CURRENT DATE: " + getDateAsString(currentDate));

        fDay.setNewDate(getDateAsString(currentDate));
        fBar.refreshDate(getDateAsString(currentDate),getDayOfWeek(currentDate));
    }

    @Override
    public void onNextDay() {
        cal.add(Calendar.DAY_OF_MONTH,1);
        currentDate = cal.getTime();

        String newDate = getDateAsString(currentDate);
        Log.i(TAG,"NEW DATE: "+newDate);
        Log.i(TAG,"NEW DATE ISO: "+getDateAsStringISO(currentDate));

        fBar.refreshDate(newDate,getDayOfWeek(currentDate));
        fDay.setNewDate(newDate);
    }

    @Override
    public void onPreviousDay() {
        cal.add(Calendar.DAY_OF_MONTH,-1);
        currentDate = cal.getTime();
        String newDate = getDateAsString(currentDate);

        Log.i(TAG,"NEW DATE: "+newDate);
        Log.i(TAG,"NEW DATE ISO: "+getDateAsStringISO(currentDate));

        fBar.refreshDate(newDate,getDayOfWeek(currentDate));
        fDay.setNewDate(newDate);
    }

    public static String getDateAsString(Date d){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(d);
        return date;
    }

    public static String getDateAsStringISO(Date d){
        SimpleDateFormat formatService = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        String date = formatService.format(d);
        return date;
    }

    public static String getDayOfWeek(Date d) {
        SimpleDateFormat formater= new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
        String dayOfWeek = formater.format(d);
        return dayOfWeek;
    }
}
