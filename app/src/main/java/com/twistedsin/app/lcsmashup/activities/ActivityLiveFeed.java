package com.twistedsin.app.lcsmashup.activities;

import android.app.ActionBar;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.twistedsin.app.api.models.Twitter.Authenticated;
import com.twistedsin.app.api.models.Twitter.Tweet;
import com.twistedsin.app.api.models.Twitter.Twitter;
import com.twistedsin.app.api.services.ApiServices;
import com.twistedsin.app.lcsmashup.C;
import com.twistedsin.app.lcsmashup.R;
import com.twistedsin.app.lcsmashup.adapters.AdapterTwitter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.DefaultHeaderTransformer;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by Filipe Oliveira on 13-08-2014.
 */
public class ActivityLiveFeed extends BaseActivity implements OnRefreshListener {

    public final static String CONSUMER_KEY = "Vd8g4xoNyLlMiaaVi96AuI60D";
    public final static String CONSUMER_SECRET = "rmjXwoCPc1ZiGh786Uw4kYx9bp4qeP4wMXiPGkK9VKPt9uZbJU";
    public final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
    public final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

    final static String ScreenName = "lolesports";

    ArrayList<Tweet> tweetList  = new ArrayList<Tweet>();
    boolean isLoading = false;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ListView tweetFeedListview;
    public static String lastTweetID = "0";
    public static String sinceID = "0";
    AdapterTwitter adapter;

    RelativeLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livefeed);

        ActionBar ab = getActionBar();
        ab.setTitle("Live Feed");

        tweetList = new ArrayList<Tweet>();
        sinceID = lastTweetID;
        lastTweetID = "0";

        loadingLayout = (RelativeLayout)findViewById( R.id.activity_livefeed_loadinglayout);

        tweetFeedListview = (ListView) findViewById(R.id.activity_livefeed_listview);

        tweetFeedListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (isLoading == false) {
                        setProgressBarIndeterminateVisibility(true);
                        isLoading = true;
                        new GetMoreTweetsTask().execute(ScreenName);
                    }
                }
            }
        });


        // As we're using a ListFragment we create a PullToRefreshLayout manually
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.activity_livefeed_layout);

        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(this)

                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);
        DefaultHeaderTransformer transformer = (DefaultHeaderTransformer) mPullToRefreshLayout
                .getHeaderTransformer();
        transformer.setProgressBarColor(getResources().getColor(R.color.theme_primary));

        if(tweetList.size()  == 0)
            downloadTweets();

        adapter = new AdapterTwitter(getBaseContext(), tweetList);
        tweetFeedListview.setAdapter(adapter);
    }

    @Override
    protected int getSelfNavDrawerItem() {

        return NAVDRAWER_ITEM_LIVEFEED;
    }

    @Override
    public void onRefreshStarted(View view) {
        downloadTweets();
    }


    // download twitter timeline after first checking to see if there is a network connection
    public void downloadTweets() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadTwitterTask().execute(ScreenName);
        } else {
            if (C.LOG_MODE) C.logW("No network connection available.");
        }

    }


    private class GetMoreTweetsTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... screenNames) {
            String result = null;

            if (C.LOG_MODE) C.logW("CALLING GET MORE TWEETS !!!!!!!!!!!!!!");
            if (screenNames.length > 0) {
                result = getTwitterStream(screenNames[0],false);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Twitter twits = jsonToTwitter(result);
            if (C.LOG_MODE) C.logI(result);

            handleTwits(twits);

            sinceID = tweetList.get(0).getId();
            lastTweetID = tweetList.get(tweetList.size()-1).getId();

            isLoading = false;
            setProgressBarIndeterminateVisibility(false);
        }
    }

    // Uses an AsyncTask to download a Twitter user's timeline
    private class DownloadTwitterTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... screenNames) {
            String result = null;

            if (screenNames.length > 0) {
                result = getTwitterStream(screenNames[0],true);
            }

            return result;
        }

        // onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
        @Override
        protected void onPostExecute(String result) {
            Twitter twits = jsonToTwitter(result);

            handleTwits(twits);
            if(tweetList.size() > 0) {
                sinceID = tweetList.get(0).getId();
                lastTweetID = tweetList.get(tweetList.size() - 1).getId();
            }
            isLoading = false;
            loadingLayout.setVisibility(View.GONE);
            mPullToRefreshLayout.setRefreshComplete();
        }

    }

    private void handleTwits(Twitter twits){
        // lets write the results to the console as well
        for (Tweet tweet : twits) {


                if(C.LOG_MODE) C.logI(tweet.getId());


            if (tweet.getEntities() != null) {

                if (tweet.getEntities().getData().size() == 0) {

//                    if (C.LOG_MODE) C.logI("Got Entitites but no DATA");

                } else {
//                    C.logI("IMG ENTITIES URL: " + tweet.getEntities().getData().get(0).getUrl());

                    String s = tweet.getText();
                    s = s.replace(tweet.getEntities().getData().get(0).getUrl(),tweet.getEntities().getData().get(0).getExpandedUrl());

                    tweet.setText(s);
                }

                if(tweet.getEntities().getMedia().size() == 0){
//                    if (C.LOG_MODE) C.logI("Got Entitites but no MEDIA");
                }else {
//                    C.logI("IMG MEDIA URL: " + tweet.getEntities().getMedia().get(0).getMediaUrl());

                    String s = tweet.getText();
                    s = s.replace(tweet.getEntities().getMedia().get(0).getUrl()," ");

                    tweet.setText(s);
                }


            } else {
//                    if (C.LOG_MODE) C.logI("No entitites found");
            }


            AddTweet(tweet);
        }
    }

    // converts a string of JSON data into a Twitter object
    private Twitter jsonToTwitter(String result) {
        Twitter twits = null;
        if (result != null && result.length() > 0) {
            try {
                Gson gson = new Gson();
                twits = gson.fromJson(result, Twitter.class);
            } catch (IllegalStateException ex) {
                // just eat the exception
            }
        }
        return twits;
    }

    // convert a JSON authentication object into an Authenticated object
    private Authenticated jsonToAuthenticated(String rawAuthorization) {
        Authenticated auth = null;
        if (rawAuthorization != null && rawAuthorization.length() > 0) {
            try {
                Gson gson = new Gson();
                auth = gson.fromJson(rawAuthorization, Authenticated.class);
            } catch (IllegalStateException ex) {
                // just eat the exception
            }
        }
        return auth;
    }

    private String getResponseBody(HttpRequestBase request) {
        StringBuilder sb = new StringBuilder();
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String reason = response.getStatusLine().getReasonPhrase();

            if (statusCode == 200) {

                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sb.append(line);
                }
            } else {
                sb.append(reason);
            }
        } catch (UnsupportedEncodingException ex) {
        } catch (ClientProtocolException ex1) {
        } catch (IOException ex2) {
        }
        return sb.toString();
    }

    private String getTwitterStream(String screenName,boolean isRefresh) {
        String results = null;

        // Step 1: Encode consumer key and secret
        try {
            // URL encode the consumer key and secret
            String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
            String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");

            // Concatenate the encoded consumer key, a colon character, and the
            // encoded consumer secret
            String combined = urlApiKey + ":" + urlApiSecret;

            // Base64 encode the string
            String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

            // Step 2: Obtain a bearer token
            HttpPost httpPost = new HttpPost(TwitterTokenURL);
//                if (C.LOG_MODE) C.logI("TOKEN:     " + TwitterTokenURL);
            httpPost.setHeader("Authorization", "Basic " + base64Encoded);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
            String rawAuthorization = getResponseBody(httpPost);
            Authenticated auth = jsonToAuthenticated(rawAuthorization);

            // Applications should verify that the value associated with the
            // token_type key of the returned object is bearer
            if (auth != null && auth.token_type.equals("bearer")) {

                // Step 3: Authenticate API requests with bearer token
                HttpGet httpGet;
                if(isRefresh) {
                    if(sinceID.equals("0"))
                        httpGet = new HttpGet(TwitterStreamURL + screenName);
                    else
                        httpGet = new HttpGet(TwitterStreamURL + screenName + "&since_id="+sinceID);
                }
                else
                    httpGet = new HttpGet(TwitterStreamURL + screenName+ "&max_id="+lastTweetID);
                // construct a normal HTTPS request and include an Authorization
                // header with the value of Bearer <>
                httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
                httpGet.setHeader("Content-Type", "application/json");
                // update the results with the body of the response
                results = getResponseBody(httpGet);
            }
        } catch (UnsupportedEncodingException ex) {
        } catch (IllegalStateException ex1) {
        }
        return results;
    }

    private synchronized void AddTweet(Tweet t){

        tweetList.add(t);
        adapter.notifyDataSetChanged();
    }
}
