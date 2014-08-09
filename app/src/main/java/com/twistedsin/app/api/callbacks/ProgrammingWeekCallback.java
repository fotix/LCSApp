package com.twistedsin.app.api.callbacks;

import com.google.gson.Gson;
import com.twistedsin.app.api.messages.EventBusManager;
import com.twistedsin.app.api.messages.ResponseNotification;
import com.twistedsin.app.api.models.Programming.ProgrammingWeek;
import com.twistedsin.app.api.responses.ProgrammingWeekResponseNotification;

import java.io.IOException;

import retrofit.client.Response;

/**
 * Created by Filipe Oliveira on 07-07-2014.
 */
public class ProgrammingWeekCallback extends GenericCallback<Response> {

    public ProgrammingWeekCallback(String requestID) {
        super(requestID);
    }

    @Override
    public void success(Response r, Response response) {


        ResponseNotification<ProgrammingWeek> notification = new ProgrammingWeekResponseNotification();

        notification.requestId = requestId();
        notification.origin = r;

        try{
            String theString = getStringFromInputStream(r.getBody().in());

            Gson gson = new Gson();
            ProgrammingWeek p = gson.fromJson(theString,ProgrammingWeek.class);

            notification.data = p;

        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e ){
            e.printStackTrace();
        }

        EventBusManager.post(notification);

    }


}
