package com.trappz.lcsmashup.api.models.Game;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filipe Oliveira on 09-07-2014.
 */
public class Player {

    private Integer id;
    private Integer teamId;
    private String name;
    private String photoURL;
    private Double kda;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Integer endLevel;
    private Integer minionsKilled;
    private Integer totalGold;
//    private String spell012;
//    private String spell14;
//    private String items03111;
//    private String items13211;
//    private String items21011;
//    private String items32049;
//    private String items43068;
//    private String items53082;
    private Integer championId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public Double getKda() {
        return kda;
    }

    public void setKda(Double kda) {
        this.kda = kda;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getEndLevel() {
        return endLevel;
    }

    public void setEndLevel(Integer endLevel) {
        this.endLevel = endLevel;
    }

    public Integer getMinionsKilled() {
        return minionsKilled;
    }

    public void setMinionsKilled(Integer minionsKilled) {
        this.minionsKilled = minionsKilled;
    }

    public Integer getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Integer totalGold) {
        this.totalGold = totalGold;
    }

//    public String getSpell012() {
//        return spell012;
//    }
//
//    public void setSpell012(String spell012) {
//        this.spell012 = spell012;
//    }
//
//    public String getSpell14() {
//        return spell14;
//    }
//
//    public void setSpell14(String spell14) {
//        this.spell14 = spell14;
//    }
//
//    public String getItems03111() {
//        return items03111;
//    }
//
//    public void setItems03111(String items03111) {
//        this.items03111 = items03111;
//    }
//
//    public String getItems13211() {
//        return items13211;
//    }
//
//    public void setItems13211(String items13211) {
//        this.items13211 = items13211;
//    }
//
//    public String getItems21011() {
//        return items21011;
//    }
//
//    public void setItems21011(String items21011) {
//        this.items21011 = items21011;
//    }
//
//    public String getItems32049() {
//        return items32049;
//    }
//
//    public void setItems32049(String items32049) {
//        this.items32049 = items32049;
//    }
//
//    public String getItems43068() {
//        return items43068;
//    }
//
//    public void setItems43068(String items43068) {
//        this.items43068 = items43068;
//    }
//
//    public String getItems53082() {
//        return items53082;
//    }
//
//    public void setItems53082(String items53082) {
//        this.items53082 = items53082;
//    }

    public Integer getChampionId() {
        return championId;
    }

    public void setChampionId(Integer championId) {
        this.championId = championId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

