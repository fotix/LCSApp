package com.twistedsin.app.api.models.Match;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filipe Oliveira on 08-07-2014.
 */

public class PreviousGames {


    private String id;
    private String winnerId;
    private String noVods;
    private Integer hasVod;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public String getNoVods() {
        return noVods;
    }

    public void setNoVods(String noVods) {
        this.noVods = noVods;
    }

    public Integer getHasVod() {
        return hasVod;
    }

    public void setHasVod(Integer hasVod) {
        this.hasVod = hasVod;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
