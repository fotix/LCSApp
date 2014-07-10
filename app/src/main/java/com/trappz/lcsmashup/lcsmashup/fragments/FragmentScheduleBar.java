package com.trappz.lcsmashup.lcsmashup.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.trappz.lcsmashup.lcsmashup.R;
import com.trappz.lcsmashup.lcsmashup.activities.ActivitySchedule;

import org.w3c.dom.Text;

/**
 * Created by Filipe Oliveira on 09-07-2014.
 */
public class FragmentScheduleBar extends Fragment{
    ImageView previous,next;
    TextView date,dayofweek;

    OnDateChangedListener mDateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_schedule_bar, container, false);

        date = (TextView) v.findViewById(R.id.fragment_bar_date);
        dayofweek = (TextView) v.findViewById(R.id.fragment_bar_dayofweek);

        previous = (ImageView) v.findViewById(R.id.fragment_bar_previous);
        next = (ImageView) v.findViewById(R.id.fragment_bar_next);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateListener.onPreviousDay();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateListener.onNextDay();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mDateListener = (OnDateChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    public void refreshDate(String date,String dayOfWeek) {
        this.date.setText(date);
        this.dayofweek.setText(dayOfWeek);
    }

    public interface OnDateChangedListener{
        public void onNextDay();

        public void onPreviousDay();
    }
}
