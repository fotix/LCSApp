package com.trappz.lcsmashup.api.messages;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Filipe Oliveira on 30-06-2014.
 */
public class ResponseNotification<T> {
    public String requestId;
    public T data;
    public Response origin;
    public RetrofitError error;
}
