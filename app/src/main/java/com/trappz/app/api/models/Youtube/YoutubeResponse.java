package com.trappz.app.api.models.Youtube;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filipe Oliveira on 21-07-2014.
 */
public class YoutubeResponse
{

    private String kind;

    private String etag;

    private String nextPageToken;

    private PageInfo pageInfo;

    private List<Item> items = new ArrayList<Item>();

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
