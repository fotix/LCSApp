package com.trappz.lcsmashup.lcsmashup.adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
            holder.tournamentName = (TextView) convertView.findViewById(R.id.adapter_schedule_tournamentname);

            holder.ll = (LinearLayout) convertView.findViewById(R.id.adapter_schedule_info);

            convertView.setTag(holder);
        }else
        {
            holder = (Holder) convertView.getTag();
        }
        if(mList.get(position).getContestants() != null) {
            if (!mList.get(position).getContestants().getBlue().getAcronym().isEmpty())
                holder.BlueTeamName.setText(mList.get(position).getContestants().getBlue().getAcronym());
            else
                holder.BlueTeamName.setText(mList.get(position).getContestants().getBlue().getName());

            if (!(mList.get(position).getContestants().getRed().getAcronym().isEmpty()))
                holder.RedTeamName.setText(mList.get(position).getContestants().getRed().getAcronym());
            else
                holder.RedTeamName.setText(mList.get(position).getContestants().getRed().getName());


            holder.BlueScore.setText(mList.get(position).getContestants().getBlue().getWins()+"W - "+
                    mList.get(position).getContestants().getBlue().getLosses()+"L");

            holder.RedScore.setText(mList.get(position).getContestants().getRed().getWins()+"W - "+
                    mList.get(position).getContestants().getRed().getLosses()+"L");

            if(mList.get(position).getWinnerId().equals(mList.get(position).getContestants().getBlue().getId()))
            {
                //Blue Team wins
//                holder.BlueLogo.setBackgroundColor(Color.parseColor("#22FFBB33"));
//                holder.RedLogo.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                holder.BlueLogo.setBackground(context.getResources().getDrawable(R.drawable.winner_border));
                holder.RedLogo.setBackground(null);
            }else
            {
                holder.RedLogo.setBackground(context.getResources().getDrawable(R.drawable.winner_border));
                holder.BlueLogo.setBackground(null);

//                holder.RedLogo.setBackgroundColor(Color.parseColor("#22FFBB33"));
//                holder.BlueLogo.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            }
            Picasso.with(context).load(C.BASE_URL+mList.get(position).getContestants().getBlue().getLogoURL()).into(holder.BlueLogo);
            Picasso.with(context).load(C.BASE_URL+mList.get(position).getContestants().getRed().getLogoURL()).into(holder.RedLogo);
        }else
        {
            holder.BlueTeamName.setText("TBD");
            holder.RedTeamName.setText("TBD");

            holder.BlueScore.setText("0W - 0L");
            holder.RedScore.setText("0W - 0L");
        }


        holder.tournamentName.setText(mList.get(position).getTournament().getName()
                +" - Round "
                +mList.get(position).getTournament().getRound());

        holder.date.setText(mList.get(position).getDateTime().split("T")[1].split("Z")[0]);
        holder.ll.setBackgroundColor(Color.parseColor(mList.get(position).getColor().replace("#","#33")));





        return convertView;
    }

    static class Holder
    {
        public LinearLayout ll;

        public TextView date;
        public TextView tournamentName;

        public TextView BlueScore;
        public TextView RedScore;
        public TextView BlueTeamName;
        public TextView RedTeamName;

        public ImageView BlueLogo;
        public ImageView RedLogo;
    }

}
