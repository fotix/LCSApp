package com.trappz.lcsmashup.api.callbacks;

import com.google.gson.Gson;
import com.trappz.lcsmashup.api.messages.EventBusManager;
import com.trappz.lcsmashup.api.messages.ResponseNotification;
import com.trappz.lcsmashup.api.models.Match.Match;
import com.trappz.lcsmashup.api.responses.MatchResponseNotification;

import java.io.IOException;

import retrofit.client.Response;

/**
 * Created by Filipe Oliveira on 08-07-2014.
 */
public class MatchCallback  extends GenericCallback<Response> {



    public MatchCallback(String requestID){
        super(requestID);
    }


    @Override
    public void success(Response r,Response response){
        ResponseNotification<Match> notification = new MatchResponseNotification();
        notification.requestId = requestId();
        notification.origin = r;

        try{
            String theString = getStringFromInputStream(r.getBody().in());

            Gson gson = new Gson();

            Match m = gson.fromJson(theString,Match.class);

            notification.data = m;


        } catch (IOException e) {
            e.printStackTrace();
        }

        EventBusManager.post(notification);
    }
}
