package com.trappz.app.lcsmashup.adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;

import com.squareup.picasso.Picasso;

import com.trappz.app.api.models.News.News;
import com.trappz.app.api.models.Youtube.YoutubeResponse;

import com.trappz.app.lcsmashup.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Filipe Oliveira on 01-07-2014.
 */
public class AdapterNews extends BaseAdapter {
    public static String TAG = "lcsAdapter";
    public ArrayList<News> newsList;
    public Context context;

    final Handler mHandler = new Handler();

    // Create runnable for posting
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            updateResultsInUi();
        }
    };

    private void updateResultsInUi() {
        notifyDataSetChanged();
    }

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
            holder.videoImageView = (ImageView) convertView.findViewById(R.id.adapter_newslist_video);
            holder.background = (ImageView) convertView.findViewById(R.id.newsBackground);
            holder.headline = (TextView) convertView.findViewById(R.id.newsHeadline);
//            holder.nutgraf = (TextView) convertView.findViewById(R.id.newsNutgraf);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.headline.setText(newsList.get(position).getHeadline());
//      holder.nutgraf.setText(newsList.get(position).getNutgraf());


        if (!newsList.get(position).getImageMediumUrl().equalsIgnoreCase(" ")) {
            Picasso.with(context).load(newsList.get(position).getImageMediumUrl()).into(holder.background);
        }else{
            Picasso.with(context).load(R.drawable.default_background).into(holder.background);
//            Picasso.with(context).load("http://img.youtube.com/vi/9XcK8wuk5zg/0.jpg").into(holder.background);
        }

        if(newsList.get(position).getTaxonomyId().equals("20") && newsList.get(position).getYoutubeID() == null)
            getYoutubeThumbnailURL(newsList.get(position),position);

        if(newsList.get(position).getTaxonomyId().equalsIgnoreCase("20")){
            holder.videoImageView.setVisibility(View.VISIBLE);
        }else
            holder.videoImageView.setVisibility(View.INVISIBLE);

        return convertView;
    }

    static class Holder {
        public TextView headline;
        public TextView nutgraf;
        public ImageView background;
        public ImageView videoImageView;
    }

    private String getYoutubeThumbnailURL(final News n, final int position){


        Runnable R = new Runnable(){

            @Override
            public void run() {
                try {
                    String url = "https://www.googleapis.com/youtube/v3/search?q="+ URLEncoder.encode(n.getHeadline(),"UTF-8")+"+&key=AIzaSyCAHG2RhyRuOIFJCo5purXDxwO57FPJSn0&part=snippet&maxResults=1";

                    DefaultHttpClient client = new DefaultHttpClient();

                    HttpGet getRequest = new HttpGet(url);

                    try {

                        HttpResponse getResponse = client.execute(getRequest);
                        final int statusCode = getResponse.getStatusLine().getStatusCode();

                        if (statusCode != HttpStatus.SC_OK) {
                            Log.w("youtube",
                                    "Error " + statusCode + " for URL " + url);
                            return;
                        }

                        HttpEntity getResponseEntity = getResponse.getEntity();
                        Reader reader = new InputStreamReader(getResponseEntity.getContent());

                        Gson gson = new Gson();

                        YoutubeResponse response = gson.fromJson(reader,YoutubeResponse.class);

                        if(response != null){

                            Log.w(TAG,"KIND: "+response.getKind());
                            Log.w(TAG, "VIDEO ID:" + response.getItems().get(0).getId().getVideoId());
                            newsList.get(position).setImageUrl("http://img.youtube.com/vi/" + response.getItems().get(0).getId().getVideoId() + "/0.jpg");
                            newsList.get(position).setImageMediumUrl("http://img.youtube.com/vi/" + response.getItems().get(0).getId().getVideoId() + "/0.jpg");
                            newsList.get(position).setYoutubeID(response.getItems().get(0).getId().getVideoId());
                            mHandler.post(mUpdateResults);
                        }
                    }
                    catch (IOException e) {
                        getRequest.abort();
                        Log.w("youtube", "Error for URL " + url, e);
                        return;
                    }


                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }

            }
        };

        new Thread(R).start();

        return null;
    }

}
