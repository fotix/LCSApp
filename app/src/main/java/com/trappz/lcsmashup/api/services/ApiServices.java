package com.trappz.lcsmashup.api.services;

import com.trappz.lcsmashup.api.callbacks.*;

import java.util.UUID;

import retrofit.RestAdapter;

/**
 * Created by Filipe Oliveira on 30-06-2014.
 */
public class ApiServices {

    private static String serverURL = "http://euw.lolesports.com:80/api/";
    private static ApiServicesInterface services;

    protected synchronized static ApiServicesInterface getInstance(){

        if(services == null){
            RestAdapter adapter = new RestAdapter.Builder().setServer(serverURL).build();

            services = adapter.create(ApiServicesInterface.class);
        }

        return services;
    }

    public static String getNews(int limit){
        String requestID = UUID.randomUUID().toString();

        ApiServicesInterface instance = getInstance();

        instance.getNews(limit,new NewsCallback(requestID));

        return requestID;
    }

}
