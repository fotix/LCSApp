package com.trappz.app.lcsmashup.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trappz.app.api.models.News.News;
import com.trappz.app.lcsmashup.C;
import com.trappz.app.lcsmashup.R;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Filipe Oliveira on 03-07-2014.
 */
public class ActivityNewsDetail extends Activity{

    public static String TAG = "lcsNewsDetail";

    int index = 0;
    TextView headline,nutgraf;
    TextView body;
    ImageView topImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

//        ActionBar actionBar = getActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33222222")));
//        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#55222222")));
//
//        setContentView(R.layout.activity_news_detail);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("index");
        }

        News n = (News) ActivityDashboard.newsList.get(index);


        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(new ColorDrawable(Color.parseColor("#222222")))
                .headerLayout(R.layout.activity_newsdetail_header)
                .contentLayout(R.layout.activity_newsdetail);
        setContentView(helper.createView(this));
        helper.initActionBar(this);


        body = (TextView) findViewById(R.id.activity_news_detail_body);
        headline = (TextView) findViewById(R.id.activity_news_detail_tv_headline);
        nutgraf = (TextView) findViewById(R.id.activity_news_detail_tv_nutgraf);

        topImage = (ImageView) findViewById(R.id.image_header);

        if(!n.getImageMediumUrl().equals(" "))
            Picasso.with(getApplicationContext()).load(n.getImageMediumUrl()).into(topImage);


        if(C.LOG_MODE) C.logD(n.getBody() );

        String b = n.getBody().replaceAll(System.getProperty("line.separator"),"<br>").split("<h3>Related")[0];

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
        },null)
        );

        headline.setText(n.getHeadline());
        nutgraf.setText(n.getNutgraf());

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

   public class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            if(C.LOG_MODE) C.logD("doInBackground " + C.BASE_URL+source);
            try {
                InputStream is;
                if(source.startsWith("http")) {
                    if (source.startsWith("http://admin"))
                        is = new URL(C.BASE_URL + source.split("8080")[1]).openStream();
                    else
                        is = new URL(source).openStream();
                }
                else
                    is = new URL(C.BASE_URL+source).openStream();

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
                mDrawable.setBounds(0, 0, body.getWidth(), (bitmap.getHeight()*body.getWidth())/bitmap.getWidth());
                mDrawable.setLevel(1);

                CharSequence t = body.getText();
                body.setText(t);
            }
        }
    }

}
