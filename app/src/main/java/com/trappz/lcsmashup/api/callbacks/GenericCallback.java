package com.trappz.lcsmashup.api.callbacks;

import com.trappz.lcsmashup.api.messages.EventBusManager;
import com.trappz.lcsmashup.api.messages.ResponseNotification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Filipe Oliveira on 30-06-2014.
 */
public class GenericCallback<T> implements Callback<T> {
    protected static String TAG = "lcscallbacks";
    private String requestId;

    public String requestId(){
        return requestId;
    }

    public GenericCallback(String requestId){
        this.requestId = requestId;
    }

    @Override
    public void failure(RetrofitError error) {
        ResponseNotification<T> notification = new ResponseNotification<T>();
        notification.requestId = requestId;
        notification.error = error;

        EventBusManager.post(notification);
    }

    @Override
    public void success(T data, Response r) {
        ResponseNotification<T> notification = createTypedResponse();
        notification.requestId = requestId;
        notification.data = data;
        notification.origin = r;

        EventBusManager.post(notification);
    }

    protected ResponseNotification<T> createTypedResponse() {
        return new ResponseNotification<T>();
    }

    protected String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
