package com.twistedsin.app.api.models.Youtube;

import com.google.gson.annotations.Expose;

public class Id {

    @Expose
    private String kind;
    @Expose
    private String videoId;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

}
