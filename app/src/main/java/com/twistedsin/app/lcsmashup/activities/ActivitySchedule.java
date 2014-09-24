package com.twistedsin.app.lcsmashup.activities;

import android.app.ActionBar;
import android.os.Bundle;

import com.twistedsin.app.lcsmashup.C;
import com.twistedsin.app.lcsmashup.R;
import com.twistedsin.app.lcsmashup.analyticsdata.DataType;
import com.twistedsin.app.lcsmashup.analyticsdata.GATracker;
import com.twistedsin.app.lcsmashup.fragments.FragmentScheduleBar;
import com.twistedsin.app.lcsmashup.fragments.FragmentScheduleDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Filipe Oliveira on 08-07-2014.
 *
 */
public class ActivitySchedule extends BaseActivity implements FragmentScheduleBar.OnDateChangedListener, FragmentScheduleDay.OnLoadingStateChanged{
    FragmentScheduleDay fDay;
    FragmentScheduleBar fBar;


    public static Date currentDate;
    private static Calendar cal = null;

    public static final String TAG= "ScheduleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Sending GA Screen Event
        GATracker.getInstance().sendAnalyticsData(DataType.SCREEN,getApplicationContext(),getLocalClassName());

        fDay = (FragmentScheduleDay) getFragmentManager().findFragmentById(R.id.fragment_list);

        fBar = (FragmentScheduleBar) getFragmentManager().findFragmentById(R.id.fragment_bar);

        ActionBar ab = getActionBar();

        ab.setTitle("Schedule");

//        if(getActionBar() != null)
//            getActionBar().setHomeButtonEnabled(true);

//        setupSlidingMenu();



        if(cal == null){
            cal = Calendar.getInstance();
            if(C.LOG_MODE) C.logI("Creating new Calendar instance");
        }

        if(currentDate == null) {
            currentDate = new Date();
        }
        if(C.LOG_MODE) C.logE("CURRENT DATE: " + getDateAsString(currentDate));

        fDay.setNewDate(getDateAsString(currentDate));
        fBar.refreshDate(getDateAsString(currentDate),getDayOfWeek(currentDate));
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected int getSelfNavDrawerItem() {

        return NAVDRAWER_ITEM_SCHEDULE;
    }

    @Override
    public void onNextDay() {
        cal.add(Calendar.DAY_OF_MONTH,1);

        currentDate = cal.getTime();

        String newDate = getDateAsString(currentDate);
        if(C.LOG_MODE) C.logI("NEW DATE: "+newDate);
        if(C.LOG_MODE) C.logI("NEW DATE ISO: "+getDateAsStringISO(currentDate));

        fBar.refreshDate(newDate,getDayOfWeek(currentDate));
        fDay.setNewDate(newDate);
    }

    @Override
    public void onPreviousDay() {
        cal.add(Calendar.DAY_OF_MONTH,-1);
        currentDate = cal.getTime();


        String newDate = getDateAsString(currentDate);

        if(C.LOG_MODE) C.logI("NEW DATE: "+newDate);
        if(C.LOG_MODE) C.logI("NEW DATE ISO: "+getDateAsStringISO(currentDate));

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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                this.toggleMenu();
//                return true;
//
//            case R.id.tz_pdt:
//                item.setChecked(true);
//                fDay.setTimeZone(TimeZones.PDT);
//                break;
//            case R.id.tz_edt:
//                item.setChecked(true);
//                fDay.setTimeZone(TimeZones.EDT);
//                break;
//            case R.id.tz_gmt:
//                item.setChecked(true);
//                fDay.setTimeZone(TimeZones.GMT);
//                break;
//            case R.id.tz_cest:
//                item.setChecked(true);
//                fDay.setTimeZone(TimeZones.CEST);
//                break;
//            case R.id.tz_ist:
//                item.setChecked(true);
//                fDay.setTimeZone(TimeZones.IST);
//                break;
//            case R.id.tz_kst:
//                item.setChecked(true);
//                fDay.setTimeZone(TimeZones.KST);
//                break;
//            case R.id.tz_aest:
//                item.setChecked(true);
//                fDay.setTimeZone(TimeZones.AEST);
//                break;
//
//        }
//
//        fDay.setNewDate(getDateAsString(currentDate));
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
////
////            if (super.menu.isMenuShowing()) {
////                super.menu.toggle();
////                return true;
////            }
//
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void setLoading(boolean value) {
        fBar.setLoading(value);
    }
}
