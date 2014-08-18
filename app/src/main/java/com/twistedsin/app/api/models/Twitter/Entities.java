package com.twistedsin.app.api.models.Twitter;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Filipe Oliveira on 14-08-2014.
 */


public class Entities{
    @SerializedName("media")
    private ArrayList<Media> media = new ArrayList<Media>();


    public ArrayList<Media> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Media> media) {
        this.media = media;
    }

    @SerializedName("urls")
    private ArrayList<UrlData> urls = new ArrayList<UrlData>();

    public ArrayList<UrlData> getData() {
        return urls;
    }

    public void setData(ArrayList<UrlData> data) {
        this.urls = data;
    }

}
