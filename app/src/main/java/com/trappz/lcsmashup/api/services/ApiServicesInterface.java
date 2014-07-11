package com.trappz.lcsmashup.api.services;

import com.trappz.lcsmashup.api.models.Game.Game;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Filipe Oliveira on 30-06-2014.
 */
public interface ApiServicesInterface {

    @GET("/news.json")
    void getNews(@Query("limit") int limit,@Query("offset")int offset, Callback<Response> callback);

    @GET("/programmingWeek/{date}/{offset}.json")
    void getProgrammingWeek(@Path("date") String date,@Path("offset") String offset,Callback<Response> callback);

    @GET("/programming/{id}.json")
    void getProgrammingBlock(@Path("id") String id,Callback<Response> callback);

    @GET("/match/{id}.json")
    void getMatch(@Path("id") String id,Callback<Response> callback);

    @GET("/game/{id}.json")
    void getGame(@Path("id") String id,Callback<Response> callback);
}
