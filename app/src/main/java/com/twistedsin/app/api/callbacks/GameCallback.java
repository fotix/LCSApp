package com.twistedsin.app.api.callbacks;

import com.google.gson.Gson;
import com.twistedsin.app.api.messages.EventBusManager;
import com.twistedsin.app.api.messages.ResponseNotification;
import com.twistedsin.app.api.models.Game.Game;
import com.twistedsin.app.api.responses.GameResponseNotification;
import com.twistedsin.app.lcsmashup.C;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import retrofit.client.Response;

/**
 * Created by Filipe Oliveira on 11-07-2014.
 */
public class GameCallback extends GenericCallback<Response> {


    public GameCallback(String requestId) {
        super(requestId);
    }

    @Override
    public void success(Response r, Response response) {
        ResponseNotification<Game> notification = new GameResponseNotification();
        notification.requestId = requestId();
        notification.origin = r;

        try {
            String theString = getStringFromInputStream(r.getBody().in());

            if(C.LOG_MODE) C.logW(theString);

            Gson gson = new Gson();

            Game m = gson.fromJson(theString, Game.class);


            JSONObject obj = new JSONObject(theString);
            Iterator keys = obj.keys();

            while (keys.hasNext()) {
                String currentDynamicKey = (String) keys.next();
                if (currentDynamicKey.equalsIgnoreCase("players")) {
                    JSONObject currentDynamicValue = obj.getJSONObject(currentDynamicKey);
                    Iterator players = currentDynamicValue.keys();
                    while (players.hasNext()) {
                        String s = (String) players.next();
//                        Log.w(TAG, "2 - VALUE: " + s.toString());
                        JSONObject player = currentDynamicValue.getJSONObject(s);
                        Iterator playerIterator = player.keys();
                        while (playerIterator.hasNext()) {
                            String playerKeys = (String) playerIterator.next();
                            if(playerKeys.startsWith("items")){
                                String item = player.getString(playerKeys);
                                m.getPlayers().get(s).itemList.add(item);
                            }

                        }
                    }
                }

            }

            notification.data = m;


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EventBusManager.post(notification);
    }
}
