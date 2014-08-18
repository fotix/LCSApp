package com.twistedsin.app.api.models.Twitter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Filipe Oliveira on 14-08-2014.
 */
public class Media {
    @SerializedName("media_url")
    private String mediaUrl;
    @SerializedName("media_url_https")
    private String mediaUrlHttps;
    @SerializedName("url")
    private String url;
    @SerializedName("type")
    private String type;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaUrlHttps() {
        return mediaUrlHttps;
    }

    public void setMediaUrlHttps(String mediaUrlHttps) {
        this.mediaUrlHttps = mediaUrlHttps;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
