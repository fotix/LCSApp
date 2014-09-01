package com.twistedsin.app.api.models.Twitter;

/**
 * Created by Filipe Oliveira on 14-08-2014.
 */
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Tweet {

    @SerializedName("created_at")
    private String DateCreated;

    @SerializedName("id")
    private String Id;

    @SerializedName("text")
    private String Text;

    @SerializedName("in_reply_to_status_id")
    private String InReplyToStatusId;

    @SerializedName("in_reply_to_user_id")
    private String InReplyToUserId;

    @SerializedName("in_reply_to_screen_name")
    private String InReplyToScreenName;

    @SerializedName("user")
    private TwitterUser User;

    @SerializedName("entities")
    private Entities entities;

    @SerializedName("retweeted")
    private Boolean retweeted;

    @SerializedName("retweeted_status")
    private Tweet retweeted_status;

    public Tweet getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(Tweet retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public Boolean getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(Boolean retweeted) {
        this.retweeted = retweeted;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public String getId() {
        return Id;
    }

    public String getInReplyToScreenName() {
        return InReplyToScreenName;
    }

    public String getInReplyToStatusId() {
        return InReplyToStatusId;
    }

    public String getInReplyToUserId() {
        return InReplyToUserId;
    }

    public String getText() {
        return Text;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
        InReplyToScreenName = inReplyToScreenName;
    }

    public void setInReplyToStatusId(String inReplyToStatusId) {
        InReplyToStatusId = inReplyToStatusId;
    }

    public void setInReplyToUserId(String inReplyToUserId) {
        InReplyToUserId = inReplyToUserId;
    }

    public void setText(String text) {
        Text = text;
    }

    public void setUser(TwitterUser user) {
        User = user;
    }

    public TwitterUser getUser() {
        return User;
    }

    @Override
    public String  toString(){
        return getText();
    }



}