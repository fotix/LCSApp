package com.trappz.lcsmashup.api.models.Game;

import com.google.gson.annotations.SerializedName;
import com.trappz.lcsmashup.api.models.Match.MatchContestants;
import com.trappz.lcsmashup.api.models.Match.Tournament;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filipe Oliveira on 09-07-2014.
 */
public class Game {

    private String dateTime;
    private String winnerId;
    private String maxGames;
    private String gameNumber;
    private Integer gameLength;
    private String matchId;
    private Tournament tournament;
    private Vods vods;
    private MatchContestants contestants;

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Player> result) {
        this.players = result;
    }

    @SerializedName("players")
    private Map<String, Player> players = new HashMap<String, Player>();

    private Integer noVods;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public String getMaxGames() {
        return maxGames;
    }

    public void setMaxGames(String maxGames) {
        this.maxGames = maxGames;
    }

    public String getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(String gameNumber) {
        this.gameNumber = gameNumber;
    }

    public Integer getGameLength() {
        return gameLength;
    }

    public void setGameLength(Integer gameLength) {
        this.gameLength = gameLength;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Vods getVods() {
        return vods;
    }

    public void setVods(Vods vods) {
        this.vods = vods;
    }

    public MatchContestants getContestants() {
        return contestants;
    }

    public void setContestants(MatchContestants contestants) {
        this.contestants = contestants;
    }

    public Integer getNoVods() {
        return noVods;
    }

    public void setNoVods(Integer noVods) {
        this.noVods = noVods;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
