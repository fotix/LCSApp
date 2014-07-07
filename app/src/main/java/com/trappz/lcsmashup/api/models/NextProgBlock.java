package com.trappz.lcsmashup.api.models;

import com.google.gson.annotations.Expose;

/**
 * Created by Filipe Oliveira on 07-07-2014.
 */
public class NextProgBlock {

    @Expose
    private Integer status;
    @Expose
    private String blockId;
    @Expose
    private String blockDate;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getBlockDate() {
        return blockDate;
    }

    public void setBlockDate(String blockDate) {
        this.blockDate = blockDate;
    }

}
