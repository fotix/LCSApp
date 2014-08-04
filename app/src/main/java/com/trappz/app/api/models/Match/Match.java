package com.trappz.app.api.models.Match;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filipe Oliveira on 08-07-2014.
 */
public class Match {

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private String color;
    private Tournament tournament;
    private String url;
    private String dateTime;
    private String winnerId;
    private String matchId;
    private String maxGames;
    private Boolean isLive;
    private String isFinished;
    private MatchContestants contestants;
    private Boolean liveStreams;
    private String polldaddyId;

    public Map<String, PreviousGames> getResult() {
        return result;
    }

    public void setResult(Map<String, PreviousGames> result) {
        this.result = result;
    }

    @SerializedName("games")
    Map<String, PreviousGames> result = new HashMap<String, PreviousGames>();
    
//    private List<PreviousGames> games;
    private String name;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMaxGames() {
        return maxGames;
    }

    public void setMaxGames(String maxGames) {
        this.maxGames = maxGames;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public MatchContestants getContestants() {
        return contestants;
    }

    public void setContestants(MatchContestants contestants) {
        this.contestants = contestants;
    }

    public Boolean getLiveStreams() {
        return liveStreams;
    }

    public void setLiveStreams(Boolean liveStreams) {
        this.liveStreams = liveStreams;
    }

    public String getPolldaddyId() {
        return polldaddyId;
    }

    public void setPolldaddyId(String polldaddyId) {
        this.polldaddyId = polldaddyId;
    }

//    public List<PreviousGames> getGames() {
//        return games;
//    }
//
//    public void setGames(List<PreviousGames> games) {
//        this.games = games;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
