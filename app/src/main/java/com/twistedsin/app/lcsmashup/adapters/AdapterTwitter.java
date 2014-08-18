package com.twistedsin.app.lcsmashup.adapters;

import android.content.Context;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twistedsin.app.api.models.Twitter.Tweet;
import com.twistedsin.app.lcsmashup.C;
import com.twistedsin.app.lcsmashup.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Filipe Oliveira on 14-08-2014.
 */
public class AdapterTwitter extends BaseAdapter {

    ArrayList<Tweet> tweets = new ArrayList<Tweet>();

    Context context;

    public AdapterTwitter(Context ctx, ArrayList<Tweet> tList) {
        context = ctx;
        tweets = tList;

        if (C.LOG_MODE) C.logW("tweet list size: " + tweets.size());


    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public Object getItem(int position) {
        return tweets.get(position);
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
            convertView = inflater.inflate(R.layout.adapter_twitter, parent, false);

            holder = new Holder();

            holder.tweet = (TextView) convertView.findViewById(R.id.tweet);
            holder.profileName = (TextView) convertView.findViewById(R.id.twitter_profile_name);
            holder.profileTag = (TextView) convertView.findViewById(R.id.twitter_profile_tag);
            holder.tweetTime = (TextView) convertView.findViewById(R.id.twitter_tweet_time);
            holder.tweetImage = (ImageView) convertView.findViewById(R.id.tweet_image);
            holder.profileImage = (ImageView) convertView.findViewById(R.id.twitter_profile_image);




            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (tweets.get(position).getText() != null) {
            holder.tweet.setText(tweets.get(position).getText());


            Linkify.TransformFilter filter = new Linkify.TransformFilter() {
                public final String transformUrl(final Matcher match, String url) {
                    return match.group();
                }
            };

            Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
            String mentionScheme = "http://www.twitter.com/";
            Linkify.addLinks(holder.tweet, mentionPattern, mentionScheme, null, filter);

            Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9_-]+)");
            String hashtagScheme = "http://www.twitter.com/search/";
            Linkify.addLinks(holder.tweet, hashtagPattern, hashtagScheme, null, filter);

            Pattern urlPattern = Patterns.WEB_URL;
            Linkify.addLinks(holder.tweet, urlPattern, null, null, filter);

            if(tweets.get(position).getEntities().getMedia().size() != 0) {
                holder.tweetImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load(tweets.get(position).getEntities().getMedia().get(0).getMediaUrl()).into(holder.tweetImage);
            }else
                holder.tweetImage.setVisibility(View.GONE);
        } else
            holder.tweet.setText("NULL");

        holder.profileName.setText(tweets.get(position).getUser().getName());
        holder.profileTag.setText("@"+tweets.get(position).getUser().getScreenName());
//        holder.tweetTime.setText(tweets.get(position).getDateCreated());

        setTime(holder.tweetTime, tweets.get(position).getDateCreated());
        Picasso.with(context).load(tweets.get(position).getUser().getProfileImageUrl()).into(holder.profileImage);

        return convertView;
}

    private void setTime(TextView tweetTime, String dateCreated) {
        Date d = null;

       d = parseTwitterDate(dateCreated);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long currentTime = cal.getTimeInMillis();
        long milliseconds =  currentTime - d.getTime();


        tweetTime.setText(getTimeText(milliseconds));


    }

    private String getTimeText(long diff) {
        diff = diff/1000;
        if (diff <= 1) {return "0s";}
        if (diff < 20) {return diff + "s";}
        if (diff < 40) {return "<30s";}
        if (diff < 60) {return "<1m";}
        if (diff <= 90) {return "1m";}
        if (diff <= 3540) {return  Math.round(diff / 60) + "m";}
        if (diff <= 5400) {return "1h";}
        if (diff <= 86400) {return Math.round(diff / 3600) + "h";}
        if (diff <= 129600) {return "1d";}
        if (diff < 604800) {return Math.round(diff / 86400) + "d";}
        if (diff <= 777600) {return ">1w";}

        return "-";
    }

    public Date parseTwitterDate(String date)
    {


        final String TWITTER = "EEE MMM dd HH:mm:ss Z yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
        sf.setLenient(true);

        try {
            return sf.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    static class Holder {
    public TextView tweet;
    public TextView profileName;
    public TextView profileTag;
    public TextView tweetTime;
    public ImageView profileImage;
    public ImageView tweetImage;

}
}
