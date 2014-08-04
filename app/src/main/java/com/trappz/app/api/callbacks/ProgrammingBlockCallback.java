package com.trappz.app.api.callbacks;

import com.google.gson.Gson;
import com.trappz.app.api.messages.EventBusManager;
import com.trappz.app.api.messages.ResponseNotification;
import com.trappz.app.api.models.Programming.ProgrammingBlock;
import com.trappz.app.api.responses.ProgrammingBlockResponseNotification;

import java.io.IOException;

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
