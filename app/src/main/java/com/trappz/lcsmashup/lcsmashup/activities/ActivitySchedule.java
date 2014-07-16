package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.trappz.lcsmashup.lcsmashup.R;
import com.trappz.lcsmashup.lcsmashup.Utils.TimeZones;
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
public class ActivitySchedule extends SuperActivity implements FragmentScheduleBar.OnDateChangedListener, FragmentScheduleDay.OnLoadingStateChanged{
    FragmentScheduleDay fDay;
    FragmentScheduleBar fBar;


    public static Date currentDate;
    private static Calendar cal = null;

    public static final String TAG= "ScheduleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        fDay = (FragmentScheduleDay) getFragmentManager().findFragmentById(R.id.fragment_list);

        fBar = (FragmentScheduleBar) getFragmentManager().findFragmentById(R.id.fragment_bar);

        if(getActionBar() != null)
            getActionBar().setHomeButtonEnabled(true);

//        setupSlidingMenu();


        if(cal == null){
            cal = Calendar.getInstance();
            Log.i(TAG,"Creating new Calendar instance");
        }

        if(currentDate == null) {
            currentDate = new Date();
        }
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


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_schedule, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                this.toggleMenu();
                return true;

            case R.id.tz_pdt:
                item.setChecked(true);
                fDay.setTimeZone(TimeZones.PDT);
                break;
            case R.id.tz_edt:
                item.setChecked(true);
                fDay.setTimeZone(TimeZones.EDT);
                break;
            case R.id.tz_gmt:
                item.setChecked(true);
                fDay.setTimeZone(TimeZones.GMT);
                break;
            case R.id.tz_cest:
                item.setChecked(true);
                fDay.setTimeZone(TimeZones.CEST);
                break;
            case R.id.tz_ist:
                item.setChecked(true);
                fDay.setTimeZone(TimeZones.IST);
                break;
            case R.id.tz_kst:
                item.setChecked(true);
                fDay.setTimeZone(TimeZones.KST);
                break;
            case R.id.tz_aest:
                item.setChecked(true);
                fDay.setTimeZone(TimeZones.AEST);
                break;

        }

        fDay.setNewDate(getDateAsString(currentDate));

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (super.menu.isMenuShowing()) {
                super.menu.toggle();
                return true;
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setLoading(boolean value) {
        fBar.setLoading(value);
    }
}
