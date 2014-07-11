package com.trappz.lcsmashup.api.callbacks;

import com.google.gson.Gson;
import com.trappz.lcsmashup.api.messages.EventBusManager;
import com.trappz.lcsmashup.api.messages.ResponseNotification;
import com.trappz.lcsmashup.api.models.Game.Game;
import com.trappz.lcsmashup.api.models.Match.Match;
import com.trappz.lcsmashup.api.responses.GameResponseNotification;
import com.trappz.lcsmashup.api.responses.MatchResponseNotification;

import java.io.IOException;

import retrofit.client.Response;

/**
 * Created by Filipe Oliveira on 11-07-2014.
 */
public class GameCallback extends GenericCallback<Response>{


    public GameCallback(String requestId) {
        super(requestId);
    }

    @Override
    public void success(Response r,Response response){
        ResponseNotification<Game> notification = new GameResponseNotification();
        notification.requestId = requestId();
        notification.origin = r;

        try{
            String theString = getStringFromInputStream(r.getBody().in());

            Gson gson = new Gson();

            Game m = gson.fromJson(theString,Game.class);

            notification.data = m;


        } catch (IOException e) {
            e.printStackTrace();
        }

        EventBusManager.post(notification);
    }
}
