package com.trappz.lcsmashup.api.models.Match;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filipe Oliveira on 08-07-2014.
 */
public class MatchContestants {

    private BlueTeam blue;
    private RedTeam red;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public BlueTeam getBlue() {
        return blue;
    }

    public void setBlue(BlueTeam blue) {
        this.blue = blue;
    }

    public RedTeam getRed() {
        return red;
    }

    public void setRed(RedTeam red) {
        this.red = red;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
