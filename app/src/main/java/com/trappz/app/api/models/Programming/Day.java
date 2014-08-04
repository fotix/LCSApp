package com.trappz.app.api.models.Programming;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filipe Oliveira on 07-07-2014.
 */
public class Day {

    @Expose
    private Integer blockNum;
    @Expose
    private List<String> blockIds = new ArrayList<String>();
    @Expose
    private String date;
    @Expose
    private String day;

    public Integer getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(Integer blockNum) {
        this.blockNum = blockNum;
    }

    public List<String> getBlockIds() {
        return blockIds;
    }

    public void setBlockIds(List<String> blockIds) {
        this.blockIds = blockIds;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
