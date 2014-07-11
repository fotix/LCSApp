package com.trappz.lcsmashup.api.models.Game;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filipe Oliveira on 11-07-2014.
 */
public class Vods {

    @SerializedName("vod")
    private Vod vod;

    public Vod getVod() {
        return vod;
    }

    public void setVod(Vod vod) {
        this.vod = vod;
    }

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
