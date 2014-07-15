package com.trappz.lcsmashup.lcsmashup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trappz.lcsmashup.api.models.News.News;
import com.trappz.lcsmashup.lcsmashup.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by Filipe Oliveira on 01-07-2014.
 */
public class AdapterNews extends BaseAdapter {
    public static String TAG = "lcsAdapter";
    ArrayList<News> newsList;
    Context context;

    public AdapterNews() {
        newsList = null;
        context = null;
    }

    public AdapterNews(Context context, ArrayList<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_newslist, parent, false);

            holder = new Holder();
            holder.background = (ImageView) convertView.findViewById(R.id.newsBackground);
            holder.headline = (TextView) convertView.findViewById(R.id.newsHeadline);
//            holder.nutgraf = (TextView) convertView.findViewById(R.id.newsNutgraf);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.headline.setText(newsList.get(position).getHeadline());
//        holder.nutgraf.setText(newsList.get(position).getNutgraf());


        if (!newsList.get(position).getImageUrl().equals(" ")) {

            Picasso.with(context).load(newsList.get(position).getImageUrl()).into(holder.background);

        } else
            Picasso.with(context).load(R.drawable.news_placeholder).into(holder.background);

        return convertView;
    }

    static class Holder {
        public TextView headline;
        public TextView nutgraf;
        public ImageView background;
    }
}
