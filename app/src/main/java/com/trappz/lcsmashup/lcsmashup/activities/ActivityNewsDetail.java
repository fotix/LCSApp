package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trappz.lcsmashup.api.models.News.News;
import com.trappz.lcsmashup.lcsmashup.R;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

/**
 * Created by Filipe Oliveira on 03-07-2014.
 */
public class ActivityNewsDetail extends Activity{

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

        if(!n.getImageUrl().equals(" "))
            Picasso.with(getApplicationContext()).load(n.getImageUrl()).into(topImage);

        body.setText(Html.fromHtml(n.getBody()));

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

}
