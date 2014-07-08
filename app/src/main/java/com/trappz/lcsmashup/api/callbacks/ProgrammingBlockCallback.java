package com.trappz.lcsmashup.api.callbacks;

import android.util.Log;

import com.google.gson.Gson;
import com.trappz.lcsmashup.api.messages.EventBusManager;
import com.trappz.lcsmashup.api.messages.ResponseNotification;
import com.trappz.lcsmashup.api.models.Programming.ProgrammingBlock;
import com.trappz.lcsmashup.api.models.Programming.ProgrammingWeek;
import com.trappz.lcsmashup.api.responses.ProgrammingBlockResponseNotification;

import java.io.IOException;

import retrofit.client.Request;
import retrofit.client.Response;

/**
 * Created by Filipe Oliveira on 08-07-2014.
 */
public class ProgrammingBlockCallback extends GenericCallback<Response>{

    public ProgrammingBlockCallback(String requestID){
        super(requestID);
    }


    @Override
    public void success(Response r,Response response){



        ResponseNotification<ProgrammingBlock> notification = new ProgrammingBlockResponseNotification();

        notification.requestId = requestId();
        notification.origin = r;

        try{

            String theString = getStringFromInputStream(r.getBody().in());

            Gson gson = new Gson();

            ProgrammingBlock p = gson.fromJson(theString,ProgrammingBlock.class);

            notification.data = p;

        } catch (IOException e) {
            e.printStackTrace();
        }

        EventBusManager.post(notification);

    }

}
