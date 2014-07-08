package com.trappz.lcsmashup.lcsmashup.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trappz.lcsmashup.api.models.Match.Match;
import com.trappz.lcsmashup.lcsmashup.C;
import com.trappz.lcsmashup.lcsmashup.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Filipe Oliveira on 08-07-2014.
 */
public class AdapterSchedule extends BaseAdapter {

    ArrayList<Match> mList;
    Context context;

    public AdapterSchedule(Context context,ArrayList<Match> matchList)
    {
        this.context = context;
        this.mList = matchList;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Match getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_schedule,parent,false);

            holder = new Holder();
            holder.BlueTeamName = (TextView) convertView.findViewById(R.id.adapter_schedule_blueteam_name);
            holder.RedTeamName = (TextView) convertView.findViewById(R.id.adapter_schedule_redteam_name);

            holder.BlueScore = (TextView) convertView.findViewById(R.id.adapter_schedule_score_blueteam);
            holder.RedScore = (TextView) convertView.findViewById(R.id.adapter_schedule_score_redteam);

            holder.BlueLogo = (ImageView) convertView.findViewById(R.id.adapter_schedule_iv_blueteam);
            holder.RedLogo = (ImageView) convertView.findViewById(R.id.adapter_schedule_iv_redteam);

            holder.date = (TextView) convertView.findViewById(R.id.adapter_schedule_date);


            convertView.setTag(holder);
        }else
        {
            holder = (Holder) convertView.getTag();
        }

        holder.BlueTeamName.setText(mList.get(position).getContestants().getBlue().getName());
        holder.RedTeamName.setText(mList.get(position).getContestants().getRed().getName());
        holder.date.setText(mList.get(position).getDateTime());

        Picasso.with(context).load(C.BASE_URL+mList.get(position).getContestants().getBlue().getLogoURL()).into(holder.BlueLogo);
        Picasso.with(context).load(C.BASE_URL+mList.get(position).getContestants().getRed().getLogoURL()).into(holder.RedLogo);


        return convertView;
    }

    static class Holder
    {
        public TextView date;
        public TextView BlueScore;
        public TextView RedScore;
        public TextView BlueTeamName;
        public TextView RedTeamName;

        public ImageView BlueLogo;
        public ImageView RedLogo;
    }

}
