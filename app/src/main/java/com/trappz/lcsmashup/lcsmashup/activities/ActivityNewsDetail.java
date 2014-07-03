package com.trappz.lcsmashup.lcsmashup.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.text.Html;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trappz.lcsmashup.api.models.News;
import com.trappz.lcsmashup.lcsmashup.R;

import org.w3c.dom.Text;

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


        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33222222")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#55222222")));

        setContentView(R.layout.activity_news_detail);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("index");
        }

        News n = (News) ActivityDashboard.newsList.get(index);

        body = (TextView) findViewById(R.id.activity_news_detail_body);
        headline = (TextView) findViewById(R.id.activity_news_detail_tv_headline);
        nutgraf = (TextView) findViewById(R.id.activity_news_detail_tv_nutgraf);

        topImage = (ImageView) findViewById(R.id.activity_news_detail_topimage);

        if(!n.getImageUrl().equals(" "))
            Picasso.with(getApplicationContext()).load(n.getImageUrl()).into(topImage);

        body.setText(Html.fromHtml(n.getBody()));

        headline.setText(n.getHeadline());
        nutgraf.setText(n.getNutgraf());

    }
}
