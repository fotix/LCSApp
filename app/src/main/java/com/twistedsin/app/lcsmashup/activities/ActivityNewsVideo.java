package com.twistedsin.app.lcsmashup.activities;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.twistedsin.app.api.models.News.News;
import com.twistedsin.app.lcsmashup.Base;
import com.twistedsin.app.lcsmashup.C;
import com.twistedsin.app.lcsmashup.R;
import com.twistedsin.app.lcsmashup.analytics.DataType;
import com.twistedsin.app.lcsmashup.analytics.GATracker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static com.twistedsin.app.lcsmashup.R.color.black;

/**
 * Created by Filipe Oliveira on 21-07-2014.
 */
public class ActivityNewsVideo extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    public static String TAG = "ActivityNewsVideo";
    int index = 0;
    TextView body, headline, nutgraf;
    YouTubePlayerView youTubeView;
    String vodUrl = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_video);

        getActionBar().setDisplayHomeAsUpEnabled(true);


        //Sending GA Screen Event
        GATracker.getInstance().sendAnalyticsData(DataType.SCREEN, getApplicationContext(), getLocalClassName());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("index");
        }

        News n = (News) ActivityDashboard.newsList.get(index);
        vodUrl = n.getYoutubeID();
        if (n.getYoutubeID() != null) {

        }

        body = (TextView) findViewById(R.id.activity_news_video_body);
        headline = (TextView) findViewById(R.id.activity_news_video_tv_headline);
        nutgraf = (TextView) findViewById(R.id.activity_news_video_tv_nutgraf);

        youTubeView = (YouTubePlayerView) findViewById(R.id.newsvideo_player);

        youTubeView.initialize("AIzaSyCAHG2RhyRuOIFJCo5purXDxwO57FPJSn0", this);


        String b = n.getBody().replaceAll(System.getProperty("line.separator"), "<br>").split("<h3>Related")[0];



        body.setText(Html.fromHtml(b, new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {

                        LevelListDrawable d = new LevelListDrawable();
                        Drawable empty = getResources().getDrawable(R.drawable.default_background);
                        d.addLevel(0, 0, empty);
                        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

                        new LoadImage().execute(source, d);

                        return d;
                    }
                }, null)
        );

        stripUnderlines(body);

        headline.setText(n.getHeadline());
        nutgraf.setText(n.getNutgraf());
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult error) {
        Toast.makeText(this, "Oh no! " + error.toString(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        player.loadVideo(vodUrl);
    }


    public class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            if(C.LOG_MODE) C.logD("doInBackground " + C.BASE_URL + source);
            try {
                InputStream is;
                if (source.startsWith("http")) {
                    if (source.startsWith("http://admin"))
                        is = new URL(C.BASE_URL + source.split("8080")[1]).openStream();
                    else
                        is = new URL(source).openStream();
                } else
                    is = new URL(C.BASE_URL + source).openStream();

                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(C.LOG_MODE) C.logD("onPostExecute drawable " + mDrawable);
            if(C.LOG_MODE) C.logD("onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);

                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, body.getWidth(), (bitmap.getHeight() * body.getWidth()) / bitmap.getWidth());
                mDrawable.setLevel(1);

                CharSequence t = body.getText();
                body.setText(t);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    private class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(black);
            ds.setUnderlineText(false);
        }
    }
}
