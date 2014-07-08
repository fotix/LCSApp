package com.trappz.lcsmashup.api.callbacks;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trappz.lcsmashup.api.messages.EventBusManager;
import com.trappz.lcsmashup.api.messages.ResponseNotification;
import com.trappz.lcsmashup.api.models.News.News;
import com.trappz.lcsmashup.api.responses.NewsResponseNotification;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import retrofit.client.Response;

/**
 * Created by Filipe Oliveira on 30-06-2014.
 */
public class NewsCallback extends GenericCallback<Response> {


    public NewsCallback(String requestID) {
        super(requestID);
    }

    @Override
    public void success(Response r, Response response) {

        ResponseNotification<ArrayList<News>> notification = new NewsResponseNotification();
        notification.requestId = requestId();
        notification.origin = r;

        try {
            String theString = getStringFromInputStream(r
                    .getBody().in());

            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, News>>() {
            }.getType(); // define generic type
            Map<String, News> result = gson
                    .fromJson(theString, mapType);

            if (result != null) {

                 ArrayList<News> data = new ArrayList<News>();
                for (String string : result.keySet()) {
                        data.add(result.get(string));
                }

                notification.data = data;
            } else
                Log.d(TAG, "bode");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        EventBusManager.post(notification);

    }
}
