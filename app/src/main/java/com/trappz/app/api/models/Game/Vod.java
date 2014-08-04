package com.trappz.app.api.models.Game;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filipe Oliveira on 11-07-2014.
 */
public class Vod {

    private String type;
    private String URL;
    private Object embedCode;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String uRL) {
        this.URL = uRL;
    }

    public Object getEmbedCode() {
        return embedCode;
    }

    public void setEmbedCode(Object embedCode) {
        this.embedCode = embedCode;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
