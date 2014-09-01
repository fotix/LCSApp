package com.twistedsin.app.lcsmashup.activities;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.twistedsin.app.api.models.News.News;
import com.twistedsin.app.api.models.Youtube.YoutubeResponse;
import com.twistedsin.app.lcsmashup.C;
import com.twistedsin.app.lcsmashup.R;
import com.twistedsin.app.lcsmashup.Utils.onDataDownloadedListener;

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

/**
 * Created by Filipe Oliveira on 25-08-2014.
 */
public class ActivityLiveStream extends BaseActivity implements YouTubePlayer.OnInitializedListener,onDataDownloadedListener {
    public YoutubeResponse youtubeData;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerFragment youTubePlayerFragment;
    public onDataDownloadedListener dataListener;
    public YouTubePlayer.OnInitializedListener youtubeInitializer;
    private static final String LOLESPORTS_CHANNEL_ID ="UCvqRdlKsE5Q8mf8YXbdIJ";
    public static final String API_KEY = "AIzaSyCAHG2RhyRuOIFJCo5purXDxwO57FPJSn0";
    public static  String VIDEO_ID;

    RelativeLayout loadingLayout,noStreamLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestream);
        ActionBar ab = getActionBar();

        ab.setTitle("Live Stream");

        loadingLayout = (RelativeLayout) findViewById(R.id.activity_livestream_loadinglayout);
        noStreamLayout = (RelativeLayout) findViewById(R.id.activity_livestream_nostream);
        youtubeInitializer = this;
        dataListener = this;
        youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                .findFragmentById(R.id.youtubeplayerfragment);

        youTubePlayerFragment.initialize(API_KEY, this);
        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActionBar().hide();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getActionBar().show();
        }
    }

    @Override
    protected int getSelfNavDrawerItem() {

        return NAVDRAWER_ITEM_LIVESTREAM;
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
        youTubePlayer = player;
        getLOLEsportsLiveStreams();
//        if(C.LOG_MODE) C.logI("VIDEO ID: "+VIDEO_ID);
//        if(VIDEO_ID != null)
//            player.loadVideo(VIDEO_ID);
    }




    private String getLOLEsportsLiveStreams(){

        Runnable R = new Runnable(){

            @Override
            public void run() {
                String url = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyCAHG2RhyRuOIFJCo5purXDxwO57FPJSn0&part=snippet&maxResults=3&channelId="+LOLESPORTS_CHANNEL_ID+"&eventType=live&type=video";

                DefaultHttpClient client = new DefaultHttpClient();

                HttpGet getRequest = new HttpGet(url);

                try {

                    HttpResponse getResponse = client.execute(getRequest);
                    final int statusCode = getResponse.getStatusLine().getStatusCode();

                    if (statusCode != HttpStatus.SC_OK) {
                        if(C.LOG_MODE) C.logW(
                                "Error " + statusCode + " for URL " + url);
                        return;
                    }

                    HttpEntity getResponseEntity = getResponse.getEntity();
                    Reader reader = new InputStreamReader(getResponseEntity.getContent());

                    Gson gson = new Gson();

                    YoutubeResponse response = gson.fromJson(reader,YoutubeResponse.class);
                    youtubeData = response;
                    if(response != null){

                        if(C.LOG_MODE) C.logW("KIND: "+response.getKind());
                        if(C.LOG_MODE) C.logW("TotalResults: "+response.getPageInfo().getResultsPerPage());

                        if(response.getPageInfo().getTotalResults() > 0){

                            VIDEO_ID = response.getItems().get(0).getId().getVideoId();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataListener.onDataDownloaded(true);
                                }
                            });

                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataListener.onDataDownloaded(false);
                                }
                            });
                        }
                    }
                }
                catch (IOException e) {
                    getRequest.abort();
                    if(C.LOG_MODE)
                        Log.w("youtube", "Error for URL " + url, e);
                    return;
                }


            }

        };

        new Thread(R).start();

        return null;
    }

    @Override
    public void onDataDownloaded(boolean success) {
        if(success){
            if(C.LOG_MODE) C.logW("Data downloaded with sucess, starting stream");
            youTubePlayer.loadVideo(VIDEO_ID);
            loadingLayout.setVisibility(View.GONE);
        }else{
            if(C.LOG_MODE) C.logW("No channels were found");
            loadingLayout.setVisibility(View.GONE);
            noStreamLayout.setVisibility(View.VISIBLE);
        }
    }

}
