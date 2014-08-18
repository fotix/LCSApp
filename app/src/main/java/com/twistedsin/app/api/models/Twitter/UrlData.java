package com.twistedsin.app.api.models.Twitter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Filipe Oliveira on 14-08-2014.
 */
public class UrlData{
    @SerializedName("expanded_url")
    private String expanded_url;

    @SerializedName("url")
    private String url;

    @SerializedName("display_url")
    private String display_url;

    public String getExpandedUrl() {
        return expanded_url;
    }

    public void setExpandedUrl(String expandedUrl) {
        this.expanded_url = expandedUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayUrl() {
        return display_url;
    }

    public void setDisplayUrl(String displayUrl) {
        this.display_url = displayUrl;
    }
}
